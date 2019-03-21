package model.accounts;

import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.exceptions.NoTransactionException;
import model.transactions.Transaction;
import model.persons.User;

import java.util.ArrayList;
import java.util.Date;

public class HighInterestAccount extends Account{
	// The account owns higher interest, but each time you take money out, higher fee is required.
	private double interestRate = 0.5/100;

	/**
	 * Take `amount` of money out of the account.
	 * NoEnoughMoneyException will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(double amount) throws NoEnoughMoneyException {
		if (this.balance < amount) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
		}
		this.balance -= amount;		// TODO: change to a higher fee
	}

	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	@Override
	public void putMoneyIn(double amount) {
		this.balance += amount;
	}

	/**
	 * Return the balance factor for this account.
	 * This value is 1 if a positive balance means the account holder
	 * has money while a negative balance means the holder owes money.
	 * It should be -1 otherwise.
	 * @return the balance factor of this account
	 */
	@Override
	public int balanceFactor(){
		return 1;
	}
}
