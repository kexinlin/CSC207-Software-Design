package atm;

import java.util.Date;

public abstract class Transaction {
	private double amount;
	private Date transTime;
	private ATM atm;

	/**
	 * Construct a new TransferTransaction.
	 *
	 * @param amount the amount of transaction
	 */
	public Transaction(double amount) {
		this.amount = amount;
		this.atm = atm;
		this.transTime = atm.getCurrentTime();
	}

	public double getAmount() {
		return amount;
	}

	public Date getTransTime() {
		return transTime;
	}
}
