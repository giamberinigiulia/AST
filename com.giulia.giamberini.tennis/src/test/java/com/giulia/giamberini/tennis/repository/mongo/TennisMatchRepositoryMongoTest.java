package com.giulia.giamberini.tennis.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.Arrays;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TennisMatchRepositoryMongoTest {

	private static final String COLLECTION_NAME_MATCHES = "matches";
	private static final String DATABASE_NAME_TENNIS_MATCHES = "tennis_matches";
	private static final String WINNER_ID = "1";
	private static final String WINNER_NAME = "winner name";
	private static final String WINNER_SURNAME = "winner surname";
	private static final String LOSER_ID = "2";
	private static final String LOSER_NAME = "loser name";
	private static final String LOSER_SURNAME = "loser surname";
	
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0.13");
	private MongoClient mongoClient;
	private TennisMatchMongoRepository repo;
	private MongoCollection<TennisMatch> collection;

	@Before
	public void setup() {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		repo = new TennisMatchMongoRepository(mongoClient, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_MATCHES);
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME_TENNIS_MATCHES)
				.withCodecRegistry(pojoCodecRegistry);
		database.drop();
		collection = database.getCollection(COLLECTION_NAME_MATCHES, TennisMatch.class);
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Test
	public void testFindAllWhenDbIsEmpty() {
		assertThat(repo.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenThereAreElementsInTheDB() {
		TennisPlayer winner = new TennisPlayer(WINNER_ID, WINNER_NAME, WINNER_SURNAME);
		TennisPlayer loser = new TennisPlayer(LOSER_ID, LOSER_NAME, LOSER_SURNAME);
		LocalDate tennisMatchDate1 = LocalDate.of(2025, 10, 22);
		LocalDate tennisMatchDate2 = LocalDate.of(2025, 10, 22);
		TennisMatch tennisMatch1 = new TennisMatch(winner, loser, tennisMatchDate1);
		TennisMatch tennisMatch2 = new TennisMatch(winner, loser, tennisMatchDate2);
		collection.insertMany(Arrays.asList(tennisMatch1, tennisMatch2));
		assertThat(repo.findAll()).containsExactly(tennisMatch1, tennisMatch2);
	}

}
