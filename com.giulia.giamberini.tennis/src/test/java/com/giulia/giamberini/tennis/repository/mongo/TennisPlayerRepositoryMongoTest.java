package com.giulia.giamberini.tennis.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TennisPlayerRepositoryMongoTest {

	private static final String COLLECTION_NAME_PLAYERS = "players";
	private static final String DATABASE_NAME_TENNIS_MATCHES = "tennis_matches";
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0.13");
	private MongoClient mongoClient;
	private TennisPlayerRepositoryMongo repo;
	private MongoCollection<TennisPlayer> collection;

	@Before
	public void setup() {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		repo = new TennisPlayerRepositoryMongo(mongoClient, DATABASE_NAME_TENNIS_MATCHES, COLLECTION_NAME_PLAYERS);
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME_TENNIS_MATCHES).withCodecRegistry(pojoCodecRegistry);
		database.drop();
		collection = database.getCollection(COLLECTION_NAME_PLAYERS, TennisPlayer.class);
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Test
	public void testFindAllWhenDBIsEmpty() {
		assertThat(repo.findAll()).isEmpty();
	}

}
