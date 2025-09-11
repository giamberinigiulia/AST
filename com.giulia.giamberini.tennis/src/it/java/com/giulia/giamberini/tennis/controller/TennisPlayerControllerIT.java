package com.giulia.giamberini.tennis.controller;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.giulia.giamberini.tennis.repository.mongo.TennisMatchRepositoryMongo;
import com.giulia.giamberini.tennis.repository.mongo.TennisPlayerRepositoryMongo;
import com.giulia.giamberini.tennis.view.TennisManagementView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class TennisPlayerControllerIT {

	private static final String COLLECTION_NAME_MATCHES = "matches";
	private static final String COLLECTION_NAME_PLAYERS = "players";
	private static final String DATABASE_NAME_TENNIS_MATCHES = "tennis_matches";
	@Mock
	private TennisManagementView view;
	private TennisPlayerRepository playerRepo;
	private TennisMatchRepository matchRepo;
	private TennisPlayerController controller;
	private AutoCloseable closeable;
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0.13");

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		MongoClient client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		playerRepo = new TennisPlayerRepositoryMongo(client, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_PLAYERS);
		matchRepo = new TennisMatchRepositoryMongo(client, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_MATCHES);
		for (TennisPlayer tennisPlayer : playerRepo.findAll()) {
			playerRepo.delete(tennisPlayer);
		}
		controller = new TennisPlayerController(playerRepo, matchRepo, view);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllTennisPlayer() {
		TennisPlayer player1 = new TennisPlayer("1", "test name", "test surname");
		playerRepo.save(player1);
		controller.findAllTennisPlayers();
		verify(view).showAllTennisPlayers(Arrays.asList(player1));
	}

	@Test
	public void testAddNewTennisPlayer() {
		TennisPlayer player1 = new TennisPlayer("1", "test name", "test surname");
		controller.addNewTennisPlayer(player1);
		verify(view).newTennisPlayerAdded(player1);
	}

	@Test
	public void testRemoveTennisPlayerAlsoRemovesAssociatedMatches() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		TennisPlayer player3 = new TennisPlayer("3", "test name3", "test surname3");
		playerRepo.save(player1);
		playerRepo.save(player2);
		playerRepo.save(player3);
		LocalDate today = LocalDate.of(2025, 10, 10);
		TennisMatch match1 = new TennisMatch(player1, player2, today);
		TennisMatch match2 = new TennisMatch(player3, player1, today);
		matchRepo.save(match1);
		matchRepo.save(match2);
		controller.deleteTennisPlayer(player1);
		InOrder order = inOrder(view);
		order.verify(view).tennisMatchRemoved(match1);
		order.verify(view).tennisMatchRemoved(match2);
		order.verify(view).tennisPlayerRemoved(player1);
	}

}
