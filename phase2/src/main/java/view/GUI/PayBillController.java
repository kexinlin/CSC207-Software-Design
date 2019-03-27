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

public class PayBillController extends GUIHomeController {

	@FXML
	TextField payAmount;
	@FXML
	ChoiceBox<String> srcAccChoiceBox;
	@FXML
	TextField payeeName;


	public void payConfirmOnClick(ActionEvent actionEvent) {
		if (srcAccChoiceBox.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please choose an account");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if (payeeName.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a payee name");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		double amount = 0;
		Account srcAccount = null;
		String[] choiceBoxStrArray = srcAccChoiceBox.getValue().split("\\s");

		// get source account of payment
		try {
			srcAccount = guiManager.getBankSystem().getAccountById(choiceBoxStrArray[choiceBoxStrArray.length - 1]);
		} catch (AccountNotExistException e) {
			e.printStackTrace();
		}


		// get amount of payment
		try {
			amount = Double.valueOf(payAmount.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a valid amount");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		try {
			guiManager.getBankSystem().payBill(srcAccount, payeeName.getText(), amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. You can now check your new account balance");
			alert.setHeaderText("Process succeeded");
			alert.show();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during transaction.");
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
			srcAccChoiceBox.getItems().add(acc.getAccountType() + ", ID: " + acc.getAccountId());
		}
	}

}
