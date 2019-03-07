package atm;

import java.util.ArrayList;
import java.util.Date;
import java.security.SecureRandom;
//import java.text.SimpleDateFormat;

public class BankManager implements Loginable {
	private ArrayList<User> users;    // stores all the users.
	private ATM atm; // the atm that this BankManager manages
	private static final String NUMBERS = "0123456789";		// for random generates;
	private static SecureRandom rnd = new SecureRandom();	// for random
	private ArrayList<String> AccountIdDataBase; // stores all the Id number of accounts

	/**
	 * Check if the password provided is the same as the one set for the user.
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return false;
	}

	public BankManager(ATM atm) {
		this.atm = atm;
//		this.users = new ArrayList<User>();
		this.AccountIdDataBase = new ArrayList<String>();
	}

	public User createUser(String name, String username, String password) {
		User u = new User(name, username, password);

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
		atm.currentTime = new Date();
		return true;
	}
}
