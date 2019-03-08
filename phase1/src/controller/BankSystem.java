package controller;

import controller.deposit.CashController;
import model.*;
import model.accounts.Account;
import model.accounts.ChequingAccount;
import model.accounts.CreditCardAccount;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankSystem {
	private Date currentTime;
	private ArrayList<Loginable> loginables;
	private ArrayList<Account> accounts;
	private Loginable loggedIn;
	private String recordFileName;

	/**
	 * Constructs an instance of BankSystem.
	 */
	public BankSystem(String recordFileName) {
		this.currentTime = new Date();

		loggedIn = null;

		this.loginables = new ArrayList<>();
		this.accounts = new ArrayList<>();

		this.recordFileName = recordFileName;

		readRecordsFromFile();
	}

	/**
	 * read records from file.
	 */
	private void readRecordsFromFile() {
		// FIXME replace this with actual file-reading
		this.loginables.add(new BankManager(this, "mgr1", "lolol"));
		User foobar = new User(this, "Foo Bar", "u1", "xxx");
		Account chq = new ChequingAccount(0, new Date(), "127", foobar);
		foobar.addAccount(chq);
		this.loginables.add(foobar);

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
	 * @param username the username of wanted person
	 * @return a `Loginable` corresponding to that person, or null if not found
	 */
	public Loginable getLoginable(String username) {
		for (Loginable curPerson : loginables) {
			if (curPerson.getUsername().equals(username)) {
				return curPerson;
			}
		}
		return null;
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
		for (Account acc : accounts) {
			if (acc.getAccountId().equals(id)) {
				return acc;
			}
		}
		throw new AccountNotExistException("Sorry, can't find this account. " +
			"Please check your account number again.");
	}

	/**
	 * Add the account `acc`
	 * @param acc the account to add
	 */
	public void addAccount(Account acc) {
		accounts.add(acc);
	}
}
