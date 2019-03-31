package view.GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.persons.AccountOwner;
import model.persons.Loginable;
import model.transactors.Account;

public class AddCoOwnerController extends GUIHomeController {

	@FXML
	TextField coOwnerUsername;

	Account acc;

	public void show() {
	}

	public void addCoOnwerOnClick(ActionEvent actionEvent) {
		if (coOwnerUsername.getText().equals("")) {
			err("Please fill in username.",
				"Process failed");
			return;
		}

		Loginable loginable = guiManager.getBankSystem().getLoginable(coOwnerUsername.getText());
		if (loginable == null) {
			err("Username does not exist.",
				"Process failed");
			return;
		}

		if (!(loginable instanceof AccountOwner)) {
			err("The individual is not a account owner.",
				"Process failed");
			return;
		}

		guiManager.getBankSystem().addCoOwner(acc, (AccountOwner) loginable);
		showAlert(Alert.AlertType.INFORMATION,
			"New co-owner has been added to this account.",
			"Process succeeded");
		getStage().close();
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}
}
