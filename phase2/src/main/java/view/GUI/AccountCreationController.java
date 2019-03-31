package view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import model.Request;
import model.persons.AccountOwner;

import java.util.ArrayList;

public class AccountCreationController extends GUIHomeController {

	@FXML
	ChoiceBox<String> accTypeChoiceBox;


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

	@FXML
	public void show() {
		ArrayList<String> accTypeList = guiManager.getBankSystem().getAllAccountType();
		for (String accType : accTypeList) {
			accTypeChoiceBox.getItems().add(accType);
		}
	}
}
