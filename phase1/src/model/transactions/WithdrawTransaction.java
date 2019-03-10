package model.transactions;

import model.accounts.Account;

import java.util.Date;

public class WithdrawTransaction extends Transaction{
	private Account acc;

	/**
	 * Construct a new WithdrawTransaction.
	 *
	 * @param amount  the amount of withdrawal
	 * @param time the time of transaction
	 * @param acc the source account of withdrawal
	 */
	public WithdrawTransaction(double amount, Date time, Account acc) {
		super(amount, time);
		this.acc = acc;
	}

	/**
	 * Get the account that involves in this withdrawal.
	 *
	 * @return the account that involves in this withdrawal.
	 */
	public Account getAcc() {
		return acc;
	}

	@Override
	public String toString() {
		return "Withdrew " + getAmount() + " from " + getAcc().getAccountId()
			+ " on " + getDateStr();
	}
}
