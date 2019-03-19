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

	/**
	 * Gets the amount of money involved in the transaction
	 * @return the amount of money involved in the transaction.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Gets the date of transaction.
	 * @return date of transaction.
	 */
	public Date getDate() {
		return transTime;
	}

	/**
	 * Gets a string-represented date of transaction.
	 * @return a String containing the date of transaction.
	 */
	String getDateStr() {
		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
		return format.format(getDate());
	}
}
