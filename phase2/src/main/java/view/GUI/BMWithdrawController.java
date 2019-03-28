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
import model.transactors.Account;

import java.util.ArrayList;

public class BMWithdrawController extends GUIHomeController {

	@FXML
	TextField withdrawAmount;
	@FXML
	TextField srcAccount;


	public void withdrawConfirmOnClick(ActionEvent actionEvent) {
		if(withdrawAmount.getText().equals("")||srcAccount.getText().equals("")){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please fill in all required entry fields.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		double amount;
		try {
			amount = Double.valueOf(withdrawAmount.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a valid amount");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		Account acc;
		try {
			acc = guiManager.getBankSystem().getAccountById(srcAccount.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Account does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		try {
			guiManager.getATM().withdrawMoney(acc, amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. Please take the money.");
			alert.setHeaderText("Process succeeded");
			alert.show();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during withdrawal.");
			alert.setHeaderText("Process failed");
			alert.show();
		} catch (NoEnoughMoneyException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Source account does not have enough money.");
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
	}


}
