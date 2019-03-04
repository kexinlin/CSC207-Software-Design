package atm;

import java.util.Collection;
import java.util.Date;

public class ATM {
	public static Date date;

	/**
	 * Constructs an instance of ATM.
	 */
	public ATM() {

	}

	/**
	 * Log in a user or admin.
	 * @param username the username of that person
	 * @param password the password of that person
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean login(String username, String password) {
		return false;//(username.equals(user.getUsername()) && user.verifyPassword(password));
	}

	/**
	 * Put cash into the machine.
	 * @param cash the collection of cash to put in
	 * @return true if the operation succeeds, false otherwise.
	 */
	public void depositCash(Collection<? extends Cash> cash) {


	}

	/**
	 * Take cash out of the machine.
	 * @param cash the collection of cash to take out
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean withdrawCash(Collection<? extends Cash> cash) {
		return false;
	}

	/**
	 * Gets the individual who is currently logged in.
	 * @return the individual who is logged in, or null if none.
	 */
	public Loginable currentLoggedIn() {
		return null;
	}

}
