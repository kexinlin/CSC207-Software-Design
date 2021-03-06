package model.persons;

import controller.BankSystem;
import model.ManagerAction;

public class BankManager implements Loginable, Employee {
	private String password;
	private String managerName;


	/**
	 * Construct a BankManager
	 *  @param username   The username of the bank manager
	 * @param password   the password of the bank manager
	 */
	public BankManager(String username, String password) {
		// TODO more secure way of storing password
		this.managerName = username;
		this.setPassword(password);
	}

	/**
	 * Get the password of the bank manager.
	 * @return the password of the bank manager
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
	 * Gets the username of this bank manager.
	 *
	 * @return the username of this bank manager.
	 */
	@Override
	public String getUsername() {
		return this.managerName;
	}

	/**
	 * Set the password of the bank manager
	 * @param password the password to change to.
	 * @return true when the operation succeed, false otherwise.
	 */
	@Override
	public boolean setPassword(String password) {
		this.password = password;
		return true;
	}

	@Override
	public boolean can(ManagerAction ignore) {
		return true;
	}

	@Override
	public String getType() { return "manager"; }
}
