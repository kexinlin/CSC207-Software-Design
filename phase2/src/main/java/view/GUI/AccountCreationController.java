package view.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

public class AccountCreationController extends GUIController{

	@FXML
	ChoiceBox<String> accTypeChoiceBox;


	@FXML
	public void initialize(){
		accTypeChoiceBox.setValue("Saving Account");
		accTypeChoiceBox.getItems().add("Saving Account");
		accTypeChoiceBox.getItems().add("Chequing Account");
		accTypeChoiceBox.getItems().add("Credit Card Account");
		accTypeChoiceBox.getItems().add("Line Of Credit Account");
	}

	@FXML
	public void requestButtonOnClick(ActionEvent actionEvent) {
		System.out.println(accTypeChoiceBox.getValue());

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("The creation of transactors has been sent. Please wait for " +
			"bank managers to process it.");
		alert.setHeaderText("Request sent");
		alert.show();
	}

}
