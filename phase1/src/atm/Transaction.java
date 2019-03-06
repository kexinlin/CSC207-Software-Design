package atm;

import java.util.Date;

public class Transaction {
	private double amount;
	private Date transTime;

	/**
	 * Construct a new TransferTransaction.
	 *
	 * @param amount the amount of transaction
	 */
	public Transaction(double amount) {
		this.amount = amount;
		this.transTime = ATM.getCurrentTime();
	}

	public double getAmount() {
		return amount;
	}

	public Date getTransTime() {
		return transTime;
	}
}
