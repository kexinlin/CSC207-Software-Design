package atm;

public class DepositTransaction extends Transaction{
	private Account acc;

	/**
	 * Construct a new DepositTransaction.
	 *
	 * @param amount  the amount of deposit
	 * @param acc the source account of deposit
	 */
	DepositTransaction(double amount, Account acc) {
		super(amount);
		this.acc = acc;
	}

	/**
	 * Get the account that involves in this deposit.
	 *
	 * @return the account that involves in this deposit.
	 */
	public Account getAcc() {
		return acc;
	}
}
