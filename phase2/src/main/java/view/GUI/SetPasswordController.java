package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;


public class SetPasswordController extends GUIHomeController {

	@FXML
	PasswordField newPassword;
	@FXML
	PasswordField confirmNewPassword;


	public void changePasswordOnClick(ActionEvent actionEvent) {

		if (newPassword == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a new password");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if (confirmNewPassword == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please confirm your new password");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if (!confirmNewPassword.getText().equals(newPassword.getText())) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The new password and confirmation password do not match.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		getCurrentUser().setPassword(newPassword.getText());
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Your password has been changed.");
		alert.setHeaderText("Process succeeded");
		alert.show();
	}

	@FXML
	public void show() {
	}
}
