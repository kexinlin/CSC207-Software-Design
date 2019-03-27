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
import model.persons.Loginable;
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
	private final ObservableList<Account> accData = FXCollections.observableArrayList();
	@FXML
	private final ObservableList<User> userData = FXCollections.observableArrayList();


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

		for(Loginable l:loginableCollection){
			if(l instanceof User){
				userList.add((User)l);
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


	@Override
	public void show() {
		showUserName();
		showUserTable();
		showAccTable();
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
		userTableView.refresh();
	}

}
