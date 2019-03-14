package model.transactions;

import model.accounts.Account;

import java.util.Date;

public class PayBillTransaction extends Transaction {
	private Account source;
	private String payeeName;

	/**
	 * Creates a PayBillTransaction.
	 * @param amount the amount to pay
	 * @param time the time of transaction
	 * @param sourceAcc the account from which the money is taken
	 * @param payeeName the payee's name
	 */
	public PayBillTransaction(double amount, Date time, Account sourceAcc, String payeeName) {
		super(amount, time);
		this.source = sourceAcc;
		this.payeeName = payeeName;
	}

	/**
	 * Gets the source account of this transaction.
	 * @return the account from which the money is taken
	 */
	public Account getSource() {
		return source;
	}

	/**
	 * Gets the payee's name
	 * @return payee's name
	 */
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * Gets a string representing this transaction, including the amount
	 * involved, source account,
	 * the payee's name, and the date of transaction.
	 * @return a string representing this transaction
	 */
	@Override
	public String toString() {
		return "Paid " + getAmount() + " from "
			+ getSource().getAccountId() + " to " + getPayeeName()
			+ " on " + getDateStr();
	}
}
