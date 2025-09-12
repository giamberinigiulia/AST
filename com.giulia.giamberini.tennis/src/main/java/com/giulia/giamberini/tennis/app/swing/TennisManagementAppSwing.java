package com.giulia.giamberini.tennis.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.giulia.giamberini.tennis.controller.TennisMatchController;
import com.giulia.giamberini.tennis.controller.TennisPlayerController;
import com.giulia.giamberini.tennis.repository.mongo.TennisMatchRepositoryMongo;
import com.giulia.giamberini.tennis.repository.mongo.TennisPlayerRepositoryMongo;
import com.giulia.giamberini.tennis.view.swing.TennisManagementViewSwing;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class TennisManagementAppSwing implements Callable<Void> {

	@Option(names = { "--mongoHost", "--host" }, description = "Host address of the Mongo Database")
	private static String hostMongoDabatase = "localhost";
	@Option(names = { "--mongoPort", "--port" }, description = "Host port of the Mongo Database")
	private static int defaultPortMongoDatabase = 27017;
	@Option(names = { "--databaseName", "--dbName" }, description = "Database name")
	private static String databaseName = "tennis_matches";
	@Option(names = { "--playersColletionName", "--playersCollection" }, description = "Players collection name")
	private static String playersCollectionName = "players";
	@Option(names = { "--matchesColletionName", "--matchesCollection" }, description = "Matches collection name")
	private static String matchesCollectionName = "matches";

	public static void main(String[] args) {
		new CommandLine(new TennisManagementAppSwing()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient client = new MongoClient(new ServerAddress(hostMongoDabatase, defaultPortMongoDatabase));
				TennisPlayerRepositoryMongo playerRepositoryMongo = new TennisPlayerRepositoryMongo(client,
						databaseName, playersCollectionName);
				TennisMatchRepositoryMongo matchRepositoryMongo = new TennisMatchRepositoryMongo(client, databaseName,
						matchesCollectionName);
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
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}
}