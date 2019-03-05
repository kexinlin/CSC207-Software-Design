package atm;

import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

import java.util.Date;
import java.util.Stack;

public abstract class Account {
	protected double balance;
	protected Date dateOfCreation;
	protected String accountId;
	protected User owner;
	protected Stack<Transaction> logs;

	/**
	 * Create an instance of account
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the date of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public Account(double balance, Date dateOfCreation, String accountId, User owner) {
		this.balance = balance;
		this.dateOfCreation = dateOfCreation;
		this.accountId = accountId;
		this.owner = owner;
		this.logs = new Stack<Transaction>();
	}


	/**
	 * Gets the balance of this account.
	 *
	 * @return balance of the account
	 */
	public double getBalance() {
		return this.balance;
	}


	/**
	 * Gets the account id of this account.
	 *
	 * @return account id
	 */
	public String getAccountId() {
		return accountId;
	}


	/**
	 * Gets the date of account creation.
	 *
	 * @return date of creation
	 */
	public Date getDateOfCreation() {
		return this.dateOfCreation;
	}


	/**
	 * Gets the owner of the account.
	 *
	 * @return the User owner of the account
	 */
	public User getOwner() {
		return owner;
	}


	/**
	 * Take `amount` of money out of the account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	public abstract void takeMoneyOut(double amount);


	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	public abstract void putMoneyIn(double amount);


	/**
	 * Pay `amount` of money to a non-user account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 *
	 * @param nonUserAccount a non-user account represented by a String
	 * @param amount         the amount of bill
	 */
	public abstract void payBill(String nonUserAccount, double amount);


	/**
	 * Return whether `this.log` contains no transaction
	 *
	 * @return return true when `this.log` contains no transaction
	 */
	public boolean logEmpty() {
		return this.logs.empty();
	}


	/**
	 * @return
	 */
	public boolean undoTrans() {
		this.logs.pop();
		// also have to deal with the money
		return true;
	}
}
