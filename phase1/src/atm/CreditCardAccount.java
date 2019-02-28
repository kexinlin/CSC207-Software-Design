package atm;

public class CreditCardAccount extends DebtAccount {
	public CreditCardAccount() {

	}
	public double getBalance() {
		return 0;

	}

	/**
	 * Take money out of the credit card account.
	 * @param amount the amount of money to take out.
	 * @return false.
	 */
	@Override
	public boolean takeMoneyOut(double amount) {
		return false;
	}

	/**
	 * Put money into the credit card account.
	 * @param amount the amount of money to put in
	 * @return true if succeeds, false otherwise.
	 */
	@Override
	public boolean putMoneyIn(double amount) {
		return false;
	}
}
