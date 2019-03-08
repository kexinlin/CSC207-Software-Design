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

	public Account getSource() {
		return source;
	}
}
