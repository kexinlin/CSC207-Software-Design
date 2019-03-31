package view.GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.persons.AccountOwner;
import model.persons.Loginable;
import model.transactors.Account;

public class RemoveCoOwnerController extends GUIHomeController {

	@FXML
	TextField coOwnerUsername;

	Account acc;

	public void show() {
	}

	public void removeCoOnwerOnClick(ActionEvent actionEvent) {
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

		if(guiManager.getBankSystem().removeCoOwner(acc, (AccountOwner) loginable)){
		showAlert(Alert.AlertType.INFORMATION,
			"The co-owner has been removed from this account.",
			"Process succeeded");
		getStage().close();}
		else{
			err("The individual is not a co-owner of this account.",
				"Process failed");
		}
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}
}
