package com.giulia.giamberini.tennis.repository;

import java.util.List;

import com.giulia.giamberini.tennis.model.TennisMatch;

public interface TennisMatchRepository {

	List<TennisMatch> findAll();

}
