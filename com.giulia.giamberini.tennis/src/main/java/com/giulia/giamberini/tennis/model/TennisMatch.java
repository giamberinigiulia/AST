package com.giulia.giamberini.tennis.model;

import java.time.LocalDate;
import java.util.Objects;

public class TennisMatch {
	private TennisPlayer winner;
	private TennisPlayer loser;
	private LocalDate dateOfTheMatch;

	public TennisMatch(TennisPlayer winner, TennisPlayer loser, LocalDate dateOfTheMatch) {
		this.winner = winner;
		this.loser = loser;
		this.dateOfTheMatch = dateOfTheMatch;
	}

	public TennisMatch() {
		// TODO Auto-generated constructor stub
	}

	public TennisPlayer getWinner() {
		return winner;
	}

	public TennisPlayer getLoser() {
		return loser;
	}

	public LocalDate getDateOfTheMatch() {
		return dateOfTheMatch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfTheMatch, loser, winner);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TennisMatch other = (TennisMatch) obj;
		return Objects.equals(dateOfTheMatch, other.dateOfTheMatch) && Objects.equals(loser, other.loser)
				&& Objects.equals(winner, other.winner);
	}

	@Override
	public String toString() {
		return "TennisMatch [winner=" + winner + ", loser=" + loser + ", dateOfTheMatch=" + dateOfTheMatch + "]";
	}

}
