package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LoginController {

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


		if(username.equals("")||password.equals("")){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter your username or password");
			alert.setHeaderText("LoginController fails");
			alert.show();
		}

		else if (!username.equals(password)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Wrong password");
			alert.setHeaderText("LoginController fail");
			alert.show();
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserHomeScene.fxml"));
			AnchorPane ap = loader.load();
			Main.root.getChildren().setAll(ap);

		}
	}
}
