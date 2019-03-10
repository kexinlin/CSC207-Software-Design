package model.persons;

import model.Message;
import model.accounts.*;
import model.transactions.Transaction;

import java.util.ArrayList;

public class User implements Loginable {
	private String name;
	private String username;
	private String password;
	private ArrayList<Account> accounts = new ArrayList<>();
	private ChequingAccount primaryAccount;
	private ArrayList<Transaction> transactions = new ArrayList<>();
	private ArrayList<Message> messages = new ArrayList<>();

	/**
	 * Constructs a user object.
	 * @param name the full name of this user
	 * @param username the username of this user
	 * @param password the password of this user
	 */
	public User(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
	}

	/**
	 * Get the name of this User.
	 *
	 * @return name of this User
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the username of this User.
	 *
	 * @return user name of this User
	 */

	public String getUsername() {
		return this.username;
	}

	/**
	 * Get the current password of this User.
	 *
	 * @return the password of this User
	 */
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


	/**
	 * return all available accounts
	 *
	 * @return
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}


	/**
	 * Sets the primary chequing account for this user
	 * @param acc the chequing account to set as primary
	 */
	public void setPrimaryCheuqingAccount(ChequingAccount acc) {
		this.primaryAccount = acc;
	}

	/**
	 * Gets the primary chequing account for this user
	 * @return the primary chequing account, or null if the user does not have one
	 */
	public ChequingAccount getPrimaryChequingAccount() {
		return this.primaryAccount;
	}

	/**
	 * add account `acc` to this user
	 * @param acc the account to add
	 */
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

	/**
	 * get a list of all messages
	 * @return an ArrayList of all messages
	 */
	public ArrayList<Message> getMessages() {
		return messages;
	}

	/**
	 * Add a message to the message list
	 * @param msg the message to add
	 */
	public void addMessage(Message msg) {
		this.messages.add(msg);
	}

	/**
	 * Remove a message from the message list
	 * @param msg the message to remove
	 */
	public void removeMessage(Message msg) {
		this.messages.remove(msg);
	}
}
