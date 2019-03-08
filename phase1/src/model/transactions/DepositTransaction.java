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
}
