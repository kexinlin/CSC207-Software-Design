package atm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

public class User implements Loginable {
	public String name;

	public String username;

	private String password;

	private ArrayList<CreditCardAccount> creditcards = new ArrayList<CreditCardAccount>();

	private ArrayList<ChequingAccount> chequing = new ArrayList<>();

	private ArrayList<LineOfCreditAccount> lineofcredit = new ArrayList<LineOfCreditAccount>();

	private ArrayList<SavingAccount> savings = new ArrayList<SavingAccount>();

	private ArrayList<Account> allAccount = new ArrayList<Account>();

	private ChequingAccount primaryaccount;

	public Date date;

	private ArrayList<Transaction> transactions = new ArrayList<>();


	public User(String name, String username, String password) {

		this.name = name;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
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
		return this.password.equals(password); // Can I change like this?
		//return false;
	}


	/**
	 * Change the password of this user to `password`.
	 * @param password the password to change to.
	 * @return true if succeeds, false otherwise.
	 */
	public boolean setPassword(String password) {
		this.password = password;
		return true;
	}


	public double getNetTotal() {

		double total = 0;
		for (CreditCardAccount acc : creditcards) {
			total -= acc.getBalance();
		}
		for (ChequingAccount acc : chequing) {
			total += acc.getBalance();
		}
		for (LineOfCreditAccount acc : lineofcredit) {
			total -= acc.getBalance();
		}
		for (SavingAccount acc : savings) {
			total += acc.getBalance();
		}
		return total;
	}


	public Date getDateOfCreation() {
		return date;
	}


	/**
	 * return specific account by entering accountid
	 *
	 * @param accountId
	 * @return
	 */
	public Account getAccount(String accountId) {

		for (Account acc : allAccount) {
			if (acc.getAccountId().equals(accountId)) {
				return acc;
			}
		}
		return null;
	}


	/**
	 * return all available accounts
	 *
	 * @return
	 */
	public ArrayList<Account> getAccounts() {
		return allAccount;
	}

	public boolean logEmpty() {
		return this.transactions.isEmpty();
	}

	public Transaction getMostRecentTransaction() throws NoTransactionException {
		if (!logEmpty()) {
			return this.transactions.get(transactions.size() - 1);
		} else {
			throw new NoTransactionException("This account does not have transaction record.");
		}
	}

	public void setPrimaryCheuqingAccount(ChequingAccount acc) {
		this.primaryaccount = acc;
	}

	public void addAccount(ChequingAccount acc) {
		this.chequing.add(acc);
	}

	public void addAccount(SavingAccount acc) {
		this.savings.add(acc);
	}

	public void addAccount(LineOfCreditAccount acc) {
		this.lineofcredit.add(acc);
	}

	public void addAccount(CreditCardAccount acc) {
		this.creditcards.add(acc);
	}


	/**
	 * Save transaction t to the last index of transactions.
	 *
	 * @param t a new transaction
	 */
	public void addTransaction(Transaction t) {
		this.transactions.add(t);
	}


	public void sendNewAccountRequest(String accounttype) {
	}
}
