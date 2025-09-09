package com.giulia.giamberini.tennis.controller;

import java.util.List;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisPlayerController {

	private TennisPlayerRepository playersRepo;
	private TennisManagementView view;
	private TennisMatchRepository matchesRepo;

	public TennisPlayerController(TennisPlayerRepository playersRepo, TennisMatchRepository matchesRepo, TennisManagementView view) {
		this.playersRepo = playersRepo;
		this.matchesRepo = matchesRepo;
		this.view = view;
	}

	public void findAllTennisPlayers() {
		view.showAllTennisPlayers(playersRepo.findAll());
	}

	public void addNewTennisPlayer(TennisPlayer tennisPlayerToAdd) {
		TennisPlayer existingPlayer = playersRepo.findById(tennisPlayerToAdd.getId());
		if (existingPlayer != null) {
			view.showErrorTennisPlayerAlreadyExist(
					"The selected id " + tennisPlayerToAdd.getId() + " is already in use by another player",
					existingPlayer);
			return;
		}
		playersRepo.save(tennisPlayerToAdd);
		view.newTennisPlayerAdded(tennisPlayerToAdd);
	}

	public void deleteTennisPlayer(TennisPlayer tennisPlayerToRemove) {
		TennisPlayer existingPlayer = playersRepo.findById(tennisPlayerToRemove.getId());
		if (existingPlayer == null) {
			view.showErrorNotExistingTennisPlayer(
					"The selected id " + tennisPlayerToRemove.getId() + " is not associated with any tennis player",
					tennisPlayerToRemove);
			return;
		}
		List<TennisMatch> matchesByTennisPlayerId = matchesRepo.findMatchesByTennisPlayerId(tennisPlayerToRemove.getId());
		for (TennisMatch tennisMatch : matchesByTennisPlayerId) {
			matchesRepo.delete(tennisMatch);
			view.tennisMatchRemoved(tennisMatch);
		}
		playersRepo.delete(tennisPlayerToRemove);
		view.tennisPlayerRemoved(tennisPlayerToRemove);
	}

}
