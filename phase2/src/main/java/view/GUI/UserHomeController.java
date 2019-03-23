package view.GUI;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.persons.Loginable;
import model.persons.User;
import model.transactors.Account;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

public class UserHomeController extends GUIHomeController {

	@FXML
	Label name;
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

	private final ObservableList<Account> data = FXCollections.observableArrayList();


	@FXML
	public void showLabel() {
		StringProperty valueProperty = new SimpleStringProperty(((User)loginable).getName());
		name.textProperty().bind(valueProperty);
	}

	@FXML
	public void LogOutButtonOnClick(ActionEvent actionEvent) {

	}


	@FXML
	public void showTable(){

		ArrayList<Account> accList = ((User)loginable).getAccounts();

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
	public void show(){
		showLabel();
		showTable();
	}


}
