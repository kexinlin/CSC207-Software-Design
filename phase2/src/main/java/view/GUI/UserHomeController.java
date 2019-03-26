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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.persons.User;
import model.transactors.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class UserHomeController extends GUIHomeController {

	@FXML
	Label name;
	@FXML
	Label netTotal;
	@FXML
	TableColumn<Account, String> accType;
	@FXML
	TableColumn<Account, String> accNum;
	@FXML
	TableColumn<Account, String> accBalance;
	@FXML
	TableColumn<Account, Date> accDateOfCreation;
	@FXML
	TableView<Account> accTableView;
	@FXML
	private final SimpleStringProperty netTotalVal = new SimpleStringProperty();
	@FXML
	private final ObservableList<Account> data = FXCollections.observableArrayList();


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

	@Override
	public void show() {
		showName();
		showTable();
		showAndRefreshNetTotal();
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

	@FXML
	public void logOutButtonOnClick(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScene.fxml"));
		AnchorPane homeScene = loader.load();

		GUIController controller = loader.getController();
		controller.setGUIManager(guiManager);

		Main.root.getChildren().setAll(homeScene);
	}

	@FXML
	public void loadWindow(String location, String title) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
			Parent parent = loader.load();

			GUIHomeController homeController = loader.getController();
			homeController.setGUIManager(guiManager);
			homeController.setCurrentUser(getCurrentUser());
			homeController.show();

			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);


			stage.setScene(new Scene(parent));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@FXML
	public void myAccountsTabOnSelect(Event event) {
		accTableView.refresh();
		if (currentUser != null) {
			showAndRefreshNetTotal();
		}

	}

	public void myProfilesTabOnSelect(Event event) {
	}
}
