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

public class TennisMatchMongoRepository implements TennisMatchRepository {

	private MongoCollection<TennisMatch> collection;

	public TennisMatchMongoRepository(MongoClient client, String databaseName, String collectionName) {
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
		Bson tennisMatchAsGiven = Filters.and(Filters.eq("winner", winner), Filters.eq("loser", loser),
				Filters.eq("dateOfTheMatch", date));
		Bson tennisMatchWithOppositeResult = Filters.and(Filters.eq("winner", loser), Filters.eq("loser", winner),
				Filters.eq("dateOfTheMatch", date));
		return collection.find(Filters.or(tennisMatchAsGiven, tennisMatchWithOppositeResult)).first();
	}

	@Override
	public void save(TennisMatch tennisMatchToAdd) {
		collection.insertOne(tennisMatchToAdd);
	}

	@Override
	public void delete(TennisMatch matchToDelete) {
		collection.deleteOne(Filters.and(Filters.eq("winner", matchToDelete.getWinner()),
				Filters.eq("loser", matchToDelete.getLoser()),
				Filters.eq("dateOfTheMatch", matchToDelete.getDateOfTheMatch())));
	}

	@Override
	public List<TennisMatch> findMatchesByTennisPlayerId(String playerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
