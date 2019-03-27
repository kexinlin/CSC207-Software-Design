package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Request;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;
import model.persons.User;
import model.transactors.Account;

import java.util.ArrayList;

public class NewAccountCreationController extends GUIHomeController {

	@FXML
	TextField accountOwnerUsername;
	@FXML
	ChoiceBox<String> accTypeChoiceBox;


	public void AccCreationOnClick(ActionEvent actionEvent) {
		if (accountOwnerUsername.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a username.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if (accTypeChoiceBox.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please choose an account type.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		AccountOwner owner = (AccountOwner) guiManager.getBankSystem().getLoginable(accountOwnerUsername.getText());

		if (owner == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Username does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		String accType = accTypeChoiceBox.getValue();
		Request request = new Request(owner, accType, "");
		guiManager.getBankSystem().createAccount(request);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("New account has been created.");
		alert.setHeaderText("Process succeeded");
		alert.show();
	}

	@FXML
	public void show() {
		ArrayList<String> accTypeList = guiManager.getBankSystem().getAllAccountType();
		for (String accType : accTypeList) {
			accTypeChoiceBox.getItems().add(accType);
		}
	}


}
