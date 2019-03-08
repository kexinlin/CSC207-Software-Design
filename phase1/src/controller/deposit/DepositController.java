package controller.deposit;

import controller.ATM;
import model.accounts.Account;
import model.exceptions.InvalidOperationException;

public interface DepositController {
	/**
	 * Sets the ATM for this deposit controller
	 * @param atm the ATM to set to.
	 */
	void setATM(ATM atm);

	/**
	 * Gets the ATM for this deposit controller
	 * @return the ATM.
	 */
	ATM getATM();

	/**
	 * Deposits the money read from the device into the Account.
	 */
	void depositMoney(Account acc) throws InvalidOperationException;
}
