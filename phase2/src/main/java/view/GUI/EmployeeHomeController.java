package view.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EmployeeHomeController extends GUIHomeController {
	@FXML
	Label test;


	@FXML
	public void showLabel() {
		StringProperty valueProperty = new SimpleStringProperty(currentUser.getUsername());
		test.textProperty().bind(valueProperty);
		System.out.println(currentUser);
	}

	public void show(){
		showLabel();
	}


}
