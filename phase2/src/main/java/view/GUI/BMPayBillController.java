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

import javax.xml.soap.Text;
import java.util.ArrayList;

public class BMPayBillController extends GUIHomeController {

	@FXML
	TextField payAmount;
	@FXML
	TextField paySrcAcc;
	@FXML
	TextField payeeName;


	public void payConfirmOnClick(ActionEvent actionEvent) {
		if (payAmount.getText().equals("") || paySrcAcc.getText().equals("") || payeeName.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please fill in all required entry fields.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		double amount;
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

		Account srcAccount = null;

		// get source account of payment
		try {
			srcAccount = guiManager.getBankSystem().getAccountById(paySrcAcc.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Account does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
		}


		try {
			guiManager.getBankSystem().payBill(srcAccount, payeeName.getText(), amount);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. You can now check the new account balance");
			alert.setHeaderText("Process succeeded");
			alert.show();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during transaction.");
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
