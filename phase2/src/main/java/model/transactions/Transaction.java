package model.transactions;

import model.FeeCalculator;
import model.Money;
import model.NormalFeeCalculator;
import model.transactors.Transactor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
	private Money amount;
	private Date transTime;
	private Transactor source, dest;
	private String comment;
	private Money fee;

	/**
	 * Construct a new Transaction.
	 *
	 * @param amount the amount of transaction
	 * @param time the time of transaction
	 * @param source the dealer where the money comes from
	 * @param dest the dealer where the money goes to
	 */
	public Transaction(Money amount, Date time, Transactor source, Transactor dest) {
		this.amount = amount;
		this.transTime = time;
		this.source = source;
		this.dest = dest;
		this.comment = "";
		FeeCalculator calc = new NormalFeeCalculator();
		this.fee = calc.getFee(this);
	}

	public Transaction(Money amount, Date time, Transactor source, Transactor dest, Money fee) {
		this.amount = amount;
		this.transTime = time;
		this.source = source;
		this.dest = dest;
		this.comment = "";
		this.fee = fee;
	}


	public Transaction(Money amount, Date time, Transactor source, Transactor dest, String comment) {
		this.amount = amount;
		this.transTime = time;
		this.source = source;
		this.dest = dest;
		this.comment = comment;
		FeeCalculator calc = new NormalFeeCalculator();
		this.fee = calc.getFee(this);
	}

	/**
	 * Gets the amount of money involved in the transaction
	 * @return the amount of money involved in the transaction.
	 */
	public Money getAmount() {
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

	/**
	 * Gets the source for this transaction.
	 * @return the source for this transaction.
	 */
	public Transactor getSource() {
		return source;
	}

	/**
	 * Gets the destination for this transaction.
	 * @return the destination for this transaction.
	 */
	public Transactor getDest() {
		return dest;
	}

	public Money getFee() {
		return fee;
	}

	/**
	 * Gets a human-readable representation of this transaction, including
	 * the amount, the source and destination account and transaction date
	 * @return a string representing the transaction
	 */
	@Override
	public String toString() {
		return "Transferred " + getAmount() + " from " + getSource().getId()
			+ " to " + getDest().getId() + " on " + getDateStr()
			+ (comment.length() > 0 ? "for " + comment : "");
	}
}
