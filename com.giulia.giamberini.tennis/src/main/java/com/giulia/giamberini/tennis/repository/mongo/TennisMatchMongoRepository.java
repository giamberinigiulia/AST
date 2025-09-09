package com.giulia.giamberini.tennis.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

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
		return collection.find(Filters.and(Filters.eq("winner", winner), Filters.eq("loser", loser),
				Filters.eq("dateOfTheMatch", date))).first();
	}

	@Override
	public void save(TennisMatch tennisMatchToAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TennisMatch matchToDelete) {
		// TODO Auto-generated method stub

	}

}
