package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;


public class ChooseRoleController extends GUIHomeController {

	public void chooseUserOnClick(ActionEvent actionEvent) {
		loadNextScene("/UserHomeScene.fxml");
	}

	public void chooseEmployeeOnClick(ActionEvent actionEvent) {
		loadNextScene("/BankManagerHomeScene.fxml");
	}

	public void loadNextScene(String resourceName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
		try {
			AnchorPane homeScene = loader.load();

			GUIHomeController homeController = loader.getController();
			homeController.setGUIManager(guiManager);
			homeController.setCurrentUser(getCurrentUser());

			homeController.show();

			GraphicsUI.root.getChildren().setAll(homeScene);
		} catch (IOException e) {
			showAlert(Alert.AlertType.ERROR,
				"Cannot load your home page",
				"System error");
		}
	}

	public void show() {
	}
}
