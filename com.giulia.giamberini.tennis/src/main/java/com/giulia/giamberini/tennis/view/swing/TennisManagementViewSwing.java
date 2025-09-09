package com.giulia.giamberini.tennis.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

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

	private JPanel playerPanel;
	private GridBagLayout gridBagLayout;
	private JLabel idLbl;
	private JLabel nameLbl;
	private JTextField nameTextBox;
	private JLabel surnameLbl;
	private JTextField surnameTextBox;
	private JButton addPlayerBtn;
	private JScrollPane playersListScrollPane;
	private JList<TennisPlayer> playersList;
	private JButton deletePlayerBtn;
	private JLabel errorPlayerLbl;
	private JPanel matchPanel;
	private JLabel winnerLbl;
	private JComboBox<TennisPlayer> winnerComboBox;
	private JLabel loserLbl;
	private JComboBox<TennisPlayer> loserComboBox;
	private JLabel dateLbl;
	private JTextField dateOfTheMatchTextBox;
	private JButton addMatchBtn;
	private JScrollPane matchesListScrollPane;
	private JList<TennisMatch> matchesList;
	private JButton deleteMatchBtn;
	private JLabel errorMatchLbl;
	private JTextField idTextBox;
	private DefaultListModel<TennisPlayer> listPlayerModel;

	public TennisManagementViewSwing() {

		gridBagLayout = new GridBagLayout();
		setSize(600, 400);
		setTitle("Tennis Matches Management");
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 363, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		playerPanel = new JPanel();
		playerPanel.setBorder(
				new TitledBorder(null, "Player management", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_playerPanel = new GridBagConstraints();
		gbc_playerPanel.insets = new Insets(0, 0, 0, 5);
		gbc_playerPanel.fill = GridBagConstraints.BOTH;
		gbc_playerPanel.gridx = 0;
		gbc_playerPanel.gridy = 0;
		gbc_playerPanel.weightx = 0.5;
		gbc_playerPanel.weighty = 1.0;
		gbc_playerPanel.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(playerPanel, gbc_playerPanel);
		GridBagLayout gbl_playerPanel = new GridBagLayout();
		gbl_playerPanel.columnWidths = new int[] { 0, 0 };
		gbl_playerPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_playerPanel.columnWeights = new double[] { 0.0, 1.0 };
		gbl_playerPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		playerPanel.setLayout(gbl_playerPanel);

		idLbl = new JLabel("ID");
		idLbl.setName("idLbl");
		GridBagConstraints gbc_idLbl = new GridBagConstraints();
		gbc_idLbl.insets = new Insets(0, 0, 5, 5);
		gbc_idLbl.anchor = GridBagConstraints.EAST;
		gbc_idLbl.gridx = 0;
		gbc_idLbl.gridy = 0;
		playerPanel.add(idLbl, gbc_idLbl);

		idTextBox = new JTextField();
		KeyAdapter addPlayerBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				addPlayerBtn.setEnabled(!idTextBox.getText().trim().isEmpty() && !nameTextBox.getText().trim().isEmpty()
						&& !surnameTextBox.getText().trim().isEmpty());
			}
		};
		idTextBox.addKeyListener(addPlayerBtnEnabler);
		idTextBox.setName("idTextBox");
		GridBagConstraints gbc_idTextBox = new GridBagConstraints();
		gbc_idTextBox.insets = new Insets(0, 0, 5, 0);
		gbc_idTextBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_idTextBox.gridx = 1;
		gbc_idTextBox.gridy = 0;
		playerPanel.add(idTextBox, gbc_idTextBox);
		idTextBox.setColumns(10);

		nameLbl = new JLabel("Name");
		nameLbl.setName("nameLbl");
		GridBagConstraints gbc_nameLbl = new GridBagConstraints();
		gbc_nameLbl.anchor = GridBagConstraints.EAST;
		gbc_nameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_nameLbl.gridx = 0;
		gbc_nameLbl.gridy = 1;
		playerPanel.add(nameLbl, gbc_nameLbl);

		nameTextBox = new JTextField();
		nameTextBox.addKeyListener(addPlayerBtnEnabler);
		nameTextBox.setName("nameTextBox");
		GridBagConstraints gbc_nameTextBox = new GridBagConstraints();
		gbc_nameTextBox.insets = new Insets(0, 0, 5, 0);
		gbc_nameTextBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameTextBox.gridx = 1;
		gbc_nameTextBox.gridy = 1;
		playerPanel.add(nameTextBox, gbc_nameTextBox);
		nameTextBox.setColumns(10);

		surnameLbl = new JLabel("Surname");
		surnameLbl.setName("surnameLbl");
		GridBagConstraints gbc_surnameLbl = new GridBagConstraints();
		gbc_surnameLbl.anchor = GridBagConstraints.EAST;
		gbc_surnameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_surnameLbl.gridx = 0;
		gbc_surnameLbl.gridy = 2;
		playerPanel.add(surnameLbl, gbc_surnameLbl);

		surnameTextBox = new JTextField();
		surnameTextBox.addKeyListener(addPlayerBtnEnabler);
		surnameTextBox.setName("surnameTextBox");
		GridBagConstraints gbc_surnameTextBox = new GridBagConstraints();
		gbc_surnameTextBox.insets = new Insets(0, 0, 5, 0);
		gbc_surnameTextBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_surnameTextBox.gridx = 1;
		gbc_surnameTextBox.gridy = 2;
		playerPanel.add(surnameTextBox, gbc_surnameTextBox);
		surnameTextBox.setColumns(10);

		addPlayerBtn = new JButton("Add player");
		addPlayerBtn.setEnabled(false);
		addPlayerBtn.setName("addPlayerBtn");
		GridBagConstraints gbc_addPlayerBtn = new GridBagConstraints();
		gbc_addPlayerBtn.gridwidth = 2;
		gbc_addPlayerBtn.insets = new Insets(0, 0, 5, 0);
		gbc_addPlayerBtn.gridx = 0;
		gbc_addPlayerBtn.gridy = 3;
		playerPanel.add(addPlayerBtn, gbc_addPlayerBtn);

		playersListScrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		playerPanel.add(playersListScrollPane, gbc_scrollPane);

		listPlayerModel = new DefaultListModel<TennisPlayer>();
		playersList = new JList<TennisPlayer>(listPlayerModel);
		playersList.addListSelectionListener(arg0 -> deletePlayerBtn.setEnabled(playersList.getSelectedIndex() != -1));
		playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playersList.setName("playersList");
		playersListScrollPane.setViewportView(playersList);

		deletePlayerBtn = new JButton("Delete player");
		deletePlayerBtn.setEnabled(false);
		deletePlayerBtn.setName("deletePlayerBtn");
		GridBagConstraints gbc_deletePlayerBtn = new GridBagConstraints();
		gbc_deletePlayerBtn.insets = new Insets(0, 0, 5, 0);
		gbc_deletePlayerBtn.gridwidth = 2;
		gbc_deletePlayerBtn.gridx = 0;
		gbc_deletePlayerBtn.gridy = 5;
		playerPanel.add(deletePlayerBtn, gbc_deletePlayerBtn);

		errorPlayerLbl = new JLabel(" ");
		errorPlayerLbl.setForeground(new Color(255, 0, 0));
		errorPlayerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		errorPlayerLbl.setName("errorPlayerLbl");
		GridBagConstraints gbc_errorPlayerLbl = new GridBagConstraints();
		gbc_errorPlayerLbl.gridwidth = 2;
		gbc_errorPlayerLbl.gridx = 0;
		gbc_errorPlayerLbl.gridy = 6;
		playerPanel.add(errorPlayerLbl, gbc_errorPlayerLbl);

		matchPanel = new JPanel();
		matchPanel.setBorder(
				new TitledBorder(null, "Match management", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_matchPanel = new GridBagConstraints();
		gbc_matchPanel.fill = GridBagConstraints.BOTH;
		gbc_matchPanel.gridx = 1;
		gbc_matchPanel.gridy = 0;
		gbc_matchPanel.weightx = 0.5;
		gbc_matchPanel.weighty = 1;
		gbc_matchPanel.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(matchPanel, gbc_matchPanel);
		GridBagLayout gbl_matchPanel = new GridBagLayout();
		gbl_matchPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_matchPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_matchPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_matchPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		matchPanel.setLayout(gbl_matchPanel);

		winnerLbl = new JLabel("Winner");
		winnerLbl.setName("winnerLbl");
		GridBagConstraints gbc_winnerLbl = new GridBagConstraints();
		gbc_winnerLbl.insets = new Insets(0, 0, 5, 5);
		gbc_winnerLbl.anchor = GridBagConstraints.EAST;
		gbc_winnerLbl.gridx = 0;
		gbc_winnerLbl.gridy = 0;
		matchPanel.add(winnerLbl, gbc_winnerLbl);

		winnerComboBox = new JComboBox<>();
		winnerComboBox.setName("winnerComboBox");
		winnerComboBox.setEnabled(false);
		GridBagConstraints gbc_winnerComboBox = new GridBagConstraints();
		gbc_winnerComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_winnerComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_winnerComboBox.gridx = 1;
		gbc_winnerComboBox.gridy = 0;
		matchPanel.add(winnerComboBox, gbc_winnerComboBox);

		loserLbl = new JLabel("Loser");
		loserLbl.setName("loserLbl");
		GridBagConstraints gbc_loserLbl = new GridBagConstraints();
		gbc_loserLbl.anchor = GridBagConstraints.EAST;
		gbc_loserLbl.insets = new Insets(0, 0, 5, 5);
		gbc_loserLbl.gridx = 0;
		gbc_loserLbl.gridy = 1;
		matchPanel.add(loserLbl, gbc_loserLbl);

		loserComboBox = new JComboBox<>();
		loserComboBox.setName("loserComboBox");
		loserComboBox.setEnabled(false);
		GridBagConstraints gbc_loserComboBox = new GridBagConstraints();
		gbc_loserComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_loserComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_loserComboBox.gridx = 1;
		gbc_loserComboBox.gridy = 1;
		matchPanel.add(loserComboBox, gbc_loserComboBox);

		dateLbl = new JLabel("Date");
		dateLbl.setName("dateLbl");
		GridBagConstraints gbc_dateLbl = new GridBagConstraints();
		gbc_dateLbl.anchor = GridBagConstraints.EAST;
		gbc_dateLbl.insets = new Insets(0, 0, 5, 5);
		gbc_dateLbl.gridx = 0;
		gbc_dateLbl.gridy = 2;
		matchPanel.add(dateLbl, gbc_dateLbl);

		dateOfTheMatchTextBox = new JTextField();
		dateOfTheMatchTextBox.setName("dateOfTheMatchTextBox");
		GridBagConstraints gbc_dateOfTheMatchTextBox = new GridBagConstraints();
		gbc_dateOfTheMatchTextBox.insets = new Insets(0, 0, 5, 0);
		gbc_dateOfTheMatchTextBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateOfTheMatchTextBox.gridx = 1;
		gbc_dateOfTheMatchTextBox.gridy = 2;
		matchPanel.add(dateOfTheMatchTextBox, gbc_dateOfTheMatchTextBox);
		dateOfTheMatchTextBox.setColumns(10);

		addMatchBtn = new JButton("Add match");
		addMatchBtn.setName("addMatchBtn");
		addMatchBtn.setEnabled(false);
		GridBagConstraints gbc_addMatchBtn = new GridBagConstraints();
		gbc_addMatchBtn.gridwidth = 2;
		gbc_addMatchBtn.insets = new Insets(0, 0, 5, 0);
		gbc_addMatchBtn.gridx = 0;
		gbc_addMatchBtn.gridy = 3;
		matchPanel.add(addMatchBtn, gbc_addMatchBtn);

		matchesListScrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 4;
		matchPanel.add(matchesListScrollPane, gbc_scrollPane_1);

		matchesList = new JList<>();
		matchesList.setName("matchesList");
		matchesListScrollPane.setViewportView(matchesList);

		deleteMatchBtn = new JButton("Delete match");
		deleteMatchBtn.setName("deleteMatchBtn");
		deleteMatchBtn.setEnabled(false);
		GridBagConstraints gbc_deleteMatchBtn = new GridBagConstraints();
		gbc_deleteMatchBtn.gridwidth = 2;
		gbc_deleteMatchBtn.insets = new Insets(0, 0, 5, 0);
		gbc_deleteMatchBtn.gridx = 0;
		gbc_deleteMatchBtn.gridy = 5;
		matchPanel.add(deleteMatchBtn, gbc_deleteMatchBtn);

		errorMatchLbl = new JLabel(" ");
		errorMatchLbl.setName("errorMatchLbl");
		GridBagConstraints gbc_errorMatchLbl = new GridBagConstraints();
		gbc_errorMatchLbl.gridwidth = 2;
		gbc_errorMatchLbl.gridx = 0;
		gbc_errorMatchLbl.gridy = 6;
		matchPanel.add(errorMatchLbl, gbc_errorMatchLbl);

	}

	@Override
	public void showAllTennisPlayers(List<TennisPlayer> players) {
		for (TennisPlayer tennisPlayer : players) {
			listPlayerModel.addElement(tennisPlayer);
		}
	}

	@Override
	public void newTennisPlayerAdded(TennisPlayer tennisPlayerToAdd) {
		listPlayerModel.addElement(tennisPlayerToAdd);
		errorPlayerLbl.setText(" ");
	}

	@Override
	public void showErrorTennisPlayerAlreadyExist(String errorMessage, TennisPlayer existingTennisPlayer) {
		errorPlayerLbl.setText(errorMessage + ": " + existingTennisPlayer.getId() + " - "
				+ existingTennisPlayer.getName() + " - " + existingTennisPlayer.getSurname());
	}

	@Override
	public void tennisPlayerRemoved(TennisPlayer tennisPlayerToRemove) {
		listPlayerModel.removeElement(tennisPlayerToRemove);
		errorPlayerLbl.setText(" ");
	}

	@Override
	public void showErrorNotExistingTennisPlayer(String errorMessage, TennisPlayer tennisPlayerToRemove) {
		errorPlayerLbl.setText(errorMessage + ": " + tennisPlayerToRemove.getId() + " - "
				+ tennisPlayerToRemove.getName() + " - " + tennisPlayerToRemove.getSurname());
		listPlayerModel.removeElement(tennisPlayerToRemove);
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

	public DefaultListModel<TennisPlayer> getListPlayerModel() {
		return listPlayerModel;
	}

}
