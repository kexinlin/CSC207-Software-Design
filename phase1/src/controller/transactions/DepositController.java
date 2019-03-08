package controller.transactions;

import controller.ATM;
import model.accounts.Account;
import model.exceptions.InvalidOperationException;

public interface DepositController {
	/**
	 * Sets the ATM for this transactions controller
	 * @param atm the ATM to set to.
	 */
	void setATM(ATM atm);

	/**
	 * Gets the ATM for this transactions controller
	 * @return the ATM.
	 */
	ATM getATM();

	/**
	 * Deposits the money read from the device into the Account.
	 */
	void depositMoney(Account acc) throws InvalidOperationException;
}
