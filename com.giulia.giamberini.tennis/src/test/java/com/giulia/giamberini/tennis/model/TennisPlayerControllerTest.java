package com.giulia.giamberini.tennis.model;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.giulia.giamberini.tennis.controller.TennisPlayerController;
import com.giulia.giamberini.tennis.repository.TennisPlayerRepository;
import com.giulia.giamberini.tennis.view.TennisManagementView;

public class TennisPlayerControllerTest {

	@Mock
	private TennisPlayerRepository playersRepo;
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
		TennisPlayer tennisPlayerToAdd = new TennisPlayer("1","test","Test");
		playersController.addNewTennisPlayer(tennisPlayerToAdd);
		InOrder order = inOrder(playersRepo, view);
		order.verify(playersRepo).save(tennisPlayerToAdd);
		order.verify(view).newTennisPlayerAdded(tennisPlayerToAdd);
	}

}
