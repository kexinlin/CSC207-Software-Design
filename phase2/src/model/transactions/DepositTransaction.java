package model.transactions;

import model.accounts.Account;

import java.util.Date;

public class DepositTransaction extends Transaction{
	private Account acc;

	/**
	 * Construct a new DepositTransaction.
	 *
	 * @param amount  the amount of transactions
	 * @param time the time of transaction
	 * @param acc the source account of transactions
	 */
	public DepositTransaction(double amount, Date time, Account acc) {
		super(amount, time);
		this.acc = acc;
	}

	/**
	 * Get the account that involves in this transactions.
	 *
	 * @return the account that involves in this transactions.
	 */
	public Account getAcc() {
		return acc;
	}

	/**
	 * Gets the string representation of this transaction, including
	 * the amount deposited and the destination account.
	 * @return the string representation of this transaction
	 */
	@Override
	public String toString() {
		return "Deposited " + getAmount() + " into "
			+ acc.getAccountId() + " on " + getDateStr();
	}
}
