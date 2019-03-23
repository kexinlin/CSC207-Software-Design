package view.GUI;

import controller.ATM;
import controller.BankSystem;

public class GUIManager {

	ATM atm;
	BankSystem bankSystem;

	/**
	 * Construct a new GUIManager
	 * @param atm the ATM machine to handle
	 * @param bankSystem the bank system of the machine
	 */
	GUIManager(ATM atm, BankSystem bankSystem){
		this.atm = atm;
		this.bankSystem = bankSystem;
	}

}
