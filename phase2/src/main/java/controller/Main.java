package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import view.GUI.GraphicsUI;
import view.cmdline.CommandLineUI;
import view.UI;

import java.util.List;

/**
 * The entrance to the whole program.
 */
public class Main extends Application {
	public Main() {}

	public void start(Stage ignore) {
		String recordFileName = "records.txt";
		String atmRecordFileName = "atm-records.txt";
		BankSystem sys = new BankSystem(recordFileName);
		ATM atm = new ATM(sys, atmRecordFileName);
		UI ui;
		List<String> args = getParameters().getRaw();
		if (args.size() >= 1 && args.get(0).equals("--cmdline")) {
			ui = new CommandLineUI(atm,
				System.in,
				System.out,
				System.err,
				false);
		} else {
			ui = new GraphicsUI(atm);
		}
		ui.mainLoop();
//		atm.close();
//		sys.close();
		Platform.exit();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
