package view.GUI;

import javafx.fxml.FXML;
import model.persons.Loginable;

public abstract class GUIHomeController extends GUIController{

	Loginable currentUser;

	@FXML
	public void setCurrentUser(Loginable loginable){
		this.currentUser = loginable;
	}

	public Loginable getCurrentUser() {
		return currentUser;
	}

	@FXML
	public abstract void show();

}
