package controller;

import view.cmdline.CommandLineUI;
import view.UI;

/**
 * The entrance to the whole program.
 */
public class Main {
	public static void main(String[] args) {
		String recordFileName = "records.txt";
		BankSystem sys = new BankSystem(recordFileName);
		ATM atm = new ATM(sys);
		UI ui = new CommandLineUI(atm,
			System.in,
			System.out,
			System.err,
			false);
		ui.mainLoop();
		sys.close();
	}
}
