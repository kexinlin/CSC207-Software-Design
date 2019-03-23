package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.persons.User;

import java.io.IOException;


public class LoginController extends GUIController{

	@FXML
	TextField usernameInput;
	@FXML
	PasswordField passwordInput;

	@FXML
	public void LoginButtonOnClick(ActionEvent actionEvent) throws IOException {
		String username = usernameInput.getText();
		String password = passwordInput.getText();
		System.out.println(username);
		System.out.println(password);

		if (username.equals("") || password.equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter your username or password");
			alert.setHeaderText("Login failed");
			alert.show();
		} else if (guiManager.getAtm().login(username, password)) {
			String resourceName;
			if (guiManager.getBankSystem().getLoginable(username) instanceof User) {
				resourceName = "/UserHomeScene.fxml";
			} else {
				resourceName = "/EmployeeHomeScene.fxml";
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
			AnchorPane homeScene = loader.load();

			GUIHomeController homeController = loader.getController();
			homeController.setGUIManager(guiManager);
			homeController.setCurrentUser(guiManager.bankSystem.getLoginable(username));

			homeController.show();

			Main.root.getChildren().setAll(homeScene);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Incorrect password. Please check your password again.");
			alert.setHeaderText("Login failed");
			alert.show();
		}
	}
}
