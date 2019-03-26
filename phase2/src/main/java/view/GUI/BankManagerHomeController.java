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
import model.persons.AccountOwner;
import model.persons.User;
import model.transactors.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class BankManagerHomeController extends GUIHomeController {

	@FXML
	Label username;

	@FXML
	TableView<Account> accTableView;
	@FXML
	TableColumn<Account, String> accOwner;
	@FXML
	TableColumn<Account, String> accType;
	@FXML
	TableColumn<Account, String> accNum;
	@FXML
	TableColumn<Account, String> accBalance;
	@FXML
	TableColumn<Account, Date> accDateOfCreation;


//	@FXML
//	TableView<User> userTableView;
//	@FXML
//	TableColumn<User, String> userName;
//	@FXML
//	TableColumn<User, String> accType;
//	@FXML
//	TableColumn<User, String> accNum;
//	@FXML
//	TableColumn<User, String> accBalance;
//	@FXML
//	TableColumn<User, Date> accDateOfCreation;


	@FXML
	private final ObservableList<Account> accData = FXCollections.observableArrayList();


	@FXML
	public void showUserName() {
		StringProperty valueProperty = new SimpleStringProperty((currentUser).getUsername());
		username.textProperty().bind(valueProperty);
	}

	@FXML
	public void showAccTable() {

		Collection<Account> accCollection = guiManager.getBankSystem().getAccounts().values();

		ArrayList<Account> accList = new ArrayList<>(accCollection);

		accData.addAll(accList);

		accTableView.setItems(accData);


		accOwner.setCellValueFactory(
			new PropertyValueFactory<>("primaryOwner")
		);

		accOwner.setCellValueFactory(accData -> new SimpleStringProperty(accData.getValue().getOwner().getUsername()));

		accType.setCellValueFactory(
			new PropertyValueFactory<>("accountType")
		);
		accNum.setCellValueFactory(
			new PropertyValueFactory<>("accountId")
		);
		accBalance.setCellValueFactory(
			new PropertyValueFactory<>("balance")
		);
		accDateOfCreation.setCellValueFactory(
			new PropertyValueFactory<>("dateOfCreation")
		);
	}



	@Override
	public void show() {
		showUserName();
		showAccTable();
	}

	@FXML
	public void createAccountButtonOnClick(ActionEvent actionEvent) {
		loadWindow("/RequestAccCreationScene.fxml", "Account Creation Request");
	}

	@FXML
	public void createUserButtonOnClick(ActionEvent actionEvent) {
		loadWindow("/NewUserCreationScene.fxml", "New User Creation");
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


	public void accountsTabOnSelect(Event event) {
		accTableView.refresh();
	}

	public void usersTabOnSelect(Event event) {
	}
}
