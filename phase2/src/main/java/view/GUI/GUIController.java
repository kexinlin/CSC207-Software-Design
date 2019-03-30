package view.GUI;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class GUIController implements Initializable {
	/// register event handlers here
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}
	GUIManager guiManager;

	public void setGUIManager(GUIManager guiManager) {
		this.guiManager = guiManager;
	}

	protected void showAlert(Alert.AlertType type, String content, String title) {
		Alert alert = new Alert(type);
		alert.setContentText(content);
		alert.setHeaderText(title);
		alert.show();
	}

	protected void err(String content, String title) {
		showAlert(Alert.AlertType.ERROR, content, title);
	}
}
