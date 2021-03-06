package model.transactors;

import model.Money;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.exceptions.NoTransactionException;
import model.persons.AccountOwner;
import model.transactions.Transaction;
import model.persons.User;

import java.util.ArrayList;
import java.util.Date;

public class HighInterestAccount extends AssetAccount {
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
	public HighInterestAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
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
		if (getBalance().compareTo(amount) < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
		}
		setBalance(getBalance().subtract(amount));		// TODO:a higher fee is required
	}

	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	@Override
	public void putMoneyIn(Money amount) {
		setBalance(getBalance().add(amount));
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
		Money interest = new Money(getMinimumBalance().getMoneyValue() * interestRate);
		setBalance(getBalance().add(interest));
	}

	/**
	 * @return the type of the account
	 */
	@Override
	public String getAccountType(){
		return "HighInterestAccount";
	}

	@Override
	public Money getFeeFor(Transaction tx) {
		Money onePercentFee = new Money(tx.getAmount().getMoneyValue() * 0.01);
		Money free = new Money(0);
		if (! (tx.getDest() instanceof Account)) {
			return onePercentFee;
		}
		if (((Account) tx.getDest()).isOwnedBy(getOwner())) {
			return free;
		}
		for (AccountOwner owner : getCoOwners()) {
			if (((Account) tx.getDest()).isOwnedBy(owner)) {
				return free;
			}
		}
		return onePercentFee;
	}
}
