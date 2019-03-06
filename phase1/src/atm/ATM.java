package atm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ATM {
	static Date currentTime = new Date();
	ArrayList<User> userList;
	ArrayList<Account> accountList;


	/**
	 * Constructs an instance of ATM.
	 */
	public ATM() {
	}


	/**
	 * Get current time of ATM.
	 *
	 * @return current time of ATM
	 */
	public static Date getCurrentTime() {
		return currentTime;
	}

	/**
	 * Set a new current time for this ATM.
	 *
	 * @param newTime The new time set for this ATM.
	 */
	public static void setCurrentTime(String newTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(newTime);
			long timeInMillis = date.getTime();
			currentTime = new Date(timeInMillis);
		} catch (ParseException e) {
		}
	}

	/**
	 * Get string representation of `currentTime` in this ATM.
	 *
	 * @return string representation of `currentTime` in this ATM
	 */
	public static String getCurrentTimeStr() {
		Date date = getCurrentTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}


	/**
	 * Log in a user or admin.
	 *
	 * @param username the username of that person
	 * @param password the password of that person
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean login(String username, String password) {
		for (User u : userList) {
			if (u.getUsername().equals(username)) {
				return u.getPassword().equals(password);
			}
		}
		return false;
	}

	/**
	 * Put cash into the machine.
	 *
	 * @param cash the collection of cash to put in
	 * @return true if the operation succeeds, false otherwise.
	 */
	public void depositCash(Collection<? extends Cash> cash) {

	}

	/**
	 * Take cash out of the machine.
	 *
	 * @param cash the collection of cash to take out
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean withdrawCash(Collection<? extends Cash> cash) {
		return false;
	}

	/**
	 * Gets the individual who is currently logged in.
	 *
	 * @return the individual who is logged in, or null if none.
	 */
	public Loginable currentLoggedIn() {
		return null;
	}

	// TODO: implement this method
	public void transferMoney(Account fromAcc, Account toAcc, double amount)
		throws InvalidOperationException, NoEnoughMoneyException {
		if (fromAcc instanceof CreditCardAccount) {
			throw new InvalidOperationException("Sorry, transfer money out of a " +
				"credit card account is not supported.");
		}

		fromAcc.takeMoneyOut(amount);
		toAcc.putMoneyIn(amount);
		Transaction newTrans = new Transaction(fromAcc, toAcc, amount);

	}

	public void createTransaction() {

	}


}
