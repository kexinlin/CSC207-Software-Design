package model.persons;

import controller.BankSystem;
import model.Message;
import model.Request;
import model.accounts.*;
import model.exceptions.NoTransactionException;
import model.transactions.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class User implements Loginable {
	private String name;
	private String username;
	private BankSystem machine;
	private String password;
	private ArrayList<Account> accounts = new ArrayList<>();
	private ChequingAccount primaryAccount;
	private Date date;
	private ArrayList<Transaction> transactions = new ArrayList<>();
	private ArrayList<Message> messages = new ArrayList<>();

	public User(BankSystem bankSystem, String name, String username, String password) {
		this.machine = bankSystem;
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return password;
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

	public ChequingAccount getPrimaryChequingAccount() {
		return this.primaryAccount;
	}

	public void addAccount(Account acc) {
		this.accounts.add(acc);
	}

	/**
	 * Save transaction t to the last index of transactions.
	 *
	 * @param t a new transaction
	 */
	public void addTransaction(Transaction t) {
		this.transactions.add(t);
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message msg) {
		this.messages.add(msg);
	}

	public void removeMessage(Message msg) {
		this.messages.remove(msg);
	}
}
