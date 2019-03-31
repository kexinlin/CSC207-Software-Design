package model.transactors;

import javafx.beans.property.SimpleObjectProperty;
import model.Money;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;
import model.transactions.Transaction;

import java.util.Date;

public abstract class DebtAccount extends Account {
	private Money debtLimit;
	private Money usedCredit;
	private double interestRate;
	/**
	 * Create an instance of account
	 * @param balance the balance of the account
	 *                  - this is the statement balance, updated every month on stmt day
	 *                  - users can put money in the account to pay their balance first
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId account id
	 * @param owner owner of the account
	 */
	DebtAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
		super(balance, dateOfCreation, accountId, owner);
		debtLimit = new Money(500);
		interestRate = 0.1;
		usedCredit = new Money(0);
	}

	public Money getUsedCredit() {
		return usedCredit;
	}

	public void setUsedCredit(Money m) {
		usedCredit = m;
	}

	public void setDebtLimit(Money m) {
		debtLimit = m;
	}

	public Money getDebtLimit() { return debtLimit; }

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public Money getStatementBalance() {
		return super.getBalance();
	}

	private void setStatementBalance(Money m) {
		super.setBalance(m);
	}

	/**
	 * current balance = statement balance + used credit
	 * @return cumulative balance
	 */
	@Override
	public Money getBalance() {
		return getStatementBalance().add(usedCredit);
	}

	private Money getCumulativeBalance() { return getBalance(); }

	/**
	 * Take money out of the credit card account.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(Money amount) throws NoEnoughMoneyException {
		if (this.getCumulativeBalance().add(amount).compareTo(debtLimit) <= 0) {
			usedCredit = usedCredit.add(amount);
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
		if (getStatementBalance().compareTo(amount) > 0) {
			// if the amount is not enough to cover the statement balance
			// pay to stmt balance first
			setStatementBalance(getStatementBalance().subtract(amount));
		} else {
			// the amount is enough to pay back all stmt balance
			// then subtract from used credit as well
			Money origStmtBalance = getStatementBalance();
			setStatementBalance(new Money(0));
			usedCredit = usedCredit.subtract(amount.subtract(origStmtBalance));
		}
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

	/**
	 * Gains interest if there is a positive balance.
	 * This should only be called by BankSystem's close(), on payment day.
	 */
	public void gainInterest() {
		if (getStatementBalance().compareTo(new Money(0)) > 0) {
			setStatementBalance(
				new Money(this.getStatementBalance().getMoneyValue()
					* (1 + interestRate)));
		}
	}

	/**
	 * Generate the account's statement.
	 * Add used credit to last month's statement balance, set it as the new stmt balance.
	 * This should only be called by BankSystem's close(), on statement day
	 */
	public void genStatement() {
		setStatementBalance(getStatementBalance().add(getUsedCredit()));
		setUsedCredit(new Money(0));
	}

	@Override
	public Money getFeeFor(Transaction tx) {
		Money cashAdvanceFee = new Money(5);
		Money free = new Money(0);
		if (! (tx.getDest() instanceof OutgoingBill)) {
			return cashAdvanceFee;
		}
		return free;
	}
}
