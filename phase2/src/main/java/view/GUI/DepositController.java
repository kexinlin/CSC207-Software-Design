package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.persons.User;
import model.transactors.Account;

import java.util.ArrayList;

public class DepositController extends GUIHomeController {

	@FXML
	ChoiceBox<String> depositDesChoiceBox;


	public void depositConfirmOnClick(ActionEvent actionEvent) {
		if (depositDesChoiceBox.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please choose an account");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		String[] choiceBoxStrArray = depositDesChoiceBox.getValue().split("\\s");
		Account acc = null;
		try {
			acc = guiManager.getBankSystem().getAccountById(choiceBoxStrArray[choiceBoxStrArray.length - 1]);
		} catch (AccountNotExistException e) {
			e.printStackTrace();
		}

		try {
			guiManager.getATM().depositMoney(acc);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Succeeded. You can now check the new balance.");
			alert.setHeaderText("Process succeeded");
			alert.show();
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("An error occurred during deposit.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
	}

	@FXML
	public void show() {
		ArrayList<Account> accList = ((User) currentUser).getAccounts();
		for (Account acc : accList) {
			depositDesChoiceBox.getItems().add(acc.getAccountType() + ", ID: " + acc.getAccountId());
		}
	}
}
