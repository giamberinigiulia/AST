package com.giulia.giamberini.tennis.model;

import java.util.Date;

public class Match {
	private String id;
	private TennisPlayer winner;
	private TennisPlayer loser;
	private Date dateOfTheMatch;

	public Match(String id, TennisPlayer winner, TennisPlayer loser, Date dateOfTheMatch) {
		this.id = id;
		this.winner = winner;
		this.loser = loser;
		this.dateOfTheMatch = dateOfTheMatch;
	}

	public String getId() {
		return id;
	}

	public TennisPlayer getWinner() {
		return winner;
	}

	public TennisPlayer getLoser() {
		return loser;
	}

	public Date getDateOfTheMatch() {
		return dateOfTheMatch;
	}

}
