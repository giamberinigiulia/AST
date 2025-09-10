package com.giulia.giamberini.tennis.controller;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

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

}
