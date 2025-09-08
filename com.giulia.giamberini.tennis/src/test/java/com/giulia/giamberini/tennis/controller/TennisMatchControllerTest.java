package com.giulia.giamberini.tennis.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisMatchControllerTest {

	@Mock
	private TennisMatchRepository matchesRepo;
	@Mock
	private TennisManagementView view;
	@InjectMocks
	private TennisMatchController matchesController;

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
	public void testAllTennisMatches() {
		List<TennisMatch> matchesInRepo = Arrays.asList(new TennisMatch());
		when(matchesRepo.findAll()).thenReturn(matchesInRepo);
		matchesController.findAllTennisMatches();
		verify(view).showAllTennisMatches(matchesInRepo);
	}

	@Test
	public void testInsertNewtennisMatchWhenItDoesntExist() {
		TennisPlayer winner = new TennisPlayer("1", "winner name", "loser name");
		TennisPlayer loser = new TennisPlayer("2", "loser name", "loser surname");
		LocalDate date = LocalDate.of(2025, 10, 10);
		TennisMatch tennisMatchToAdd = new TennisMatch("1", winner, loser, date);
		when(matchesRepo.findByMatchInfo("1", winner, loser, date)).thenReturn(null);
		matchesController.addNewTennisMatch(tennisMatchToAdd);
		InOrder order = inOrder(matchesRepo, view);
		order.verify(matchesRepo).save(tennisMatchToAdd);
		order.verify(view).newTennisMatchAdded(tennisMatchToAdd);
	}

	@Test
	public void testInsertNewTennisMatchWhenItAlreadyExist() {
		TennisPlayer winner = new TennisPlayer("1", "winner name", "loser name");
		TennisPlayer loser = new TennisPlayer("2", "loser name", "loser surname");
		LocalDate date = LocalDate.of(2025, 10, 10);
		TennisMatch existingMatch = new TennisMatch("1", winner, loser, date);
		when(matchesRepo.findByMatchInfo("1", winner, loser, date)).thenReturn(existingMatch);
		matchesController.addNewTennisMatch(existingMatch);
		verify(view).showErrorTennisMatchAlreadyExist("The match between " + winner.toString() + " and "
				+ loser.toString() + " has been already played in the selected date " + date, existingMatch);
		verifyNoInteractions(ignoreStubs(matchesRepo));
	}

}
