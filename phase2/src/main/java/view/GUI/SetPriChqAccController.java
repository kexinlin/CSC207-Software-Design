package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.persons.AccountOwner;
import model.persons.User;
import model.transactors.Account;
import model.transactors.ChequingAccount;

import java.util.ArrayList;

public class SetPriChqAccController extends GUIHomeController {

	@FXML
	ChoiceBox<String> priChqAccChoiceBox;


	public void setPriChqAccOnClick(ActionEvent actionEvent) {
		if (priChqAccChoiceBox.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please choose an account");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		String[] choiceBoxStrArray = priChqAccChoiceBox.getValue().split("\\s");
		Account acc = null;
		try {
			acc = guiManager.getBankSystem().getAccountById(choiceBoxStrArray[choiceBoxStrArray.length - 1]);
		} catch (AccountNotExistException e) {
			e.printStackTrace();
		}

		((User) getCurrentUser()).setPrimaryCheuqingAccount((ChequingAccount) acc);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Your new primary chequing has been updated.");
		alert.setHeaderText("Process succeeded");
		alert.show();
		getStage().close();
	}

	@FXML
	public void show() {
		ArrayList<Account> accList = ((User) currentUser).getAccounts();
		for (Account acc : accList) {
			if(acc.getClass() == ChequingAccount.class)
			priChqAccChoiceBox.getItems().add(acc.getAccountType() + ", ID: " + acc.getAccountId());
		}
	}
}
