package view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainPage extends Application{
	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("My Online Banking");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));

		Scene loginScene = new Scene(loader.load(), 1000, 600);

		primaryStage.setScene(loginScene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
