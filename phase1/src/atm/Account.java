package atm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public abstract class Account {
	double balance;
	private Date dateOfCreation;
	private String accountId;
	private User owner;
	private ArrayList<Transaction> logs;
	private ATM atm;
	private static final String PAY_BILL_FILE_NAME = "outgoing.txt";

	/**
	 * Create an instance of account
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public Account(double balance, Date dateOfCreation, String accountId, User owner) {
		this.balance = balance;
		this.dateOfCreation = dateOfCreation;
		this.accountId = accountId;
		this.owner = owner;
		this.logs = new ArrayList<>();
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
	 * Gets the currentTime of account creation.
	 *
	 * @return currentTime of creation
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
	public abstract void takeMoneyOut(double amount) throws NoEnoughMoneyException, InvalidOperationException;


	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	public void putMoneyIn(double amount) {
		this.balance += amount;
	}


	/**
	 * Pay `amount` of money to a non-user account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 * If succeeds, result will be recorded in outgoing.txt.
	 *
	 * @param nonUserAccount a non-user account represented by a String
	 * @param amount         the amount of bill
	 */
	public void payBill(String nonUserAccount, double amount) throws NoEnoughMoneyException{
		Writer writer;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("." + File.separator + "phase1" + File.separator
					+ PAY_BILL_FILE_NAME), StandardCharsets.UTF_8));
			String timeStr = atm.getCurrentTimeStr();
			writer.write(String.join(",", this.accountId, this.owner.getUsername(),
				nonUserAccount, String.valueOf(amount), timeStr));
			writer.close();
		} catch (IOException ex) {
			/*ignore*/
		}
	}


	/**
	 * Return whether `this.log` contains no transaction.
	 *
	 * @return return true when `this.log` contains no transaction
	 */
	public boolean logEmpty() {
		return this.logs.isEmpty();
	}


	/**
	 * Return the most recent Transaction.
	 * NoTransactionException will be throw if transaction record is empty.
	 *
	 * @return the most recently added Transaction into logs
	 */
	public Transaction getLastTrans() throws NoTransactionException {
		if (!logEmpty()) {
			return this.logs.get(logs.size() - 1);
		} else {
			throw new NoTransactionException("This account does not have transaction record.");
		}
	}

	public void addTrans(Transaction trans) {
		this.logs.add(trans);

	}

}
