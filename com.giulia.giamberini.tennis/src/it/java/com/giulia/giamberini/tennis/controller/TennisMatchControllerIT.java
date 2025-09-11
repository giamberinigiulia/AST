package com.giulia.giamberini.tennis.controller;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.giulia.giamberini.tennis.repository.mongo.TennisMatchRepositoryMongo;
import com.giulia.giamberini.tennis.view.TennisManagementView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class TennisMatchControllerIT {

	private static final String COLLECTION_NAME_MATCHES = "matches";
	private static final String DATABASE_NAME_TENNIS_MATCHES = "tennis_matches";
	@Mock
	private TennisManagementView view;
	private TennisMatchRepository matchRepo;
	private TennisMatchController controller;
	private AutoCloseable closeable;
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0.13");

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		MongoClient client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		matchRepo = new TennisMatchRepositoryMongo(client, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_MATCHES);
		for (TennisMatch tennisPlayer : matchRepo.findAll()) {
			matchRepo.delete(tennisPlayer);
		}
		controller = new TennisMatchController(matchRepo, view);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testFindAllTennisMatches() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		LocalDate date = LocalDate.of(2025, 10, 10);
		TennisMatch match = new TennisMatch(player1, player2, date);
		matchRepo.save(match);
		controller.findAllTennisMatches();
		verify(view).showAllTennisMatches(Arrays.asList(match));
	}
}