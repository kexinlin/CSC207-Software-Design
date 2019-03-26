package view.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Message;
import model.persons.User;
import model.transactors.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class BankManagerHomeController extends GUIHomeController {

	@FXML
	Label username;
	

	@FXML
	public void showUserName() {
		StringProperty valueProperty = new SimpleStringProperty((currentUser).getUsername());
		username.textProperty().bind(valueProperty);
	}

	@Override
	public void show() {
		showUserName();
	}

	@FXML
	public void createAccountButtonOnClick(ActionEvent actionEvent) {
		loadWindow("/AccountCreationScene.fxml", "Account Creation Request");
	}

	@FXML
	public void withdrawOnClick(ActionEvent actionEvent) {
		loadWindow("/WithdrawScene.fxml", "Cash Withdrawal");
	}

	@FXML
	public void depositOnClick(ActionEvent actionEvent) {
		loadWindow("/DepositScene.fxml", "Money Deposit");
	}

	@FXML
	public void transferOnClick(ActionEvent actionEvent) {
		loadWindow("/TransferScene.fxml", "Money Transaction");
	}

	@FXML
	public void payBillOnClick(ActionEvent actionEvent) {
		loadWindow("/PayBillScene.fxml", "Money Transaction");

	}

	public void userListTabOnSelect(Event event) {
	}

	public void accountListTabOnSelect(Event event) {
	}

	public void createUserButtonOnClick(ActionEvent actionEvent) {
	}
}
