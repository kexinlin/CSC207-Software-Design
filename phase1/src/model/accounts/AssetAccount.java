package model.accounts;

import model.persons.User;

import java.util.Date;

public abstract class AssetAccount extends Account {
	public AssetAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	/**
	 * Return the balance factor for this account.
	 * This value is 1 if a positive balance means the account holder
	 * has money while a negative balance means the holder owes money.
	 * It should be -1 otherwise.
	 *
	 * @return the balance factor of this account
	 */
	@Override
	public int balanceFactor() {
		return 1;
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
}
