package atm;

import java.util.Date;

public abstract class Account {
	protected double balance;
	protected Date dateOfCreation;
	protected String accountId;
	protected User owner;

	/**
	 * Create an instance of account
	 * @param balance the balance of the account
	 * @param dateOfCreation the date of creation
	 * @param accountId account id
	 * @param owner owner of the account
	 */
	public Account(double balance, Date dateOfCreation, String accountId, User owner) {
		this.balance = balance;
		this.dateOfCreation = dateOfCreation;
		this.accountId = accountId;
		this.owner = owner;
	}

	/**
	 * Gets the balance of this account.
	 * @return balance of the account
	 */
	public double getBalance() {
		return this.balance;
	}

	/**
	 * Gets the account id of this account.
	 * @return account id
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Gets the date of account creation.
	 * @return date of creation
	 */
	public Date getDateOfCreation() {
		return this.dateOfCreation;
	}

	// return boolean to indicate whether the action succeeds or not

	/**
	 * Take `amount` of money out of the account.
	 * @param amount the amount of money to take out.
	 * @return true if the operation succeeds, false otherwise.
	 */
	public abstract boolean takeMoneyOut(double amount);

	// FIXME: In my opinion, this should be the function of
	// `Transaction`s
	//public abstract boolean transferOut(Account destinationAccount);

	// increaseBalance always return true

	/**
	 * Put `amount` of money into the account.
	 * @param amount the amount of money to put in
	 * @return true if the operation succeeds, false otherwise.
	 */
	public abstract boolean putMoneyIn(double amount);

	//public abstract boolean payBill(String nonUserAccount);

}
