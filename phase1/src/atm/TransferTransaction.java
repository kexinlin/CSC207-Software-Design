package atm;

public class TransferTransaction extends Transaction {
	private Account fromAcc;
	private Account toAcc;


	/**
	 * Construct a new TransferTransaction.
	 *
	 * @param amount  the amount of transaction
	 * @param fromAcc the source account of transaction
	 * @param toAcc   the destination account of transaction
	 */
	TransferTransaction(double amount, Account fromAcc, Account toAcc) {
		super(amount);
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
}
