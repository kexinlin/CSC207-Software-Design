package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LoginController  {

	@FXML
	TextField usernameInput;
	@FXML
	PasswordField passwordInput;

	private GUIManager guiManager;


	public void setGUIManager(GUIManager guiManager){
		this.guiManager = guiManager;
	}

	@FXML
	public void LoginButtonOnClick(ActionEvent actionEvent) throws IOException {
		String username = usernameInput.getText();
		String password = passwordInput.getText();
		System.out.println(username);
		System.out.println(password);

		if(username.equals("")||password.equals("")){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter your username or password");
			alert.setHeaderText("Login failed");
			alert.show();
		}

		else if (guiManager.getAtm().login(username, password)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserHomeScene.fxml"));
			AnchorPane userHomeScene = loader.load();

			UserHomeController userHomeController = loader.getController();
			userHomeController.setGUIManager(guiManager);
			userHomeController.setCurrentUser(guiManager.bankSystem.getLoginable(username));

			userHomeController.show();

			Main.root.getChildren().setAll(userHomeScene);
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Incorrect password. Please check your password again.");
			alert.setHeaderText("Login failed");
			alert.show();
		}
	}
}
