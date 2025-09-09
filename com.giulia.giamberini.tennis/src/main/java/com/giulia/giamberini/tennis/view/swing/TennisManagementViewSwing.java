package com.giulia.giamberini.tennis.view.swing;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisManagementViewSwing extends JFrame implements TennisManagementView {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TennisManagementViewSwing frame = new TennisManagementViewSwing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void showAllTennisPlayers(List<TennisPlayer> players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newTennisPlayerAdded(TennisPlayer tennisPlayerToAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorTennisPlayerAlreadyExist(String errorMessage, TennisPlayer existingTennisPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tennisPlayerRemoved(TennisPlayer tennisPlayerToRemove) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorNotExistingTennisPlayer(String errorMessage, TennisPlayer tennisPlayerToRemove) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showAllTennisMatches(List<TennisMatch> matches) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newTennisMatchAdded(TennisMatch tennisMatchToAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorTennisMatchAlreadyExist(String errorMessage, TennisMatch existingMatch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tennisMatchRemoved(TennisMatch matchToDelete) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorNotExistingTennisMatch(String errorMessage, TennisMatch matchToDelete) {
		// TODO Auto-generated method stub

	}

}
