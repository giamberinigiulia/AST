package com.giulia.giamberini.tennis.repository;

import java.util.List;

import com.giulia.giamberini.tennis.model.TennisPlayer;

public interface TennisPlayerRepository {

	List<TennisPlayer> findAll();

	TennisPlayer findById(String id);

	void save(TennisPlayer tennisPlayerToAdd);

}
