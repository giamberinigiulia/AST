package com.giulia.giamberini.tennis.controller;

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

}
