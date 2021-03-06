package model.transactors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
	final private SimpleObjectProperty<Money> balance;
	final private SimpleObjectProperty<Date> dateOfCreation;
	final private SimpleStringProperty accountId;
	private AccountOwner primaryOwner;
	final private SimpleStringProperty accountType;
	private ArrayList<AccountOwner> coOwners;
	private ArrayList<Transaction> logs;
	private Money minimumBalance;

	/**
	 * Create an instance of account
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	Account(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
		this.balance = new SimpleObjectProperty<>(balance);
		this.dateOfCreation = new SimpleObjectProperty<>(dateOfCreation);
		this.accountId = new SimpleStringProperty(accountId);
		this.primaryOwner = owner;
		this.coOwners = new ArrayList<>();
		this.logs = new ArrayList<>();

		String[] typeClass = String.valueOf(this.getClass()).split("\\.");
		this.accountType = new SimpleStringProperty(typeClass[typeClass.length - 1]);
		this.minimumBalance = new Money(-1);
	}

	/**
	 * Create an instance of account
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	Account(Money balance, Date dateOfCreation, String accountId,
			AccountOwner owner, Collection<AccountOwner> coOwners) {
		this.balance = new SimpleObjectProperty<>(balance);
		this.dateOfCreation = new SimpleObjectProperty<>(dateOfCreation);
		this.accountId = new SimpleStringProperty(accountId);
		this.primaryOwner = owner;
		this.coOwners = new ArrayList<>(coOwners);
		this.logs = new ArrayList<>();

		String[] typeClass = String.valueOf(this.getClass()).split("\\.");
		this.accountType = new SimpleStringProperty(typeClass[typeClass.length - 1]);
		this.minimumBalance = new Money(-1);
	}

	/**
	 * Gets the balance of this account.
	 *
	 * @return balance of the account
	 */
	public Money getBalance() {
		return this.balance.getValue();
	}

	protected void setBalance(Money newBalance) {
		this.balance.setValue(newBalance);
	}

	public String getAccountType() {
		return accountType.getValue();
	}

	public String getAccountId() {
		return accountId.getValue();
	}

	public SimpleStringProperty accountIdProperty() {
		return accountId;
	}

	public SimpleStringProperty accountTypeProperty() {
		return accountType;
	}

	/**
	 * Gets the account id of this account.
	 *
	 * @return account id
	 */
	public String getId() {
		return accountId.getValue();
	}


	/**
	 * Gets the date of account creation.
	 *
	 * @return date of creation
	 */
	public Date getDateOfCreation() {
		return this.dateOfCreation.getValue();
	}


	/**
	 * Gets the owner of the account.
	 *
	 * @return the AccountOwner owner of the account
	 */
	public AccountOwner getOwner() {
		return primaryOwner;
	}

	public boolean isOwnedBy(AccountOwner owner) {
		return owner.equals(primaryOwner)
			|| getCoOwners().contains(owner);
	}

	public ArrayList<AccountOwner> getCoOwners() {
		return coOwners;
	}

	public void addCoOwner(AccountOwner owner) {
		if (!isOwnedBy(owner)) {
			coOwners.add(owner);
		}
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
	 * This function cannot be overrode by subclasses
	 * used to get the balance in the constructor.
	 * Used in record controller to get information.
	 *
	 * @return
	 */
	public final Money getActualBalance() {
		return balance.getValue();
	}

	/**
	 * Indicates whether the account is able to gain (positive) interest.
	 * In other words, if you put money into accounts which `hasInterest()`,
	 * you will end up with more money.
	 */
	public boolean hasInterest() {
		return false;
	}

	/**
	 * should be called only when hasInterest() returns true.
	 */
	public void increaseInterest() {
		throw new UnsupportedOperationException("This method is not defined");
	}

	public void setMinimumBalance(Money m) {
		if (hasInterest()) {
			minimumBalance = m;
		} else {
			throw new UnsupportedOperationException();
		}
	}
	public Money getMinimumBalance() {
		if (hasInterest()) {
			return minimumBalance;
		}
		throw new UnsupportedOperationException();
	}

	public void updateMinimumBalance() {
		if (getMinimumBalance().compareTo(new Money(0)) < 0) {
			// it is not yet set
			setMinimumBalance(getBalance());
		} else {
			setMinimumBalance(new Money(
				Math.min(getBalance().getMoneyValue(), getMinimumBalance().getMoneyValue())
			));
		}
	}

	public void resetMinimumBalance() {
		setMinimumBalance(new Money(-1));
		updateMinimumBalance();
	}
}
