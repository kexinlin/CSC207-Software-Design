package view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.persons.AccountOwner;
import model.persons.Employee;
import model.persons.Loginable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController extends GUIController {

	@FXML
	TextField usernameInput;
	@FXML
	PasswordField passwordInput;
	@FXML
	Button loginButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		usernameInput.setOnKeyReleased(this::slotUsernameInputKeyReleased);
		passwordInput.setOnKeyReleased(this::slotPasswordInputKeyReleased);
		loginButton.setOnMouseClicked(this::slotLoginButtonMouseClicked);
	}

	private void slotUsernameInputKeyReleased(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			passwordInput.requestFocus();
		}
	}

	private void slotPasswordInputKeyReleased(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			loginUser();
		}
	}

	private void slotLoginButtonMouseClicked(MouseEvent ignore) {
		loginUser();
	}

	private void loginUser() {
		String username = usernameInput.getText();
		String password = passwordInput.getText();
		System.out.println(username);
		System.out.println(password);

		if (username.equals("") || password.equals("")) {
			showAlert(Alert.AlertType.ERROR,
			"Please enter your username or password",
			"Login failed");
		} else if (guiManager.getATM().login(username, password)) {
			String resourceName;
			Loginable loginable = guiManager.getBankSystem().getLoginable(username);
			if (loginable instanceof AccountOwner && loginable instanceof Employee) {
				resourceName = "/ChooseRoleScene.fxml";
			} else if (loginable instanceof AccountOwner) {
				resourceName = "/UserHomeScene.fxml";
			} else {
				resourceName = "/BankManagerHomeScene.fxml";
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
			try {
				AnchorPane homeScene = loader.load();

				GUIHomeController homeController = loader.getController();
				homeController.setGUIManager(guiManager);
				homeController.setCurrentUser(guiManager.bankSystem.getLoginable(username));

				homeController.show();

				GraphicsUI.root.getChildren().setAll(homeScene);
			} catch (IOException e) {
				showAlert(Alert.AlertType.ERROR,
					"Cannot load your home page",
					"System error");
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Incorrect password. Please check your password again.");
			alert.setHeaderText("Login failed");
			alert.show();
		}
	}
}
