package view.GUI;

import controller.ATM;
import controller.BankSystem;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application{

	static BorderPane root;


	@Override
	public void start(Stage primaryStage) throws IOException {
		// retrieve all data from files
		String recordFileName = "records.txt";
		String atmRecordFileName = "atm-records.txt";
		BankSystem sys = new BankSystem(recordFileName);
		ATM atm = new ATM(sys, atmRecordFileName);

		GUIManager guiManager = new GUIManager(atm, sys);

		primaryStage.setTitle("My Smart ATM");

		root = new BorderPane();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));

		Scene rootScene = new Scene(root, 800, 500);
		root.setCenter(loader.load());

		LoginController loginController = loader.getController();
		loginController.setGUIManager(guiManager);

		primaryStage.setScene(rootScene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
