package view.GUI;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.persons.Loginable;
import model.persons.User;
import model.transactions.Transaction;
import model.transactors.Account;

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

	@FXML
	TableView<User> userTableView;
	@FXML
	TableColumn<User, String> userName;
	@FXML
	TableColumn<User, String> userUsername;
	@FXML
	TableColumn<User, String> userPriChqAcc;
	@FXML
	TableColumn<User, String> userNetTotal;

	@FXML
	TableView<Transaction> transTableView;
	@FXML
	TableColumn<Transaction, String> transSrcAcc;
	@FXML
	TableColumn<Transaction, String> transDesAcc;
	@FXML
	TableColumn<Transaction, String> transAmount;
	@FXML
	TableColumn<Transaction, Date> transDate;


	@FXML
	private final ObservableList<Account> accData = FXCollections.observableArrayList();
	@FXML
	private final ObservableList<User> userData = FXCollections.observableArrayList();
	@FXML
	private final ObservableList<Transaction> transData = FXCollections.observableArrayList();


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

	@FXML
	public void showUserTable() {

		Collection<Loginable> loginableCollection = guiManager.getBankSystem().getLoginables().values();

		ArrayList<User> userList = new ArrayList<>();

		for (Loginable l : loginableCollection) {
			if (l instanceof User) {
				userList.add((User) l);
			}
		}

		userData.addAll(userList);
		userTableView.setItems(userData);

		userName.setCellValueFactory(userData -> new SimpleStringProperty(userData.getValue().getName()));

		userUsername.setCellValueFactory(userData -> new SimpleStringProperty(userData.getValue().getUsername()));

		userPriChqAcc.setCellValueFactory(userData -> new SimpleStringProperty(String.valueOf(
			userData.getValue().getPrimaryChequingAccount().getAccountId())));

		userNetTotal.setCellValueFactory(userData -> new SimpleStringProperty(String.valueOf(
			userData.getValue().getNetTotal())));
	}

	@FXML
	public void showTransTable() {

		ArrayList<Transaction> transList = guiManager.getBankSystem().getTransactions();


		transData.addAll(transList);
		transTableView.setItems(transData);

		transSrcAcc.setCellValueFactory(transData -> new SimpleStringProperty(transData.getValue().getSource().getId()));

		transDesAcc.setCellValueFactory(transData -> new SimpleStringProperty(transData.getValue().getDest().getId()));

		transAmount.setCellValueFactory(transData -> new SimpleStringProperty(String.valueOf(
			transData.getValue().getAmount().getMoneyValue())));

		transDate.setCellValueFactory(transData -> new SimpleObjectProperty<>(transData.getValue().getDate()));
	}


	@Override
	public void show() {
		showUserName();
		showUserTable();
		showAccTable();
		showTransTable();
	}

	@FXML
	public void createAccountButtonOnClick(ActionEvent actionEvent) {
		loadWindow("/NewAccountCreationScene.fxml", "Account Creation Request");
	}

	@FXML
	public void createUserButtonOnClick(ActionEvent actionEvent) {
		loadWindow("/NewUserCreationScene.fxml", "New User Creation");
	}

	@FXML
	public void setPriChqAccOnClick(ActionEvent actionEvent) {
		loadWindow("/BMSetPrimaryScene.fxml", "New User Creation");
	}

	@FXML
	public void withdrawOnClick(ActionEvent actionEvent) {
		loadWindow("/BMWithdrawScene.fxml", "Cash Withdrawal");
	}

	@FXML
	public void depositOnClick(ActionEvent actionEvent) {
		loadWindow("/BMDepositScene.fxml", "Money Deposit");
	}

	@FXML
	public void transferOnClick(ActionEvent actionEvent) {
		loadWindow("/BMTransferScene.fxml", "Money Transaction");
	}

	@FXML
	public void payBillOnClick(ActionEvent actionEvent) {
		loadWindow("/PayBillScene.fxml", "Money Transaction");
	}


	public void accountsTabOnSelect(Event event) {
		accTableView.refresh();
	}

	public void usersTabOnSelect(Event event) {
		userTableView.refresh();
	}

	public void transactionTabOnSelect(Event event) {
		transTableView.getItems().clear();
		showTransTable();
	}

	public void requestsTabOnSelect(Event event) {
	}

	public void aboutATMTabOnSelect(Event event) {
	}
}
