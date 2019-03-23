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
import javafx.scene.layout.BorderPane;
import model.persons.Loginable;
import model.persons.User;

import java.util.Observable;

public class UserHomeController extends GUIHomeController {

	@FXML
	Label name;


	@FXML
	public void showLabel() {
		StringProperty valueProperty = new SimpleStringProperty(((User)loginable).getName());
		name.textProperty().bind(valueProperty);
	}

	public void LogOutButtonOnClick(ActionEvent actionEvent) {

	}
}
