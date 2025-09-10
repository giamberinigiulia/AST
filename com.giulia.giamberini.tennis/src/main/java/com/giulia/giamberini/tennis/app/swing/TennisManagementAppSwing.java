package com.giulia.giamberini.tennis.app.swing;

import java.awt.EventQueue;

import com.giulia.giamberini.tennis.controller.TennisMatchController;
import com.giulia.giamberini.tennis.controller.TennisPlayerController;
import com.giulia.giamberini.tennis.repository.mongo.TennisMatchRepositoryMongo;
import com.giulia.giamberini.tennis.repository.mongo.TennisPlayerRepositoryMongo;
import com.giulia.giamberini.tennis.view.swing.TennisManagementViewSwing;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class TennisManagementAppSwing {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// default params
				String hostMongoDabatase = "localhost";
				int defaultPortMongoDatabase = 27017;
				if (args.length > 0) { // there is a param for host
					hostMongoDabatase = args[0];
				}
				if (args.length > 1) { // there is also a param for port
					defaultPortMongoDatabase = Integer.parseInt(args[1]);
				}
				MongoClient client = new MongoClient(new ServerAddress(hostMongoDabatase, defaultPortMongoDatabase));
				TennisPlayerRepositoryMongo playerRepositoryMongo = new TennisPlayerRepositoryMongo(client,
						"tennis_matches", "players");
				TennisMatchRepositoryMongo matchRepositoryMongo = new TennisMatchRepositoryMongo(client,
						"tennis_matches", "matches");
				TennisManagementViewSwing tennisManagementView = new TennisManagementViewSwing();
				TennisPlayerController playerController = new TennisPlayerController(playerRepositoryMongo,
						matchRepositoryMongo, tennisManagementView);
				TennisMatchController matchController = new TennisMatchController(matchRepositoryMongo,
						tennisManagementView);
				tennisManagementView.setPlayerController(playerController);
				tennisManagementView.setMatchController(matchController);
				tennisManagementView.setVisible(true);
				playerController.findAllTennisPlayers();
				matchController.findAllTennisMatches();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
