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
	private double interestRate;

	/**
	 * Create an instance of HighInterestAccount
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public HighInterestAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
		this.interestRate = 0.5/100;
	}
	/**
	 * Take `amount` of money out of the account.
	 * NoEnoughMoneyException will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(double amount) throws NoEnoughMoneyException {
		if (this.balance < amount * (1.05)) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
		}
		this.balance -= amount * (1.05);		// a higher fee is required
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

	/**
	 * Increase the saving account balance bt a factor of 0.1%.
	 */
	public void increaseInterest() {
		this.balance *= (1 + interestRate);
	}
}
