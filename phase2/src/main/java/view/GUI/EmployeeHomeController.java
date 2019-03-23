package view.GUI;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.persons.Loginable;
import model.persons.User;

import java.util.Observable;

public class EmployeeHomeController extends GUIHomeController {
	@FXML
	Label test;


	@FXML
	public void showLabel() {
		StringProperty valueProperty = new SimpleStringProperty(loginable.getUsername());
		test.textProperty().bind(valueProperty);
		System.out.println(loginable);
	}


}
