package model.transactors;

import javafx.beans.property.SimpleObjectProperty;
import model.Money;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;

import java.util.Date;

public abstract class DebtAccount extends Account {
	private Money debtLimit;
	/**
	 * Create an instance of account
	 * @param balance the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId account id
	 * @param owner owner of the account
	 */
	DebtAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
		super(balance, dateOfCreation, accountId, owner);
		debtLimit = new Money(500);
	}

	public void setDebtLimit(Money m) {
		debtLimit = m;
	}

	public Money getDebtLimit() { return debtLimit; }

	/**
	 * Take money out of the credit card account.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(Money amount) throws NoEnoughMoneyException {
		if (amount.compareTo(debtLimit) <= 0) {
			this.balance.setValue(this.balance.getValue().add(amount));
		} else {
			throw new NoEnoughMoneyException("The amount exceeds the debt limit.");
		}
	}

	/**
	 * Put money into the credit card account.
	 *
	 * @param amount the amount of money to put in
	 */
	@Override
	public void putMoneyIn(Money amount) {
		this.balance.setValue(this.balance.getValue().subtract(amount));

	}

	/**
	 * Return the balance factor for this account.
	 * This value is 1 if a positive balance means the account holder
	 * has money while a negative balance means the holder owes money.
	 * It should be -1 otherwise.
	 * @return the balance factor of this account
	 */
	@Override
	public int balanceFactor() {
		return -1;
	}
}
