package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.User;
import model.transactors.Account;

import java.util.ArrayList;

public class BMTransferController extends GUIHomeController {

	@FXML
	TextField transferAmount;
	@FXML
	TextField transSrcAccount;
	@FXML
	TextField transDesAccount;


	public void transferConfirmOnClick(ActionEvent actionEvent) {
		if (transferAmount.getText().equals("") || transSrcAccount.getText().equals("") || transDesAccount.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please fill in all required entry fields.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		double amount;
		// get amount of transaction
		try {
			amount = Double.valueOf(transferAmount.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a valid amount");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		Account srcAccount;
		Account desAccount;

		// get source account of transaction
		try {
			srcAccount = guiManager.getBankSystem().getAccountById(transSrcAccount.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Source account does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		// get destination account of transaction
		try {
			desAccount = guiManager.getBankSystem().getAccountById(transDesAccount.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Destination account does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		try {
			guiManager.getBankSystem().transferMoney(srcAccount, desAccount, amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. You can now check the new account balance");
			alert.setHeaderText("Process succeeded");
			alert.show();
			getStage().close();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during transaction. Please note " +
				"that transferring out from a credit card account is not allowed.");
			alert.setHeaderText("Process failed");
			alert.show();
		} catch (NoEnoughMoneyException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Source account does not have enough money.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
	}

	@FXML
	public void show() {

	}


}
