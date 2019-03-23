package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import model.Request;
import model.persons.AccountOwner;
import model.transactors.Account;

public class AccountCreationController extends GUIHomeController {

	@FXML
	ChoiceBox<String> accTypeChoiceBox;

	@FXML
	public void initialize() {
		accTypeChoiceBox.setValue("Saving Account");
		accTypeChoiceBox.getItems().add("Saving Account");
		accTypeChoiceBox.getItems().add("Chequing Account");
		accTypeChoiceBox.getItems().add("Credit Card Account");
		accTypeChoiceBox.getItems().add("Line Of Credit Account");
	}

	@FXML
	public void requestButtonOnClick(ActionEvent actionEvent) {

		Request request = new Request((AccountOwner) currentUser, accTypeChoiceBox.getValue(), "Requested "
			+ guiManager.getBankSystem().getCurrentTime().toString());
		guiManager.getBankSystem().getRequests().add(request);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("The request for creating an account has been sent. Please wait for " +
			"bank managers to process it.");
		alert.setHeaderText("Request sent");
		alert.show();
	}

	public void show() {
	}

}
