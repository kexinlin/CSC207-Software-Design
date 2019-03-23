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

import java.util.Observable;

public class UserHomeController {
	String username = "test";

	@FXML
	Label test;


	@FXML
	public void showLabel(){
		StringProperty valueProperty = new SimpleStringProperty(username);
		test.textProperty().bind(valueProperty);

	}

	@FXML
	public void initialize(){
		showLabel();
	}

}
