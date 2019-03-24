package view.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.exceptions.AccountNotExistException;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.User;
import model.transactors.Account;

import java.util.ArrayList;

public class WithdrawController extends GUIHomeController {

	@FXML
	TextField withdrawAmount;
	@FXML
	ChoiceBox<String> withdrawSourceChoiceBox;


	public void withdrawConfirmOnClick(ActionEvent actionEvent) {
		if(withdrawSourceChoiceBox.getValue() == null){
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

		System.out.println(acc.getBalance());

		try {
			guiManager.getATM().withdrawMoney(acc, amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. Please take your money.");
			alert.setHeaderText("Process succeeded");
			alert.show();
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
			alert.setContentText("Sorry, this machine does not have enough cash.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
		System.out.println(acc.getBalance());
	}

	@FXML
	public void show() {
		System.out.println(currentUser.getUsername());
		ArrayList<Account> accList = ((User) currentUser).getAccounts();
		for (Account acc : accList) {
			withdrawSourceChoiceBox.getItems().add(acc.getAccountType() + ", ID: " + acc.getAccountId());
		}
	}

}
