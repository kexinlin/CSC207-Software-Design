package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;
import model.persons.User;
import model.transactors.Account;

import java.util.ArrayList;

public class NewUserCreationController extends GUIHomeController {

	@FXML
	TextField name;
	@FXML
	TextField username;
	@FXML
	TextField initPassword;


	public void newUserConfirmOnClick(ActionEvent actionEvent) {
		if (name.getText().equals("") || username.getText().equals("") || initPassword.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please fill in all required entry fields.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if (guiManager.getBankSystem().getLoginable(username.getText()) != null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The username is occupied by another individual.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		AccountOwner user = new User(name.getText(), username.getText(), initPassword.getText());
		guiManager.getBankSystem().addLoginable(user);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("This new user has been added to bank system.");
		alert.setHeaderText("Process succeeded");
		alert.show();

	}

	@FXML
	public void show() {
	}


}
