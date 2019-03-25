package model.transactors;

import model.Money;
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
	public HighInterestAccount(Money balance, Date dateOfCreation, String accountId, User owner) {
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
	public void takeMoneyOut(Money amount) throws NoEnoughMoneyException, InvalidOperationException {
		if (this.balance.getValue().compareTo(amount) < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
		}
		this.balance.getValue().subtract(amount);		// TODO:a higher fee is required
	}

	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	@Override
	public void putMoneyIn(Money amount) {
		this.balance.setValue(this.balance.getValue().add(amount));
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
		Money interest = new Money(this.balance.getValue().getMoneyValue() * interestRate);
		this.balance.setValue(this.balance.getValue().add(interest));
	}

	/**
	 * @return the type of the account
	 */
	@Override
	public String getAccountType(){
		return "HighInterestAccount";
	}
}
