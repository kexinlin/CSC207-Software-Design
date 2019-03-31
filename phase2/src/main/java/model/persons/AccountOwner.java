package model.persons;

import model.Message;
import model.transactors.*;
import model.transactions.Transaction;

import java.util.ArrayList;

public abstract class AccountOwner implements Loginable {
	private String username;
	private String name;
	private String password;
	private ArrayList<Account> accounts = new ArrayList<>();
	private ChequingAccount primaryAccount;
	private ArrayList<Transaction> transactions = new ArrayList<>();
	private ArrayList<Message> messages = new ArrayList<>();
	private int age = 0;
	private int income = 0;
	private String email;

	/**
	 * Constructs a user object.
	 *
	 * @param username the username of this user
	 * @param password the password of this user
	 */
	public AccountOwner(String name, String username, String password) {
		this.username = username;
		this.name = name;
		this.password = password;

	}


	/**
	 * Constructs a user object with income, age, and email info
	 *
	 * @param username the username of this user
	 * @param password the password of this user
	 * @param age      the age of this user
	 * @param income   the income of this user
	 * @param email    the email address of this user
	 */
	public AccountOwner(String name, String username, String password, int age, int income, String email) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.age = age;
		this.income = income;
		this.email = email;

	}

	/**
	 * Get the username of this AccountOwner.
	 *
	 * @return user name of this AccountOwner
	 */

	public String getUsername() {
		return this.username;
	}

	/**
	 * Get the current password of this AccountOwner.
	 *
	 * @return the password of this AccountOwner
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
	 *
	 * @param password the password to change to.
	 * @return true if succeeds, false otherwise.
	 */
	public boolean setPassword(String password) {
		this.password = password;
		return true;
	}


	/**
	 * Gets the net balance of this user
	 *
	 * @return net balance
	 */
	public double getNetTotal() {
		return accounts.stream()
			.mapToDouble(acc -> acc.getBalance().getMoneyValue() * acc.balanceFactor())
			.sum();
	}


	/**
	 * return all available transactors
	 *
	 * @return all accounts
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}


	/**
	 * return sum of the balance of all asset accounts
	 *
	 * @return sum of the balance of all asset accounts
	 */
	public double getTotalAssets() {
		double total = 0;
		for (Account acc : accounts) {
			if (acc instanceof AssetAccount) {
				total += acc.getBalance().getMoneyValue();
			}
		}
		return total;
	}

	public String getName() {
		return name;
	}

	/**
	 * return sum of balance of all debt accounts
	 *
	 * @return return sum of balance of all debt accounts
	 */
	public double getTotalDebts() {
		double total = 0;
		for (Account acc : accounts) {
			if (acc instanceof DebtAccount) {
				total += acc.getBalance().getMoneyValue();
			}
		}
		return total;
	}


	/**
	 * Sets the primary chequing account for this user
	 *
	 * @param acc the chequing account to set as primary
	 */
	public void setPrimaryCheuqingAccount(ChequingAccount acc) {
		this.primaryAccount = acc;
	}

	/**
	 * Gets the primary chequing account for this user
	 *
	 * @return the primary chequing account, or null if the user does not have one
	 */
	public ChequingAccount getPrimaryChequingAccount() {
		return this.primaryAccount;
	}

	/**
	 * add account `acc` to this user
	 *
	 * @param acc the account to add
	 */
	public void addAccount(Account acc) {
		if (!this.accounts.contains(acc)) {
			this.accounts.add(acc);
		}
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
	 *
	 * @return an ArrayList of all messages
	 */
	public ArrayList<Message> getMessages() {
		return messages;
	}

	/**
	 * Add a message to the message list
	 *
	 * @param msg the message to add
	 */
	public void addMessage(Message msg) {
		this.messages.add(msg);
	}

	/**
	 * Remove a message from the message list
	 *
	 * @param msg the message to remove
	 */
	public void removeMessage(Message msg) {
		this.messages.remove(msg);
	}


	/**
	 * return annual income of this accountowner
	 *
	 * @return annual income of this accountowner
	 */
	public int getIncome() {
		return this.income;
	}


	/**
	 * return age of this accountowner
	 *
	 * @return age of this accountowner
	 */
	public int getAge() {
		return this.age;
	}


	public String getEmail() {
		return this.email;
	}
}
