package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.exceptions.AccountNotExistException;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.User;
import model.transactions.Transaction;
import model.transactors.Account;

import java.util.ArrayList;

public class TransferController extends GUIHomeController {

	@FXML
	TextField transferAmount;
	@FXML
	ChoiceBox<String> transferSourceChoiceBox;
	@FXML
	TextField transferDesAccount;


	public void transferConfirmOnClick(ActionEvent actionEvent) {
		if (transferSourceChoiceBox.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please choose an account");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if (transferDesAccount.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter an account #");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		double amount = 0;
		Account desAccount = null;
		Account srcAccount = null;
		String[] choiceBoxStrArray = transferSourceChoiceBox.getValue().split("\\s");

		// get source account of transaction
		try {
			srcAccount = guiManager.getBankSystem().getAccountById(choiceBoxStrArray[choiceBoxStrArray.length - 1]);
		} catch (AccountNotExistException e) {
			e.printStackTrace();
		}

		// get destination account of transaction
		try {
			desAccount = guiManager.getBankSystem().getAccountById(transferDesAccount.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a valid account number");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

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

		try {
			Transaction trans = guiManager.getBankSystem().transferMoney(srcAccount, desAccount, amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. You can now check your new account balance. Please note that $"
				+ trans.getFee() + " of service fee is deducted.");
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
			alert.setContentText("Your account does not have enough money.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
	}

	@FXML
	public void show() {
		ArrayList<Account> accList = ((User) currentUser).getAccounts();
		for (Account acc : accList) {
			transferSourceChoiceBox.getItems().add(acc.getAccountType() + ", ID: " + acc.getAccountId());
		}
	}


}
