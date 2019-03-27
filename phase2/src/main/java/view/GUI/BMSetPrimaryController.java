package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.exceptions.AccountNotExistException;
import model.persons.AccountOwner;
import model.persons.User;
import model.transactors.Account;
import model.transactors.ChequingAccount;

import java.util.ArrayList;

public class BMSetPrimaryController extends GUIHomeController {

	@FXML
	TextField chqAccUsername;
	@FXML
	TextField priChqAccNum;


	public void ManagerSetPrimaryOnClick(ActionEvent actionEvent) {

		if (chqAccUsername.getText().equals("")||priChqAccNum.getText().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please fill in all required fields.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		AccountOwner owner = (AccountOwner) guiManager.getBankSystem().getLoginable(chqAccUsername.getText());
		if(owner == null){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Username does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		Account acc;

		try {
			acc = guiManager.getBankSystem().getAccountById(priChqAccNum.getText());
		} catch (AccountNotExistException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Account number does not exist.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if(!(acc instanceof ChequingAccount)){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Account type must be chequing account.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		if(acc.getOwner() != owner){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Owner of input account does not match input username.");
			alert.setHeaderText("Process failed");
			alert.show();
			return;
		}

		owner.setPrimaryCheuqingAccount((ChequingAccount) acc);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Primary chequing account has been set successfully.");
		alert.setHeaderText("Process succeeded");
		alert.show();
	}

	@FXML
	public void show() {

	}
}
