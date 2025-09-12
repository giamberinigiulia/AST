package com.giulia.giamberini.tennis.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;

public class TennisManagementAppSwingE2E extends AssertJSwingJUnitTestCase {

	// Tennis Player Fixture Constants
	private static final String TENNIS_PLAYER_FIXTURE_1_ID = "1";
	private static final String TENNIS_PLAYER_FIXTURE_1_NAME = "Roger";
	private static final String TENNIS_PLAYER_FIXTURE_1_SURNAME = "Federer";

	private static final String TENNIS_PLAYER_FIXTURE_2_ID = "2";
	private static final String TENNIS_PLAYER_FIXTURE_2_NAME = "Rafael";
	private static final String TENNIS_PLAYER_FIXTURE_2_SURNAME = "Nadal";

	private static final String TENNIS_PLAYER_FIXTURE_3_ID = "3";
	private static final String TENNIS_PLAYER_FIXTURE_3_NAME = "Jannik";
	private static final String TENNIS_PLAYER_FIXTURE_3_SURNAME = "Sinner";

	// MongoDB Constants
	private static final String DATABASE_NAME_TENNIS_MATCHES = "tennis_matches";
	private static final String COLLECTION_NAME_PLAYERS = "players";
	private static final String COLLECTION_NAME_MATCHES = "matches";

	// Match Dates
	private static final LocalDate DATE_NADAL_FEDERER = LocalDate.of(2025, 10, 10);
	private static final LocalDate DATE_FEDERER_SINNER = LocalDate.of(2025, 10, 11);
	private static final LocalDate DATE_FEDERER_NADAL = LocalDate.of(2025, 10, 12);

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0.13");

	private MongoClient client;
	private FrameFixture window;
	private CodecRegistry pojoCodecRegistry;

	@Override
	protected void onSetUp() throws Exception {
		String host = mongo.getHost();
		Integer port = mongo.getFirstMappedPort();
		client = new MongoClient(new ServerAddress(host, port));
		pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		client.getDatabase(DATABASE_NAME_TENNIS_MATCHES).drop();
		populateTennisPlayerCollection();
		populateTennisMatchCollection();
		application("com.giulia.giamberini.tennis.app.swing.TennisManagementAppSwing").withArgs("--mongoHost=" + host,
				"--mongoPort=" + port.toString(), "--databaseName=" + DATABASE_NAME_TENNIS_MATCHES,
				"--playersColletionName=" + COLLECTION_NAME_PLAYERS,
				"--matchesColletionName=" + COLLECTION_NAME_MATCHES).start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Tennis Matches Management".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	@Override
	protected void onTearDown() {
		if (window != null) {
			window.cleanUp();
			window = null;
		}
		if (client != null) {
			client.close();
		}
	}

	private void populateTennisMatchCollection() {
		TennisPlayer rogerFederer = new TennisPlayer(TENNIS_PLAYER_FIXTURE_1_ID, TENNIS_PLAYER_FIXTURE_1_NAME,
				TENNIS_PLAYER_FIXTURE_1_SURNAME);
		TennisPlayer rafaelNadal = new TennisPlayer(TENNIS_PLAYER_FIXTURE_2_ID, TENNIS_PLAYER_FIXTURE_2_NAME,
				TENNIS_PLAYER_FIXTURE_2_SURNAME);
		TennisPlayer jannikSinner = new TennisPlayer(TENNIS_PLAYER_FIXTURE_3_ID, TENNIS_PLAYER_FIXTURE_3_NAME,
				TENNIS_PLAYER_FIXTURE_3_SURNAME);
		TennisMatch nadalWinFedererLose = new TennisMatch(rafaelNadal, rogerFederer, DATE_NADAL_FEDERER);
		TennisMatch federerWinSinnerLose = new TennisMatch(rogerFederer, jannikSinner, DATE_FEDERER_SINNER);
		TennisMatch federerWinNadalLose = new TennisMatch(rogerFederer, rafaelNadal, DATE_FEDERER_NADAL);
		client.getDatabase(DATABASE_NAME_TENNIS_MATCHES).withCodecRegistry(pojoCodecRegistry)
				.getCollection(COLLECTION_NAME_MATCHES, TennisMatch.class)
				.insertMany(Arrays.asList(nadalWinFedererLose, federerWinSinnerLose, federerWinNadalLose));
	}

	private void populateTennisPlayerCollection() {
		TennisPlayer rogerFederer = new TennisPlayer(TENNIS_PLAYER_FIXTURE_1_ID, TENNIS_PLAYER_FIXTURE_1_NAME,
				TENNIS_PLAYER_FIXTURE_1_SURNAME);
		TennisPlayer rafaelNadal = new TennisPlayer(TENNIS_PLAYER_FIXTURE_2_ID, TENNIS_PLAYER_FIXTURE_2_NAME,
				TENNIS_PLAYER_FIXTURE_2_SURNAME);
		TennisPlayer jannikSinner = new TennisPlayer(TENNIS_PLAYER_FIXTURE_3_ID, TENNIS_PLAYER_FIXTURE_3_NAME,
				TENNIS_PLAYER_FIXTURE_3_SURNAME);

		client.getDatabase(DATABASE_NAME_TENNIS_MATCHES).withCodecRegistry(pojoCodecRegistry)
				.getCollection(COLLECTION_NAME_PLAYERS, TennisPlayer.class)
				.insertMany(Arrays.asList(rogerFederer, rafaelNadal, jannikSinner));
	}

	@Test
	@GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list("playersList").contents())
				.anySatisfy(e -> assertThat(e).contains(TENNIS_PLAYER_FIXTURE_1_ID, TENNIS_PLAYER_FIXTURE_1_NAME,
						TENNIS_PLAYER_FIXTURE_1_SURNAME))
				.anySatisfy(e -> assertThat(e).contains(TENNIS_PLAYER_FIXTURE_2_ID, TENNIS_PLAYER_FIXTURE_2_NAME,
						TENNIS_PLAYER_FIXTURE_2_SURNAME))
				.anySatisfy(e -> assertThat(e).contains(TENNIS_PLAYER_FIXTURE_3_ID, TENNIS_PLAYER_FIXTURE_3_NAME,
						TENNIS_PLAYER_FIXTURE_3_SURNAME));
		assertThat(window.list("matchesList").contents())
				.anySatisfy(e -> assertThat(e).contains(TENNIS_PLAYER_FIXTURE_2_ID, TENNIS_PLAYER_FIXTURE_2_NAME,
						TENNIS_PLAYER_FIXTURE_2_SURNAME, TENNIS_PLAYER_FIXTURE_1_ID, TENNIS_PLAYER_FIXTURE_1_NAME,
						TENNIS_PLAYER_FIXTURE_1_SURNAME, DATE_NADAL_FEDERER.toString()))
				.anySatisfy(e -> assertThat(e).contains(TENNIS_PLAYER_FIXTURE_1_ID, TENNIS_PLAYER_FIXTURE_1_NAME,
						TENNIS_PLAYER_FIXTURE_1_SURNAME, TENNIS_PLAYER_FIXTURE_3_ID, TENNIS_PLAYER_FIXTURE_3_NAME,
						TENNIS_PLAYER_FIXTURE_3_SURNAME, DATE_FEDERER_SINNER.toString()))
				.anySatisfy(e -> assertThat(e).contains(TENNIS_PLAYER_FIXTURE_1_ID, TENNIS_PLAYER_FIXTURE_1_NAME,
						TENNIS_PLAYER_FIXTURE_1_SURNAME, TENNIS_PLAYER_FIXTURE_2_ID, TENNIS_PLAYER_FIXTURE_2_NAME,
						TENNIS_PLAYER_FIXTURE_2_SURNAME, DATE_FEDERER_NADAL.toString()));
	}

	@Test
	@GUITest
	public void testAddNewPlayerButtonSuccess() {
		window.textBox("idTextBox").enterText("4");
		window.textBox("nameTextBox").enterText("Novak");
		window.textBox("surnameTextBox").enterText("Djokovic");
		window.button(JButtonMatcher.withText("Add player")).click();
		assertThat(window.list("playersList").contents())
				.anySatisfy(e -> assertThat(e).contains("4", "Novak", "Djokovic"));
		assertThat(window.comboBox("winnerComboBox").contents())
				.anySatisfy(e -> assertThat(e).contains("4", "Novak", "Djokovic"));
		assertThat(window.comboBox("loserComboBox").contents())
				.anySatisfy(e -> assertThat(e).contains("4", "Novak", "Djokovic"));
	}

	@Test
	@GUITest
	public void testAddNewPlayerButtonShowError() {
		window.textBox("idTextBox").enterText(TENNIS_PLAYER_FIXTURE_1_ID); // reuse of the same ID as for Roger Federer
		window.textBox("nameTextBox").enterText("Andy");
		window.textBox("surnameTextBox").enterText("Murray");
		window.button(JButtonMatcher.withText("Add player")).click();
		// expected to see the already in memory tennis player
		assertThat(window.label("errorPlayerLbl").text()).contains(TENNIS_PLAYER_FIXTURE_1_ID,
				TENNIS_PLAYER_FIXTURE_1_NAME, TENNIS_PLAYER_FIXTURE_1_SURNAME);
	}

}
