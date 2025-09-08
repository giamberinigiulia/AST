package com.giulia.giamberini.tennis.controller;

import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisPlayerController {

	private TennisPlayerRepository repo;
	private TennisManagementView view;

	public TennisPlayerController(TennisPlayerRepository repo, TennisManagementView view) {
		this.repo = repo;
		this.view = view;
	}

	public void findAllTennisPlayers() {
		view.showAllTennisPlayers(repo.findAll());
	}

	public void addNewTennisPlayer(TennisPlayer tennisPlayerToAdd) {
		TennisPlayer existingPlayer = repo.findById(tennisPlayerToAdd.getId());
		if (existingPlayer != null) {
			view.showErrorTennisPlayerAlreadyExist(
					"The selected id " + tennisPlayerToAdd.getId() + " is already in use by another player",
					existingPlayer);
			return;
		}
		repo.save(tennisPlayerToAdd);
		view.newTennisPlayerAdded(tennisPlayerToAdd);
	}

	public void deleteTennisPlayer(TennisPlayer tennisPlayerToRemove) {
		TennisPlayer existingPlayer = repo.findById(tennisPlayerToRemove.getId());
		if (existingPlayer != null) {
			repo.delete(tennisPlayerToRemove);
			view.tennisPlayerRemoved(tennisPlayerToRemove);
		}
	}

}
