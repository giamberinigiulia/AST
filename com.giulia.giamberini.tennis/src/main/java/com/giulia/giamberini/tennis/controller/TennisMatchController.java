package com.giulia.giamberini.tennis.controller;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisMatchController {

	private TennisMatchRepository repo;
	private TennisManagementView view;

	public TennisMatchController(TennisMatchRepository repo, TennisManagementView view) {
		this.repo = repo;
		this.view = view;
	}

	public void findAllTennisMatches() {
		view.showAllTennisMatches(repo.findAll());
	}

	public void addNewTennisMatch(TennisMatch tennisMatchToAdd) {
		TennisMatch existingMatch = repo.findByMatchInfo(tennisMatchToAdd.getId(), tennisMatchToAdd.getWinner(),
				tennisMatchToAdd.getLoser(), tennisMatchToAdd.getDateOfTheMatch());
		if (existingMatch != null) {
			view.showErrorTennisMatchAlreadyExist("The match between " + existingMatch.getWinner().toString() + " and "
					+ existingMatch.getLoser().toString() + " has been already played in the selected date "
					+ existingMatch.getDateOfTheMatch(), existingMatch);
			return;
		}
		repo.save(tennisMatchToAdd);
		view.newTennisMatchAdded(tennisMatchToAdd);
	}

}
