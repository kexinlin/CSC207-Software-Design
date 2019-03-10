package model.transactions;

import model.accounts.Account;

import java.util.Date;

public class TransferTransaction extends Transaction {
	private Account fromAcc;
	private Account toAcc;


	/**
	 * Construct a new TransferTransaction.
	 *
	 * @param amount  the amount of transaction
	 * @param time the date of transaction
	 * @param fromAcc the source account of transaction
	 * @param toAcc   the destination account of transaction
	 */
	public TransferTransaction(double amount, Date time, Account fromAcc, Account toAcc) {
		super(amount, time);
		this.fromAcc = fromAcc;
		this.toAcc = toAcc;
	}

	/**
	 * Get the source account of this transaction.
	 *
	 * @return the source account of this transaction
	 */
	public Account getFromAcc() {
		return fromAcc;
	}

	/**
	 * Get the destination account of this transaction.
	 *
	 * @return the destination account of this transaction
	 */
	public Account getToAcc() {
		return toAcc;
	}

	/**
	 * Gets a human-readable representation of this transaction, including
	 * the amount, the source and destination account and transaction date
	 * @return a string representing the transaction
	 */
	@Override
	public String toString() {
		return "Transferred " + getAmount() + " from " + fromAcc.getAccountId()
			+ " to " + toAcc.getAccountId() + " on " + getDateStr();
	}
}
