package atm;

import java.util.ArrayList;
import java.util.Date;
import java.security.SecureRandom;
//import java.text.SimpleDateFormat;

public class BankManager implements Loginable {
	private ArrayList<User> users;    // stores all the users.
	private ATM atm; // the atm that this BankManager manages
	private String password;

	/**
	 * Construct a BankManager
	 * @param atm The ATM.
	 * @param username The username of the bank manager
	 * @param password the password of the bank manager
	 */
	public BankManager(ATM atm, String username, String password) {
		// TODO more secure way of storing password
		this.atm = atm;
		this.managerName = username;
		this.setPassword(password);
//		this.users = new ArrayList<User>();
	}

	private static final String NUMBERS = "0123456789";		// for random generates;
	private static SecureRandom rnd = new SecureRandom();	// for random
	private ArrayList<String> AccountIdDataBase; // stores all the Id number of accounts
	private String managerName;


	/**
	 * Check if the password provided is the same as the one set for the user.
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}

	/**
	 * Gets the username of this bank manager.
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

	public User createUser(String name, String username, String password) {
		User u = new User(atm, name, username, password);

		this.users.add(u);
		return u;
	}

	public Object responseToRequest(String accountType, User owner) {
		// input: Accept the request of creating the account or not
		if (false)    // communicate with UI.!!!
			return false;    // if not
		else {
			return createAccount(accountType, owner);
		}
	}

	public Account createAccount(String accountType, User owner) {
		String accountId = randomString(6);    //generated randomly??
		switch (accountType) {
			case "CreditCardAccount":
				return new CreditCardAccount(0, atm.getCurrentTime(), accountId, owner);

			case "LineOfCreditAccount":
				return new LineOfCreditAccount(0, atm.getCurrentTime(), accountId, owner);

			case "ChequingAccount":
				return new ChequingAccount(0, atm.getCurrentTime(), accountId, owner);

			case "SavingAccount":
				return new SavingAccount(0, atm.getCurrentTime(), accountId, owner);

			default:
				return null;
		}
	}


	private String randomString(int length){
		StringBuilder stringbuilder = new StringBuilder(length);
		do{
			for(int i=0;i<length;i++)
				stringbuilder.append(NUMBERS.charAt(rnd.nextInt(NUMBERS.length())));
			String temp = stringbuilder.toString();
			boolean flag = true;
			for(String str: this.AccountIdDataBase){
				if (str.equals(temp))
					flag = false;
			}
			if (flag)
				break;
		}while (true);
		this.AccountIdDataBase.add(stringbuilder.toString());
		return stringbuilder.toString();
	}

	//public boolean restockMachine(ATM theATM, int denomination, int number){
	//	return theATM.addCash(denomination, number);
	//}

	public void undoTransaction(Account account) throws NoTransactionException {
		Transaction trans = account.getLastTrans();
		// TODO: deal with money
	}

	public boolean setTime() {
		//SimpleDateFormat dateForm = new SimpleDateFormat("Y/MM/dd HH:mm");
		//atm.currentTime = new Date();
		return true;
	}
}
