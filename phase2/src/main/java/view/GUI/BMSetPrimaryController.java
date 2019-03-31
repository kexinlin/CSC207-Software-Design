package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.exceptions.AccountNotExistException;
import model.persons.AccountOwner;
import model.persons.Loginable;
import model.persons.User;
import model.transactors.Account;
import model.transactors.ChequingAccount;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BMSetPrimaryController extends GUIHomeController {
	private TableView<AccountOwner> tableView;
	public BMSetPrimaryController(TableView<AccountOwner> userTableView) {
		this.tableView = userTableView;
	}

	@FXML
	TextField chqAccUsername;
	@FXML
	TextField priChqAccNum;
	@FXML
	Button acceptButton;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		acceptButton.setOnMouseClicked(this::ManagerSetPrimaryOnClick);
		if(tableView.getFocusModel().getFocusedItem() != null) {
			chqAccUsername.setText(tableView.getFocusModel().getFocusedItem().getUsername());
		}
	}

	private void ManagerSetPrimaryOnClick(MouseEvent ignore) {
		if (chqAccUsername.getText().equals("")||priChqAccNum.getText().equals("")) {
			err("Please fill in all required fields.",
				"Process failed");
			return;
		}

		Loginable loginable = guiManager.getBankSystem().getLoginable(chqAccUsername.getText());
		if (loginable == null) {
			err("Username does not exist.",
				"Process failed");
			return;
		}
		if (!(loginable instanceof AccountOwner)) {
			err("The individual is not a user.",
				"Process failed");
			return;
		}
		AccountOwner owner = (AccountOwner) loginable;

		Account acc;

		try {
			acc = guiManager.getBankSystem().getAccountById(priChqAccNum.getText());
		} catch (AccountNotExistException e) {
			err("Account number does not exist.",
				"Process failed");
			return;
		}

		if(!(acc instanceof ChequingAccount)){
			err("Account type must be chequing account.",
				"Process failed");
			return;
		}

		if(acc.isOwnedBy(owner)){
			err("Owner of input account does not match input username.",
				"Process failed");
			return;
		}

		owner.setPrimaryCheuqingAccount((ChequingAccount) acc);
		showAlert(Alert.AlertType.INFORMATION,
			"Primary chequing account has been set successfully.",
			"Process succeeded");
		getStage().close();
	}

	@FXML
	public void show() {

	}
}
