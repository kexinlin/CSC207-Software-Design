package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.persons.Loginable;

import java.io.IOException;

public abstract class GUIHomeController extends GUIController{

	Loginable currentUser;

	private Stage stage;
	public void setStage(Stage stage) { this.stage = stage; }
	public Stage getStage() { return this.stage; }

	@FXML
	public void setCurrentUser(Loginable loginable){
		this.currentUser = loginable;
	}

	public Loginable getCurrentUser() {
		return currentUser;
	}

	@FXML
	public abstract void show();

	@FXML
	public void loadWindow(String location, String title) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
			Parent parent = loader.load();

			GUIHomeController homeController = loader.getController();
			homeController.setGUIManager(guiManager);
			homeController.setCurrentUser(getCurrentUser());
			homeController.show();

			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			homeController.setStage(stage);

			stage.setScene(new Scene(parent));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadWindow(String location, String title, GUIHomeController controller) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
			loader.setController(controller);

			Parent parent = loader.load();

			controller.setGUIManager(guiManager);
			controller.setCurrentUser(getCurrentUser());
			controller.show();

			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			controller.setStage(stage);

			stage.setScene(new Scene(parent));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void logOutButtonOnClick(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));
		AnchorPane homeScene = loader.load();

		GUIController controller = loader.getController();
		controller.setGUIManager(guiManager);

		GraphicsUI.root.getChildren().setAll(homeScene);
	}

}
