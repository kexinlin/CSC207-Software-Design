package model.transactors;

import model.Money;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.exceptions.NoTransactionException;
import model.persons.AccountOwner;
import model.transactions.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class Account extends Transactor {
	Money balance;
	private Date dateOfCreation;
	private String accountId;
	private AccountOwner primaryOwner;
	private ArrayList<AccountOwner> coOwners;
	private ArrayList<Transaction> logs;

	/**
	 * Create an instance of account
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	Account(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
		this.balance = balance;
		this.dateOfCreation = dateOfCreation;
		this.accountId = accountId;
		this.primaryOwner = owner;
		this.coOwners = new ArrayList<>();
		this.logs = new ArrayList<>();
	}

	public ArrayList<AccountOwner> getCoOwners() {
		return coOwners;
	}

	public void addCoOwner(AccountOwner owner) {
		coOwners.add(owner);
	}

	/**
	 * Gets the balance of this account.
	 *
	 * @return balance of the account
	 */
	public Money getBalance() {
		return this.balance;
	}


	/**
	 * Gets the account id of this account.
	 *
	 * @return account id
	 */
	public String getId() {
		return accountId;
	}


	/**
	 * Gets the date of account creation.
	 *
	 * @return date of creation
	 */
	public Date getDateOfCreation() {
		return this.dateOfCreation;
	}


	/**
	 * Gets the owner of the account.
	 *
	 * @return the AccountOwner owner of the account
	 */
	public AccountOwner getOwner() {
		return primaryOwner;
	}


	/**
	 * Take `amount` of money out of the account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	public abstract void takeMoneyOut(Money amount) throws NoEnoughMoneyException, InvalidOperationException;


	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	public abstract void putMoneyIn(Money amount);

	/**
	 * Return whether `this.log` contains no transaction.
	 *
	 * @return return true when `this.log` contains no transaction
	 */
	private boolean logEmpty() {
		return this.logs.isEmpty();
	}


	/**
	 * Return the most recent Transaction.
	 * NoTransactionException will be throw if transaction record is empty.
	 *
	 * @return the most recently added Transaction into logs
	 */
	public Transaction getLastTrans() throws NoTransactionException {
		if (!logEmpty()) {
			return this.logs.get(logs.size() - 1);
		} else {
			throw new NoTransactionException("This account does not have transaction record.");
		}
	}

	/**
	 * Add a Transaction into `this.logs`.
	 *
	 * @param trans a Transaction by this AccountOwner
	 */
	public void addTrans(Transaction trans) {
		this.logs.add(trans);

	}

	/**
	 * Return the balance factor for this account.
	 * This value is 1 if a positive balance means the account holder
	 * has money while a negative balance means the holder owes money.
	 * It should be -1 otherwise.
	 *
	 * @return the balance factor of this account
	 */
	public abstract int balanceFactor();

	/**
	 *
	 * @return the AccountID
	 */
	public String getAccountId(){
		return accountId;
	}

	public abstract String getAccountType();
}
