package atm;

import java.util.Collection;

public class ATM {
	private int cash5;
	private int cash10;
	private int cash20;
	private int cash50;	 // the number of the cashes.
	/**
	 * Constructs an instance of ATM.
	 */
	public ATM() {
		this.cash5 = 0;
		this.cash10 = 0;
		this.cash20 = 0;
		this.cash50 = 0;
	}

	/**
	 * Log in a user or admin.
	 * @param username the username of that person
	 * @param password the password of that person
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean login(User user, String username, String password) {
		return (username.equals(user.getUsername()) && user.verifyPassword(password));
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

	public boolean addCash(int denomination, int number){
		if (number < 0)
			return false;
		switch (denomination){
			case 5:
				this.cash5 += number;
				return true;
			case 10:
				this.cash10 += number;
				return true;
			case 20:
				this.cash20 += number;
				return true;
			case 50:
				this.cash50 += number;
				return true;
			default:
				return false;
		}
	}
}
