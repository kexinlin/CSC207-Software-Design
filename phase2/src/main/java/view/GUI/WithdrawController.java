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

public class WithdrawController extends GUIHomeController {

	@FXML
	TextField withdrawAmount;
	@FXML
	ChoiceBox<String> withdrawSourceChoiceBox;


	public void withdrawConfirmOnClick(ActionEvent actionEvent) {
		if (withdrawSourceChoiceBox.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please choose an account");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		double amount = 0;
		String[] choiceBoxStrArray = withdrawSourceChoiceBox.getValue().split("\\s");
		Account acc = null;
		try {
			acc = guiManager.getBankSystem().getAccountById(choiceBoxStrArray[choiceBoxStrArray.length - 1]);
		} catch (AccountNotExistException e) {
			e.printStackTrace();
		}

		try {
			amount = Double.valueOf(withdrawAmount.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a valid amount");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		try {
			Transaction trans = guiManager.getATM().withdrawMoney(acc, amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. Please take your money. Please note that $" + trans.getFee() +
				" of service fee is deducted.");
			alert.setHeaderText("Process succeeded");
			alert.show();
			getStage().close();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during withdrawal.");
			alert.setHeaderText("Process failed");
			alert.show();
		} catch (NoEnoughMoneyException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Your account does not have enough money.");
			alert.setHeaderText("Process failed");
			alert.show();
		} catch (InsufficientCashException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("This ATM does machine not have this amount of cash.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
	}

	@FXML
	public void show() {
		ArrayList<Account> accList = ((User) currentUser).getAccounts();
		for (Account acc : accList) {
			withdrawSourceChoiceBox.getItems().add(acc.getAccountType() + ", ID: " + acc.getAccountId());
		}
	}


}
