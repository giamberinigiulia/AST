package com.giulia.giamberini.tennis.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class TennisMatchRepositoryMongo implements TennisMatchRepository {

	private static final String DATE_OF_THE_MATCH = "dateOfTheMatch";
	private static final String LOSER = "loser";
	private static final String WINNER = "winner";
	private MongoCollection<TennisMatch> collection;

	public TennisMatchRepositoryMongo(MongoClient client, String databaseName, String collectionName) {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		collection = client.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry).getCollection(collectionName,
				TennisMatch.class);
	}

	@Override
	public List<TennisMatch> findAll() {
		return collection.find().into(new ArrayList<>());
	}

	@Override
	public TennisMatch findByMatchInfo(TennisPlayer winner, TennisPlayer loser, LocalDate date) {
		Bson tennisMatchAsGiven = Filters.and(Filters.eq(WINNER, winner), Filters.eq(LOSER, loser),
				Filters.eq(DATE_OF_THE_MATCH, date));
		Bson tennisMatchWithOppositeResult = Filters.and(Filters.eq(WINNER, loser), Filters.eq(LOSER, winner),
				Filters.eq(DATE_OF_THE_MATCH, date));
		return collection.find(Filters.or(tennisMatchAsGiven, tennisMatchWithOppositeResult)).first();
	}

	@Override
	public void save(TennisMatch tennisMatchToAdd) {
		collection.insertOne(tennisMatchToAdd);
	}

	@Override
	public void delete(TennisMatch matchToDelete) {
		collection.deleteOne(Filters.and(Filters.eq(WINNER, matchToDelete.getWinner()),
				Filters.eq(LOSER, matchToDelete.getLoser()),
				Filters.eq(DATE_OF_THE_MATCH, matchToDelete.getDateOfTheMatch())));
	}

	@Override
	public List<TennisMatch> findMatchesByTennisPlayerId(String playerId) {
		return collection.find(Filters.or(Filters.eq("winner._id", playerId), Filters.eq("loser._id", playerId)))
				.into(new ArrayList<>());
	}

}
