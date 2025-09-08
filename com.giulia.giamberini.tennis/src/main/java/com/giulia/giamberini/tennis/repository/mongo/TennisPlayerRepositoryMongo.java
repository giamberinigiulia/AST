package com.giulia.giamberini.tennis.repository.mongo;

import java.util.List;

import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.mongodb.MongoClient;

public class TennisPlayerRepositoryMongo implements TennisPlayerRepository {

	public TennisPlayerRepositoryMongo(MongoClient client, String databaseName, String collectionName) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<TennisPlayer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TennisPlayer findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(TennisPlayer tennisPlayerToAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TennisPlayer tennisPlayerToRemove) {
		// TODO Auto-generated method stub

	}

}
