package view.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Message;
import model.persons.User;
import model.transactors.Account;
import java.util.ArrayList;
import java.util.Date;

public class UserHomeController extends GUIHomeController {

	@FXML
	Label name;
	@FXML
	Label netTotal;

	@FXML
	TableView<Account> accTableView;
	@FXML
	TableColumn<Account, String> accType;
	@FXML
	TableColumn<Account, String> accNum;
	@FXML
	TableColumn<Account, String> accBalance;
	@FXML
	TableColumn<Account, Date> accDateOfCreation;

	// label in My Profiles
	@FXML
	Label nameInProfile;
	@FXML
	Label usernameInProfile;
	@FXML
	Label priChqAccNum;

	@FXML
	ListView<String> msgListView;

	@FXML
	private final SimpleStringProperty netTotalVal = new SimpleStringProperty();
	@FXML
	private final ObservableList<Account> data = FXCollections.observableArrayList();
	@FXML
	private final SimpleStringProperty priChqAccNumVal = new SimpleStringProperty();
	@FXML
	private final ObservableList<String> msgData = FXCollections.observableArrayList();


	@FXML
	public void showName() {
		StringProperty valueProperty = new SimpleStringProperty(((User) currentUser).getName());
		name.textProperty().bind(valueProperty);
	}

	@FXML
	public void showAndRefreshNetTotal() {
		netTotalVal.setValue(String.valueOf(((User) currentUser).getNetTotal()));
		netTotal.textProperty().bind(netTotalVal);
	}

	@FXML
	public void showTable() {

		ArrayList<Account> accList = ((User) currentUser).getAccounts();

		data.addAll(accList);

		accTableView.setItems(data);

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
	public void showMessageList(){
		ArrayList<Message> msgList = ((User) currentUser).getMessages();
		ArrayList<String> msgContentList = new ArrayList<>();
		for(Message msg:msgList){
			msgContentList.add(msg.getText());
		}
		msgData.addAll(msgContentList);
		msgListView.setItems(msgData);
	}

	@FXML
	public void showNameInProfile() {
		StringProperty nameProperty = new SimpleStringProperty(((User) currentUser).getName());
		nameInProfile.textProperty().bind(nameProperty);

		StringProperty usernameProperty = new SimpleStringProperty((currentUser).getUsername());
		usernameInProfile.textProperty().bind(usernameProperty);
	}

	@FXML
	public void showAndRefreshPriChqAcc() {
		if (((User) currentUser).getPrimaryChequingAccount() == null) {
			priChqAccNumVal.setValue("You don't have a primary chequing account");
		} else {
			priChqAccNumVal.setValue(String.valueOf(((User) currentUser).getPrimaryChequingAccount().getAccountId()));
		}
		priChqAccNum.textProperty().bind(priChqAccNumVal);
	}

	@Override
	public void show() {
		showName();
		showTable();
		showAndRefreshNetTotal();
		showNameInProfile();
		showAndRefreshPriChqAcc();
		showMessageList();
	}

	@FXML
	public void createAccountButtonOnClick(ActionEvent actionEvent) {
		loadWindow("/RequestAccCreationScene.fxml", "Account Creation Request");
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

	@FXML
	public void setPasswordOnClick(ActionEvent actionEvent) {
		loadWindow("/SetPasswordScene.fxml", "Change Password");
	}

	@FXML
	public void setPriChqAccOnClick(ActionEvent actionEvent) {
		loadWindow("/SetPriChqAccScene.fxml", "Primary Chequing Account");
	}

	@FXML
	public void myAccountsTabOnSelect(Event event) {
		accTableView.refresh();
		if (currentUser != null) {
			showAndRefreshNetTotal();
		}

	}

	@FXML
	public void myProfilesTabOnSelect(Event event) {
		showAndRefreshPriChqAcc();
	}


}
