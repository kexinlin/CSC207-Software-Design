package view.GUI;

import controller.ATM;
import controller.BankSystem;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.UI;

import java.io.IOException;


public class GraphicsUI extends Application implements UI {

	private BankSystem sys;
	private ATM atm;

	public GraphicsUI(ATM atm) {
		this.atm = atm;
		this.sys = atm.getBankSystem();
	}

	static BorderPane root;

	@Override
	public void start(Stage primaryStage) throws IOException {
		GUIManager guiManager = new GUIManager(atm, sys);

		primaryStage.setTitle("My Smart ATM");

		root = new BorderPane();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));

		Scene rootScene = new Scene(root, 800, 500);
		root.setCenter(loader.load());

		LoginController loginController = loader.getController();
		loginController.setGUIManager(guiManager);

		primaryStage.setScene(rootScene);
		primaryStage.showAndWait();

	}

	@Override
	public void mainLoop() {
		try {
			this.start(new Stage());
		} catch (IOException e) {
			System.err.println("Starting error");
		}
	}

}
