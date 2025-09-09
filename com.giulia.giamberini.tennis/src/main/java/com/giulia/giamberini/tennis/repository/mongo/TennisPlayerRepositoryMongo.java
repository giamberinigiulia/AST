package com.giulia.giamberini.tennis.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class TennisPlayerRepositoryMongo implements TennisPlayerRepository {

	private MongoCollection<TennisPlayer> collection;

	public TennisPlayerRepositoryMongo(MongoClient client, String databaseName, String collectionName) {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		collection = client.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry).getCollection(collectionName,
				TennisPlayer.class);
	}

	@Override
	public List<TennisPlayer> findAll() {
		return collection.find().into(new ArrayList<>());
	}

	@Override
	public TennisPlayer findById(String id) {
		return collection.find(Filters.eq("_id", id)).first();
	}

	@Override
	public void save(TennisPlayer tennisPlayerToAdd) {
		collection.insertOne(tennisPlayerToAdd);
	}

	@Override
	public void delete(TennisPlayer tennisPlayerToRemove) {
		collection.deleteOne(Filters.eq("_id", tennisPlayerToRemove.getId()));
	}

}
