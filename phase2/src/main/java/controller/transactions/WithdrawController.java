package controller.transactions;

import controller.ATM;
import model.Cash;
import model.transactors.Account;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;

import java.util.HashMap;

public interface WithdrawController {
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
	 * Put the money into the envelope.
	 * @param cashMap the HashMap containing types and numbers of different cash.
	 */
	void withdrawMoney(HashMap<Cash, Integer> cashMap)
		throws InvalidOperationException, InsufficientCashException;
}
