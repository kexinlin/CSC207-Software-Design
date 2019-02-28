package atm;

import java.util.Date;

public class CreditCardAccount extends DebtAccount {
	/**
	 * Create an instance of account
	 * @param balance the balance of the account
	 * @param dateOfCreation the date of creation
	 * @param accountId account id
	 * @param owner owner of the account
	 */
	public CreditCardAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
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
