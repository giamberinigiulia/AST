package com.giulia.giamberini.tennis.view;

import java.util.List;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;

public interface TennisManagementView {

	void showAllTennisPlayers(List<TennisPlayer> players);

	void newTennisPlayerAdded(TennisPlayer tennisPlayerToAdd);

	void showErrorTennisPlayerAlreadyExist(String errorMessage, TennisPlayer existingTennisPlayer);

	void tennisPlayerRemoved(TennisPlayer tennisPlayerToRemove);

	void showErrorNotExistingTennisPlayer(String errorMessage, TennisPlayer tennisPlayerToRemove);

	void showAllTennisMatches(List<TennisMatch> matches);

	void newTennisMatchAdded(TennisMatch tennisMatchToAdd);

	void showErrorTennisMatchAlreadyExist(String errorMessage, TennisMatch existingMatch);

}
