package model.persons;

import controller.BankSystem;
import model.Request;
import model.exceptions.NoTransactionException;
import model.accounts.*;
import model.transactions.Transaction;
import java.util.Queue;
import java.util.LinkedList;

public class BankManager implements Loginable {
	private BankSystem bankSystem; // the view that this BankManager manages
	private String password;
	private String managerName;


	/**
	 * Construct a BankManager
	 *
	 * @param bankSystem The BankSystem.
	 * @param username   The username of the bank manager
	 * @param password   the password of the bank manager
	 */
	public BankManager(BankSystem bankSystem, String username, String password) {
		// TODO more secure way of storing password
		this.bankSystem = bankSystem;
		this.managerName = username;
		this.setPassword(password);
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
	 * Gets the username of this bank manager.
	 *
	 * @return the username of this bank manager.
	 */
	@Override
	public String getUsername() {
		return this.managerName;
	}

	@Override
	public boolean setPassword(String password) {
		this.password = password;
		return true;
	}

}
