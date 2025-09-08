package com.giulia.giamberini.tennis.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.giulia.giamberini.tennis.model.TennisMatch;
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

}
