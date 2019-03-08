package controller;

import controller.deposit.CashController;
import model.*;
import model.accounts.*;
import model.exceptions.AccountNotExistException;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.BankManager;
import model.persons.Loginable;
import model.persons.User;
import model.transactions.Transaction;
import model.transactions.TransferTransaction;
import model.transactions.WithdrawTransaction;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankSystem {
	private Date currentTime;
	private HashMap<String, Loginable> loginables;
	private HashMap<String, Account> accounts;
	private Loginable loggedIn;
	private String recordFileName;
	private ArrayList<Request> requests;

	private static final String NUMBERS = "0123456789";  // for randomly generating accountId;
	private static SecureRandom rnd = new SecureRandom(); // for randomly generating accountId;

	/**
	 * Constructs an instance of BankSystem.
	 */
	public BankSystem(String recordFileName) {
		this.currentTime = new Date();

		loggedIn = null;

		this.loginables = new HashMap<>();
		this.accounts = new HashMap<>();

		this.recordFileName = recordFileName;

		readRecordsFromFile();
	}

	/**
	 * read records from file.
	 */
	private void readRecordsFromFile() {
		// FIXME replace this with actual file-reading
		this.loginables.put("mgr1", new BankManager(this, "mgr1", "lolol"));
		User foobar = new User(this, "Foo Bar", "u1", "xxx");
		Account chq = new ChequingAccount(0, new Date(), "127", foobar);
		foobar.addAccount(chq);
		this.loginables.put("u1", foobar);

	}

	/**
	 * save records to file.
	 */
	private void saveRecordsToFile() {
		// TODO
	}

	public void close() {
		saveRecordsToFile();
	}

	/**
	 * Get current time of BankSystem.
	 *
	 * @return current time of BankSystem
	 */
	public Date getCurrentTime() {
		return this.currentTime;
	}


	/**
	 * Set a new current time for this BankSystem.
	 *
	 * @param newTime The new time set for this BankSystem.
	 */
	public void setCurrentTime(String newTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(newTime);
			long timeInMillis = date.getTime();
			this.currentTime = new Date(timeInMillis);
		} catch (ParseException e) {
		}
	}

	/**
	 * Get string representation of `currentTime` in this BankSystem.
	 *
	 * @return string representation of `currentTime` in this BankSystem
	 */
	public String getCurrentTimeStr() {
		Date date = getCurrentTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	/**
	 * Get the user or admin with `username`
	 *
	 * @param username the username of wanted person
	 * @return a `Loginable` corresponding to that person, or null if not found
	 */
	public Loginable getLoginable(String username) {
		return loginables.getOrDefault(username, null);
	}

	/**
	 * Create a new User and save into `this.loginables`.
	 *
	 * @param name     the name of the user
	 * @param username the username of the user
	 * @param password the password of the user
	 * @throws InvalidOperationException when the username is already taken
	 */
	public void createUser(String name, String username, String password) throws InvalidOperationException {
		if (this.loginables.containsKey(username)) {
			throw new InvalidOperationException("Sorry, " +
				"your username is already taken. Please try another one.");
		}
		User u = new User(this, name, username, password);

		this.loginables.put(username, u);
	}

	/**
	 * Randomly generate an `accountId`.
	 *
	 * @param length the length of the String generated
	 * @return the generated String
	 */
	private String randomString(int length) {
		StringBuilder stringbuilder = new StringBuilder(length);
		do {
			for (int i = 0; i < length; i++)
				stringbuilder.append(NUMBERS.charAt(rnd.nextInt(NUMBERS.length())));
			String temp = stringbuilder.toString();
			boolean flag = !accounts.containsKey(temp);
			if (flag)
				break;
		} while (true);
		return stringbuilder.toString();
	}

	/**
	 * @return a ArrayList that contain all the requests for creating accounts from User,
	 * which haven't yet been verified by BankManager
	 */
	public ArrayList<Request> getRequests() {
		return requests;
	}


	/**
	 * Create an account based on the input Request.
	 *
	 * @param request a request from a User containing the information for creating an account,
	 *                which has been verified by a BankManager.
	 */
	public void createAccount(Request request) {
		User owner = request.getUser();
		String accountType = request.getAccountType();
		Account newAccount;

		String accountId = randomString(6);   //generated randomly
		switch (accountType) {
			case "chq":
				newAccount = new CreditCardAccount(0, getCurrentTime(), accountId, owner);
				break;
			case "sav":
				newAccount = new SavingAccount(0, getCurrentTime(), accountId, owner);
				break;
			case "cre":
				newAccount = new CreditCardAccount(0, getCurrentTime(), accountId, owner);
				break;
			case "loc":
				newAccount = new LineOfCreditAccount(0, getCurrentTime(), accountId, owner);
				break;
			default:
				newAccount = null;
				break;
		}
		owner.addAccount(newAccount);
		accounts.put(accountId, newAccount);
	}

	public void undoTransaction() {
		//TODO
	}

	/**
	 * Transfer money from one account into another account.
	 * The two account may or may not belong to the same user.
	 *
	 * @param fromAcc the source account of transaction
	 * @param toAcc   the destination account of transaction
	 * @param amount  amount of money of transaction
	 * @throws InvalidOperationException when attempting to transfer out from CreditCardAccount
	 * @throws NoEnoughMoneyException    when amount of money transferred out exceeds what is allowed
	 */
	public void transferMoney(Account fromAcc, Account toAcc, double amount)
		throws InvalidOperationException, NoEnoughMoneyException {

		if (fromAcc instanceof CreditCardAccount) {
			throw new InvalidOperationException("Sorry, transfer money out of a " +
				"credit card account is not supported.");
		}

		fromAcc.takeMoneyOut(amount);
		toAcc.putMoneyIn(amount);
		Transaction newTrans = new TransferTransaction(amount, getCurrentTime(), fromAcc, toAcc);

		// add transaction record to both accounts
		fromAcc.addTrans(newTrans);
		toAcc.addTrans(newTrans);

		// add transaction record to both user
		fromAcc.getOwner().addTransaction(newTrans);
		toAcc.getOwner().addTransaction(newTrans);

	}

	/**
	 * Get the Account object corresponding to the input Account id.
	 *
	 * @param id account id
	 * @return Account object corresponding to the input Account id.
	 * @throws AccountNotExistException when account with the input id is not found
	 */
	public Account getAccountById(String id) throws AccountNotExistException {
		if (!accounts.containsKey(id)) {
			throw new AccountNotExistException("Sorry, can't find this account. " +
				"Please check your account number again.");
		}
		return accounts.get(id);
	}

	/**
	 * Add the account `acc`
	 *
	 * @param acc the account to add
	 */
	public void addAccount(Account acc) {
		accounts.put(acc.getAccountId(), acc);
	}
}
