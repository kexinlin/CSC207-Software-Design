package view.GUI;

import javafx.fxml.FXML;
import model.persons.Loginable;

public abstract class GUIHomeController extends GUIController{

	Loginable loginable;

	@FXML
	public void setCurrentUser(Loginable loginable){
		this.loginable = loginable;
	}

	@FXML
	public abstract void showLabel();

	@FXML
	public void show() {
		showLabel();
	}


}
