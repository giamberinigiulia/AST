package com.giulia.giamberini.tennis.repository.mongo;

import java.time.LocalDate;
import java.util.List;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.mongodb.MongoClient;

public class TennisMatchMongoRepository implements TennisMatchRepository {

	public TennisMatchMongoRepository(MongoClient mognoClient, String databaseNameTennisMatches,
			String collectionNameMatches) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TennisMatch> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TennisMatch findByMatchInfo(TennisPlayer winner, TennisPlayer loser, LocalDate date) {
		// TODO Auto-generated method stub
		return null;
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
