package com.giulia.giamberini.tennis.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.giulia.giamberini.tennis.model.TennisMatch;
import com.giulia.giamberini.tennis.model.TennisPlayer;
import com.giulia.giamberini.tennis.repository.TennisMatchRepository;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisPlayerControllerTest {

	@Mock
	private TennisPlayerRepository playersRepo;
	@Mock
	private TennisMatchRepository matchesRepo;
	@Mock
	private TennisManagementView view;
	@InjectMocks
	private TennisPlayerController playersController;

	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllTennisPlayers() {
		List<TennisPlayer> playersInRepo = Arrays.asList(new TennisPlayer());
		when(playersRepo.findAll()).thenReturn(playersInRepo);
		playersController.findAllTennisPlayers();
		verify(view).showAllTennisPlayers(playersInRepo);
	}

	@Test
	public void testInsertNewPlayerIdWhenItDoesntExist() {
		when(playersRepo.findById("1")).thenReturn(null);
		TennisPlayer tennisPlayerToAdd = new TennisPlayer("1", "test", "Test");
		playersController.addNewTennisPlayer(tennisPlayerToAdd);
		InOrder order = inOrder(playersRepo, view);
		order.verify(playersRepo).save(tennisPlayerToAdd);
		order.verify(view).newTennisPlayerAdded(tennisPlayerToAdd);
	}

	@Test
	public void testInsertNewPlayerWhenItAlreadyExistShouldDisplayAnErrorInTheView() {
		TennisPlayer existingTennisPlayer = new TennisPlayer("1", "existing name", "existing surname");
		TennisPlayer newTennisPlayer = new TennisPlayer("1", "another name", "another surname");
		when(playersRepo.findById("1")).thenReturn(existingTennisPlayer);
		playersController.addNewTennisPlayer(newTennisPlayer);
		verify(view).showErrorTennisPlayerAlreadyExist("The selected id 1 is already in use by another player",
				existingTennisPlayer);
		verifyNoMoreInteractions(ignoreStubs(playersRepo));
	}

	@Test
	public void testRemoveTennisPlayerWhenItAlreadyExist() {
		TennisPlayer tennisPlayerToRemove = new TennisPlayer("1", "toRemove name", "toRemove surname");
		when(playersRepo.findById("1")).thenReturn(tennisPlayerToRemove);
		playersController.deleteTennisPlayer(tennisPlayerToRemove);
		InOrder order = inOrder(playersRepo, view);
		order.verify(playersRepo).delete(tennisPlayerToRemove);
		order.verify(view).tennisPlayerRemoved(tennisPlayerToRemove);
	}

	@Test
	public void testRemoveTennisPlayerWhenItDoesntExist() {
		TennisPlayer tennisPlayerToRemove = new TennisPlayer("1", "toRemove name", "toRemove surname");
		when(playersRepo.findById("1")).thenReturn(null);
		playersController.deleteTennisPlayer(tennisPlayerToRemove);
		verify(view).showErrorNotExistingTennisPlayer("The selected id 1 is not associated with any tennis player",
				tennisPlayerToRemove);
		verifyNoMoreInteractions(ignoreStubs(playersRepo));
	}

	@Test
	public void testRemoveExistingPlayerMustRemoveAllRelatedMatches() {
		TennisPlayer tennisPlayer1 = new TennisPlayer("1", "test name1", "test surname1");
		TennisPlayer tennisPlayer2 = new TennisPlayer("2", "test name2", "test surname2");
		TennisPlayer tennisPlayer3 = new TennisPlayer("3", "test name3", "test surname3");
		LocalDate dateMatch1And2 = LocalDate.of(2025, 10, 10);
		LocalDate dateMatch2And3 = LocalDate.of(2025, 10, 11);
		TennisMatch matchToDelete = new TennisMatch(tennisPlayer1, tennisPlayer2, dateMatch1And2);
		TennisMatch remainingMatch = new TennisMatch(tennisPlayer2, tennisPlayer3, dateMatch2And3);
		when(playersRepo.findById("1")).thenReturn(tennisPlayer1);
		when(matchesRepo.findMatchesByTennisPlayerId("1")).thenReturn(Arrays.asList(matchToDelete));
		playersController.deleteTennisPlayer(tennisPlayer1);
		InOrder order = inOrder(matchesRepo, playersRepo, view);
		order.verify(matchesRepo).delete(matchToDelete);
		order.verify(view).tennisMatchRemoved(matchToDelete);
		order.verify(playersRepo).delete(tennisPlayer1);
		order.verify(view).tennisPlayerRemoved(tennisPlayer1);
		verify(matchesRepo, never()).delete(remainingMatch);
	}

}
