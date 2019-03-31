package model.transactors;

import javafx.beans.property.SimpleObjectProperty;
import model.Money;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;

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
	public SavingAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
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
		if (getBalance().compareTo(amount) < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
		}
		setBalance(getBalance().subtract(amount));
	}

	@Override
	public boolean hasInterest() {
		return true;
	}
	/**
	 * Increase the saving account balance bt a factor of 0.1%.
	 */
	@Override
	public void increaseInterest() {
		Money interest = new Money(getBalance().getMoneyValue() * interestRate);
		setBalance(getBalance().add(interest));
	}

	/**
	 * @return the type of the account
	 */
	@Override
	public String getAccountType(){
		return "SavingAccount";
	}
}
