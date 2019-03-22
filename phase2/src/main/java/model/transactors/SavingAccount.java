package model.transactors;

import model.Money;
import model.exceptions.NoEnoughMoneyException;
import model.persons.User;

import java.util.Date;

public class SavingAccount extends AssetAccount {
	private double interestRate;

	/**
	 * Create an instance of SavingAccount
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public SavingAccount(Money balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
		this.interestRate = 0.1/100;
	}

	/**
	 * Take `amount` of money out of the account.
	 * NonEnoughMoneyException will also be raised if the amount will result in a negative balance.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(Money amount) throws NoEnoughMoneyException {
		if (this.balance.compareTo(amount) < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
		}
		this.balance = this.balance.subtract(amount);
	}

	/**
	 * Increase the saving account balance bt a factor of 0.1%.
	 */
	public void increaseInterest() {
		Money interest = new Money(this.balance.getValue() * interestRate);
		this.balance = this.balance.add(interest);
	}
}
