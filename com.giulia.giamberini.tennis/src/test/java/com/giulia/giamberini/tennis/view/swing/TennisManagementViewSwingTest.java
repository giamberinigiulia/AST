package com.giulia.giamberini.tennis.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.giulia.giamberini.tennis.controller.TennisPlayerController;
import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;

@RunWith(GUITestRunner.class)
public class TennisManagementViewSwingTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private TennisManagementViewSwing view;
	@Mock
	private TennisPlayerController playerController;
	private AutoCloseable closeable;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			view = new TennisManagementViewSwing();
			view.setPlayerController(playerController);
			return view;
		});
		window = new FrameFixture(robot(), view);
		window.show();
	}

	@Override
	public void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testInitialConfiguration() {
		window.requireTitle("Tennis Matches Management");
		window.label(JLabelMatcher.withText("ID"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Name"));
		window.textBox("nameTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Surname"));
		window.textBox("surnameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add player")).requireDisabled();
		window.list("playersList").requireEnabled();
		window.button(JButtonMatcher.withText("Delete player")).requireDisabled();
		window.label("errorPlayerLbl").requireText(" ");
		window.label(JLabelMatcher.withText("Winner"));
		window.comboBox("winnerComboBox").requireDisabled();
		window.label(JLabelMatcher.withText("Loser"));
		window.comboBox("loserComboBox").requireDisabled();
		window.label(JLabelMatcher.withText("Date"));
		window.textBox("dateOfTheMatchTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add match")).requireDisabled();
		window.list("matchesList").requireEnabled();
		window.button(JButtonMatcher.withText("Delete match")).requireDisabled();
		window.label("errorMatchLbl").requireText(" ");
	}

	@Test
	@GUITest
	public void testAddPlayerButtonEnableddWhenIdAndNameAndSurnameAreNotEmpty() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test name");
		window.textBox("surnameTextBox").enterText("test surname");
		window.button(JButtonMatcher.withText("Add player")).requireEnabled();
	}

	@Test
	@GUITest
	public void testAddPlayerButtonShouldRemainDisabledWhenAtLeastOneOfIdNameAndSurnameIsBlank() {
		window.textBox("idTextBox").enterText(" ");
		window.textBox("nameTextBox").enterText("test name");
		window.textBox("surnameTextBox").enterText("test surname");
		window.button(JButtonMatcher.withText("Add player")).requireDisabled();

		window.textBox("idTextBox").setText("");
		window.textBox("nameTextBox").setText("");
		window.textBox("surnameTextBox").setText("");

		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText(" ");
		window.textBox("surnameTextBox").enterText("test surname");
		window.button(JButtonMatcher.withText("Add player")).requireDisabled();

		window.textBox("idTextBox").setText("");
		window.textBox("nameTextBox").setText("");
		window.textBox("surnameTextBox").setText("");

		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test name");
		window.textBox("surnameTextBox").enterText(" ");
		window.button(JButtonMatcher.withText("Add player")).requireDisabled();
	}

	@Test
	@GUITest
	public void testSelectionOfPlayerShouldEnableDeletePlayerButton() {
		GuiActionRunner.execute(
				() -> view.getListPlayerModel().addElement(new TennisPlayer("1", "test name", "test surname")));

		window.list("playersList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete player")).requireEnabled();
		window.list("playersList").clearSelection();
		window.button(JButtonMatcher.withText("Delete player")).requireDisabled();
	}

	@Test
	@GUITest
	public void testPlayerAreAddedToTheListWhenShowAllTennisPlayers() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> view.showAllTennisPlayers(Arrays.asList(player1, player2)));
		assertThat(window.list("playersList").contents()).containsExactly(player1.toString(), player2.toString());
	}

	@Test
	@GUITest
	public void testshowErrorTennisPlayerAlreadyExistShouldShowTheErrorMessageInTheErrorPlayerLabel() {
		TennisPlayer player = new TennisPlayer("1", "test name", "test surname");
		GuiActionRunner.execute(() -> view.showErrorTennisPlayerAlreadyExist("Error message", player));
		window.label("errorPlayerLbl").requireText("Error message: 1 - test name - test surname");
	}

	@Test
	@GUITest
	public void testShowErrorPlayerNotFound() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<TennisPlayer> listPlayersModel = view.getListPlayerModel();
			listPlayersModel.addElement(player1);
			listPlayersModel.addElement(player2);
		});
		GuiActionRunner.execute(() -> view.showErrorNotExistingTennisPlayer("Error message", player1));
		window.label("errorPlayerLbl").requireText("Error message: 1 - test name1 - test surname1");
		assertThat(window.list("playersList").contents()).containsExactly(player2.toString());
	}

	@Test
	@GUITest
	public void testPlayerSuccessfullyAddedShouldResetTheErrorLabelAndAddTheNewPlayerToTheList() {
		TennisPlayer player = new TennisPlayer("1", "test name", "test surname");
		GuiActionRunner.execute(() -> view.newTennisPlayerAdded(player));
		assertThat(window.list("playersList").contents()).containsExactly(player.toString());
		window.label("errorPlayerLbl").requireText(" ");
	}

	@Test
	@GUITest
	public void testPlayerSuccessfullyRemovedShouldRemoveThePlayerFromTheListAndResetTheErrorLabel() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<TennisPlayer> listPlayersModel = view.getListPlayerModel();
			listPlayersModel.addElement(player1);
			listPlayersModel.addElement(player2);
		});
		GuiActionRunner.execute(() -> view.tennisPlayerRemoved(player1));
		assertThat(window.list("playersList").contents()).containsExactly(player2.toString());
		window.label("errorPlayerLbl").requireText(" ");
	}

	@Test
	@GUITest
	public void testAddButtonShouldInvokePlayerControllerForAddingNewPlayer() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test name");
		window.textBox("surnameTextBox").enterText("test surname");
		window.button(JButtonMatcher.withText("Add player")).click();
		verify(playerController).addNewTennisPlayer(new TennisPlayer("1", "test name", "test surname"));
	}

	@Test
	@GUITest
	public void testDeleteButtonShouldInvokePlayerControllerForDeletingThePlayer() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<TennisPlayer> listPlayersModel = view.getListPlayerModel();
			listPlayersModel.addElement(player1);
			listPlayersModel.addElement(player2);
		});
		window.list("playersList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete player")).click();
		verify(playerController).deleteTennisPlayer(player2);
	}

	@Test
	@GUITest
	public void testPlayerSeccessfullyAddedShouldResetAlsoTheTextBoxes() {
		TennisPlayer player = new TennisPlayer("1", "test name", "test surname");
		window.textBox("idTextBox").enterText(player.getId());
		window.textBox("nameTextBox").enterText(player.getName());
		window.textBox("surnameTextBox").enterText(player.getSurname());
		GuiActionRunner.execute(() -> view.newTennisPlayerAdded(player));
		window.textBox("idTextBox").requireText("");
		window.textBox("nameTextBox").requireText("");
		window.textBox("surnameTextBox").requireText("");
	}

	@Test
	@GUITest
	public void testWinnerAndLoserComboBoxAreFilledWithTheSameElementsAsThePlayersList() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		GuiActionRunner.execute(() -> view.newTennisPlayerAdded(player1));
		assertThat(window.comboBox("winnerComboBox").contents()).containsExactly(player1.toString());
		assertThat(window.comboBox("loserComboBox").contents()).containsExactly(player1.toString());
	}

	@Test
	@GUITest
	public void testWinnerAndLoserComboBoxAreUpdatedWithExistingPlayerElementWhenshowAllTennisPlayers() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		GuiActionRunner.execute(() -> view.showAllTennisPlayers(Arrays.asList(player1)));
		assertThat(window.comboBox("winnerComboBox").contents()).containsExactly(player1.toString());
		assertThat(window.comboBox("loserComboBox").contents()).containsExactly(player1.toString());
	}

	@Test
	@GUITest
	public void testWinnerAndLoserComboBoxesShouldBeEnabledWhenThereAreAtLeastTwoElementsInTheOptions() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<TennisPlayer> winnerComboBoxModel = view.getWinnerComboBoxModel();
			winnerComboBoxModel.addElement(player1);
			winnerComboBoxModel.addElement(player2);
			DefaultComboBoxModel<TennisPlayer> loserComboBoxModel = view.getLoserComboBoxModel();
			loserComboBoxModel.addElement(player1);
			loserComboBoxModel.addElement(player2);
		});
		window.comboBox("winnerComboBox").requireEnabled();
		window.comboBox("loserComboBox").requireEnabled();
	}

	@Test
	@GUITest
	public void testWinnerAndLoserComboBoxShouldBeDisabledWhenTheListOfPlayersAvaliableIsReducedToLessThanTwo() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<TennisPlayer> winnerComboBoxModel = view.getWinnerComboBoxModel();
			winnerComboBoxModel.addElement(player1);
			winnerComboBoxModel.addElement(player2);
			DefaultComboBoxModel<TennisPlayer> loserComboBoxModel = view.getLoserComboBoxModel();
			loserComboBoxModel.addElement(player1);
			loserComboBoxModel.addElement(player2);
		});
		window.comboBox("winnerComboBox").requireEnabled();
		window.comboBox("loserComboBox").requireEnabled();

		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<TennisPlayer> winnerComboBoxModel = view.getWinnerComboBoxModel();
			winnerComboBoxModel.removeElement(player1);
			DefaultComboBoxModel<TennisPlayer> loserComboBoxModel = view.getLoserComboBoxModel();
			loserComboBoxModel.removeElement(player1);
		});
		window.comboBox("winnerComboBox").requireDisabled();
		window.comboBox("loserComboBox").requireDisabled();

	}
	
	@Test
	@GUITest
	public void testAddButtonShouldBeEnabledWhenWinnerAndLoserAreSelectedAndDateIsNotEmpty() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<TennisPlayer> winnerComboBoxModel = view.getWinnerComboBoxModel();
			winnerComboBoxModel.addElement(player1);
			winnerComboBoxModel.addElement(player2);
			DefaultComboBoxModel<TennisPlayer> loserComboBoxModel = view.getLoserComboBoxModel();
			loserComboBoxModel.addElement(player1);
			loserComboBoxModel.addElement(player2);
		});
		window.comboBox("winnerComboBox").requireEnabled();
		window.comboBox("loserComboBox").requireEnabled();

		window.comboBox("winnerComboBox").selectItem(0);
		window.comboBox("loserComboBox").selectItem(1);
		window.textBox("dateOfTheMatchTextBox").enterText("21-10-2025");
		window.button(JButtonMatcher.withText("Add match")).requireEnabled();
	}
	
	@Test
	@GUITest
	public void testWhenDateIsBlankThenAddButtonShouldNotBeEnabled() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<TennisPlayer> winnerComboBoxModel = view.getWinnerComboBoxModel();
			winnerComboBoxModel.addElement(player1);
			winnerComboBoxModel.addElement(player2);
			DefaultComboBoxModel<TennisPlayer> loserComboBoxModel = view.getLoserComboBoxModel();
			loserComboBoxModel.addElement(player1);
			loserComboBoxModel.addElement(player2);
		});
		window.comboBox("winnerComboBox").requireEnabled();
		window.comboBox("loserComboBox").requireEnabled();

		window.comboBox("winnerComboBox").selectItem(0);
		window.comboBox("loserComboBox").selectItem(1);
		window.textBox("dateOfTheMatchTextBox").enterText(" ");
		window.button(JButtonMatcher.withText("Add match")).requireDisabled();
	}
	
	@Test
	@GUITest
	public void testThatIfSamePlayerIsSelectedAsWinnerAndLoserThanTheAddButtonShouldRemainDisabled() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<TennisPlayer> winnerComboBoxModel = view.getWinnerComboBoxModel();
			winnerComboBoxModel.addElement(player1);
			winnerComboBoxModel.addElement(player2);
			DefaultComboBoxModel<TennisPlayer> loserComboBoxModel = view.getLoserComboBoxModel();
			loserComboBoxModel.addElement(player1);
			loserComboBoxModel.addElement(player2);
		});
		window.comboBox("winnerComboBox").requireEnabled();
		window.comboBox("loserComboBox").requireEnabled();

		window.comboBox("winnerComboBox").selectItem(0);
		window.comboBox("loserComboBox").selectItem(0);
		window.textBox("dateOfTheMatchTextBox").enterText("10-12-2025");
		window.button(JButtonMatcher.withText("Add match")).requireDisabled();
	}
	
	@Test
	@GUITest
	public void testSelectionOfMatchShouldEnableDeleteMatchButton() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		LocalDate date = LocalDate.of(2025, 10, 25);
		GuiActionRunner
				.execute(() -> view.getListMatchModel().addElement(new TennisMatch(player1, player2, date)));

		window.list("matchesList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete match")).requireEnabled();
		window.list("matchesList").clearSelection();
		window.button(JButtonMatcher.withText("Delete match")).requireDisabled();
	}

	@Test
	@GUITest
	public void testMatchesAreAddedToTheListWhenshowAllTennisMatches() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		LocalDate date1 = LocalDate.of(2025, 10, 25);
		LocalDate date2 = LocalDate.of(2025, 10, 28);
		TennisMatch match1 = new TennisMatch(player1, player2, date1);
		TennisMatch match2 = new TennisMatch(player2, player1, date2);
		GuiActionRunner.execute(() -> view.showAllTennisMatches(Arrays.asList(match1, match2)));
		assertThat(window.list("matchesList").contents()).containsExactly(match1.toString(), match2.toString());
	}
	
	@Test
	@GUITest
	public void testShowErrorMatchAlreadyPresentShouldShowTheErrorMessageInTheErrorMatchLabel() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		LocalDate date = LocalDate.of(2025, 10, 25);
		TennisMatch match = new TennisMatch(player1, player2, date);
		GuiActionRunner.execute(() -> view.showErrorTennisMatchAlreadyExist("Error message", match));
		window.label("errorMatchLbl").requireText("Error message: (1,test name1,test surname1) - (2,test name2,test surname2) - 2025-10-25");
	}
	
	@Test
	@GUITest
	public void testShowErrorTennisMatchNotExisting() {
		TennisPlayer player1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer player2 = new TennisPlayer("2", "test name2", "test surname2");
		LocalDate date1 = LocalDate.of(2025, 10, 25);
		LocalDate date2 = LocalDate.of(2025, 10, 28);
		TennisMatch match1 = new TennisMatch(player1, player2, date1);
		TennisMatch match2 = new TennisMatch(player2, player1, date2);
		GuiActionRunner.execute(() -> {
			DefaultListModel<TennisMatch> listMatchModel = view.getListMatchModel();
			listMatchModel.addElement(match1);
			listMatchModel.addElement(match2);
		});
		GuiActionRunner.execute(() -> view.showErrorNotExistingTennisMatch("Error message", match1));
		window.label("errorMatchLbl").requireText("Error message: (1,test name1,test surname1) - (2,test name2,test surname2) - 2025-10-25");
		assertThat(window.list("matchesList").contents()).containsExactly(match2.toString());
	}
}
