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
import javafx.scene.layout.HBox;
import model.Cash;
import model.CashCollection;
import model.Money;
import model.Request;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.Loginable;
import model.persons.User;
import model.transactions.Transaction;
import model.transactors.Account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class BankManagerHomeController extends GUIHomeController {

	@FXML
	Label username;
	@FXML
	Label fiveDollar;
	@FXML
	Label tenDollar;
	@FXML
	Label twentyDollar;
	@FXML
	Label fiftyDollar;


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
	TableColumn<Account, String> accDateOfCreation;

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
	TableColumn<Transaction, String> transDate;
	@FXML
	TableColumn<Transaction, Void> transOperation;

	@FXML
	TableView<Request> requestTableView;
	@FXML
	TableColumn<Request, String> requestContent;
	@FXML
	TableColumn<Request, Void> requestOperations;


	@FXML
	private final ObservableList<Account> accData = FXCollections.observableArrayList();
	@FXML
	private final ObservableList<User> userData = FXCollections.observableArrayList();
	@FXML
	private final ObservableList<Transaction> transData = FXCollections.observableArrayList();
	@FXML
	private final ObservableList<Request> requestData = FXCollections.observableArrayList();

	@FXML
	private final SimpleStringProperty fiveDollarAmount = new SimpleStringProperty();
	@FXML
	private final SimpleStringProperty tenDollarAmount = new SimpleStringProperty();
	@FXML
	private final SimpleStringProperty twentyDollarAmount = new SimpleStringProperty();
	@FXML
	private final SimpleStringProperty fiftyDollarAmount = new SimpleStringProperty();


	@FXML
	public void showUserName() {
		StringProperty valueProperty = new SimpleStringProperty((currentUser).getUsername());
		username.textProperty().bind(valueProperty);
	}

	@FXML
	public void showDollarAmount() {
		fiveDollarAmount.setValue(String.valueOf((guiManager.getATM().getBillAmount().get(Cash.FIVE))));
		fiveDollar.textProperty().bind(fiveDollarAmount);

		tenDollarAmount.setValue(String.valueOf((guiManager.getATM().getBillAmount().get(Cash.TEN))));
		tenDollar.textProperty().bind(tenDollarAmount);

		twentyDollarAmount.setValue(String.valueOf((guiManager.getATM().getBillAmount().get(Cash.TWENTY))));
		twentyDollar.textProperty().bind(twentyDollarAmount);

		fiftyDollarAmount.setValue(String.valueOf((guiManager.getATM().getBillAmount().get(Cash.FIFTY))));
		fiftyDollar.textProperty().bind(fiftyDollarAmount);

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

		SimpleDateFormat formatter = guiManager.getBankSystem().getDateFormmater();
		accDateOfCreation.setCellValueFactory(accData ->
			new SimpleStringProperty(formatter.format(accData.getValue().getDateOfCreation())));
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

		userPriChqAcc.setCellValueFactory(userData -> {
			try {
				return new SimpleStringProperty(String.valueOf(
					userData.getValue().getPrimaryChequingAccount().getAccountId()));
			} catch (NullPointerException e) {
				return new SimpleStringProperty("");
			}
		});

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

		SimpleDateFormat formatter = guiManager.getBankSystem().getDateFormmater();
		transDate.setCellValueFactory(transData -> new SimpleStringProperty(formatter.format(
			transData.getValue().getDate())));

		transOperation.setCellFactory(param -> new TableCell<Transaction, Void>() {
			private final Button undoButton = new Button("undo");
			private final HBox pane = new HBox(undoButton);

			{
				undoButton.setOnAction(event -> {
					Transaction trans = getTableView().getItems().get(getIndex());
					try {
						guiManager.getBankSystem().undoTransaction(trans);
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setContentText("This transaction has been undone.");
						alert.setHeaderText("Process succeeded");
						alert.show();
					} catch (InvalidOperationException e) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setContentText("Cannot undo transactions involving non-accounts.");
						alert.setHeaderText("Process failed");
						alert.show();
					} catch (NoEnoughMoneyException e) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setContentText("Cannot undo the transaction because the " +
							"account that received the money does not have enough money.");
						alert.setHeaderText("Process failed");
						alert.show();
					}
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				setGraphic(empty ? null : pane);
			}
		});
	}

	@FXML
	public void showRequestTable() {
		ArrayList<Request> requestList = guiManager.getBankSystem().getRequests();

		requestData.addAll(requestList);
		requestTableView.setItems(requestData);

		requestContent.setCellValueFactory(requestData -> new SimpleStringProperty(
			requestData.getValue().getUser().getUsername()
				+ " asked to create an account of type "
				+ requestData.getValue().getAccountType()
				+ ". Msg: "
				+ requestData.getValue().getMsg()
		));

		requestOperations.setCellFactory(param -> new TableCell<Request, Void>() {
			private final Button acceptButton = new Button("accept");
			private final Button declineButton = new Button("decline");
			private final HBox pane = new HBox(acceptButton, declineButton);

			{
				acceptButton.setOnAction(event -> {
					Request request = getTableView().getItems().get(getIndex());
					guiManager.getBankSystem().processRequest(request, true);
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("This request has been accepted. " +
						"Account will be created automatically for user.");
					alert.setHeaderText("Process succeeded");
					alert.show();
					getTableView().getItems().remove(request);
				});

				declineButton.setOnAction(event -> {
					Request request = getTableView().getItems().get(getIndex());
					guiManager.getBankSystem().processRequest(request, false);
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("This request has been declined. " +
						"User will be informed with a message.");
					alert.setHeaderText("Process succeeded");
					alert.show();
					getTableView().getItems().remove(request);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				setGraphic(empty ? null : pane);
			}
		});
	}


	@Override
	public void show() {
		showUserName();
		showUserTable();
		showAccTable();
		showTransTable();
		showRequestTable();
		showDollarAmount();
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
		loadWindow("/BMSetPrimaryScene.fxml", "Set Primary Account",
			new BMSetPrimaryController(userTableView));
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
		loadWindow("/BMPayBillScene.fxml", "Money Transaction");
	}

	public void usersTabOnSelect(Event event) {
		if (guiManager != null) {
			userTableView.getItems().clear();
			showUserTable();
		}
	}

	public void accountsTabOnSelect(Event event) {
		accTableView.getItems().clear();
		showAccTable();
	}

	public void transactionTabOnSelect(Event event) {
		transTableView.getItems().clear();
		showTransTable();
	}

	public void requestsTabOnSelect(Event event) {
		requestTableView.refresh();
	}

	public void aboutATMTabOnSelect(Event event) {
		showDollarAmount();
	}

	@FXML
	public void restockATMOnClick(ActionEvent actionEvent) {

		try {
			Money m = guiManager.getATM().getDepositController().getDepositMoney();
			if (m instanceof CashCollection) {
				guiManager.getATM().stockCash(((CashCollection) m).getCashMap());
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("ATM replenishment finished.");
				alert.setHeaderText("Process succeeded");
				alert.show();
			} else {
				throw new InvalidOperationException("The envelope does not contain cash.");
			}
		} catch (InvalidOperationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The thing you put in the machine is not cash.");
			alert.setHeaderText("Process failed");
			alert.show();
		}
	}
}
