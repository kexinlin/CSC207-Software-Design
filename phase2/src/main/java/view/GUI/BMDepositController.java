package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.persons.User;
import model.transactions.Transaction;
import model.transactors.Account;

import java.util.ArrayList;

public class BMDepositController extends GUIHomeController {

	@FXML
	TextField desAccount;


	public void depositConfirmOnClick(ActionEvent actionEvent) {
		if (desAccount.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter an account number.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		Account acc = null;
		try {
			acc = guiManager.getBankSystem().getAccountById(desAccount.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Account does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
		}

		try {
			Transaction trans = guiManager.getATM().depositMoney(acc);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. You can now check the account new balance. Please note that $"
				+ trans.getFee() + " of service fee is deducted.");
			alert.setHeaderText("Process succeeded");
			alert.show();
			getStage().close();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during deposit.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
	}

	@FXML
	public void show() {
	}
}
