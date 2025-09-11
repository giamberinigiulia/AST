package com.giulia.giamberini.tennis.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.giulia.giamberini.tennis.controller.TennisMatchController;
import com.giulia.giamberini.tennis.controller.TennisPlayerController;
import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.mongo.TennisMatchRepositoryMongo;
import com.giulia.giamberini.tennis.repository.mongo.TennisPlayerRepositoryMongo;
import com.giulia.giamberini.tennis.view.swing.TennisManagementViewSwing;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

	private static final String COLLECTION_NAME_MATCHES = "matches";
	private static final String COLLECTION_NAME_PLAYERS = "players";
	private static final String DATABASE_NAME_TENNIS_MATCHES = "tennis_matches";
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0.13");
	private MongoClient client;
	private FrameFixture window;
	private TennisManagementViewSwing view;
	private TennisPlayerRepositoryMongo playerRepo;;
	private TennisMatchRepositoryMongo matchRepo;;
	private TennisPlayerController playerController;
	private TennisMatchController matchController;

	@Override
	protected void onSetUp() throws Exception {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		playerRepo = new TennisPlayerRepositoryMongo(client, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_PLAYERS);
		matchRepo = new TennisMatchRepositoryMongo(client, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_MATCHES);
		for (TennisPlayer tennisPlayer : playerRepo.findAll()) {
			playerRepo.delete(tennisPlayer);
		}
		for (TennisMatch tennisMatch : matchRepo.findAll()) {
			matchRepo.delete(tennisMatch);
		}
		GuiActionRunner.execute(() -> {
			view = new TennisManagementViewSwing();
			playerController = new TennisPlayerController(playerRepo, matchRepo, view);
			matchController = new TennisMatchController(matchRepo, view);
			view.setPlayerController(playerController);
			view.setMatchController(matchController);
			return view;
		});
		window = new FrameFixture(robot(), view);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		client.close();
	}

	@Test
	@GUITest
	public void testAddTennisPlayerButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test name");
		window.textBox("surnameTextBox").enterText("test surname");
		window.button(JButtonMatcher.withText("Add player")).click();
		assertThat(playerRepo.findById("1")).isEqualTo(new TennisPlayer("1", "test name", "test surname"));
	}

	@Test
	@GUITest
	public void testDeleteTennisPlayer() {
		playerRepo.save(new TennisPlayer("1", "test name", "test surname"));
		GuiActionRunner.execute(() -> playerController.findAllTennisPlayers());
		window.list("playersList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete player")).click();
		assertThat(playerRepo.findById("1")).isNull();
	}

	@Test
	@GUITest
	public void testAddTennisMatch() {
		TennisPlayer winner = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer loser = new TennisPlayer("2", "test name2", "test surname2");
		LocalDate date = LocalDate.of(2025, 10, 10);
		playerRepo.save(winner);
		playerRepo.save(loser);
		GuiActionRunner.execute(() -> playerController.findAllTennisPlayers());
		window.comboBox("winnerComboBox").selectItem(0);
		window.comboBox("loserComboBox").selectItem(1);
		window.textBox("dateOfTheMatchTextBox").enterText(date.toString());
		window.button(JButtonMatcher.withText("Add match")).click();
		assertThat(matchRepo.findByMatchInfo(winner, loser, date)).isEqualTo(new TennisMatch(winner, loser, date));
	}

}
