package model.transactions;

import java.util.Date;

public abstract class Transaction {
	private double amount;
	private Date transTime;

	/**
	 * Construct a new TransferTransaction.
	 *
	 * @param amount the amount of transaction
	 * @param time the time of transaction
	 */
	public Transaction(double amount, Date time) {
		this.amount = amount;
		this.transTime = time;
	}

	public double getAmount() {
		return amount;
	}

	public Date getDate() {
		return transTime;
	}
}
