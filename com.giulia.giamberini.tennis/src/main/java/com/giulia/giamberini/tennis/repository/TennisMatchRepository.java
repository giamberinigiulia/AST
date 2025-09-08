package com.giulia.giamberini.tennis.repository;

import java.time.LocalDate;
import java.util.List;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;

public interface TennisMatchRepository {

	List<TennisMatch> findAll();

	TennisMatch findByMatchInfo(TennisPlayer winner, TennisPlayer loser, LocalDate date);

	void save(TennisMatch tennisMatchToAdd);

}
