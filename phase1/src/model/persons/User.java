package model.persons;

import controller.ATM;
import model.accounts.*;
import model.exceptions.NoTransactionException;
import model.transactions.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class User implements Loginable {
	private String name;
	private String username;
	private ATM machine;
	private String password;
	private ArrayList<Account> accounts = new ArrayList<>();
	private ChequingAccount primaryAccount;
	private Date date;
	private ArrayList<Transaction> transactions = new ArrayList<>();


	public User(ATM atm, String name, String username, String password) {
		this.machine = atm;
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	/**
	 * Check if the password provided is the same as the one set for the user.
	 *
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}


	/**
	 * Change the password of this user to `password`.
	 * @param password the password to change to.
	 * @return true if succeeds, false otherwise.
	 */
	public boolean setPassword(String password) {
		this.password = password;
		return true;
	}


	/**
	 * Gets the net balance of this user
	 * @return net balance
	 */
	public double getNetTotal() {
		return accounts.stream()
			.mapToDouble(acc -> acc.getBalance() * acc.balanceFactor())
			.sum();
	}


	public Date getDateOfCreation() {
		return date;
	}


	/**
	 * return all available accounts
	 *
	 * @return
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public boolean logEmpty() {
		return this.transactions.isEmpty();
	}

	public Transaction getMostRecentTransaction() throws NoTransactionException {
		if (!logEmpty()) {
			return this.transactions.get(transactions.size() - 1);
		} else {
			throw new NoTransactionException("This account does not have transaction record.");
		}
	}

	public void setPrimaryCheuqingAccount(ChequingAccount acc) {
		this.primaryAccount = acc;
	}

	public void addAccount(Account acc) {
		this.accounts.add(acc);
		this.machine.addAccount(acc);
	}

	/**
	 * Save transaction t to the last index of transactions.
	 *
	 * @param t a new transaction
	 */
	public void addTransaction(Transaction t) {
		this.transactions.add(t);
	}


	public void sendNewAccountRequest(String accounttype) {
	}
}
