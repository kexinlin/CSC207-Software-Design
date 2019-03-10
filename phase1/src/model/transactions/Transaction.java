package model.transactions;

import java.text.SimpleDateFormat;
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
	Transaction(double amount, Date time) {
		this.amount = amount;
		this.transTime = time;
	}

	public double getAmount() {
		return amount;
	}

	public Date getDate() {
		return transTime;
	}

	String getDateStr() {
		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
		return format.format(getDate());
	}
}
