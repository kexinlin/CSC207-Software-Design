package atm;

public class WithdrawTransaction extends Transaction{
	private Account acc;

	/**
	 * Construct a new WithdrawTransaction.
	 *
	 * @param amount  the amount of withdrawal
	 * @param acc the source account of withdrawal
	 */
	WithdrawTransaction(double amount, Account acc) {
		super(amount);
		this.acc = acc;
	}

	/**
	 * Get the account that involves in this withdrawal.
	 *
	 * @return the account that involves in this withdrawal.
	 */
	public Account getAcc() {
		return acc;
	}
}
