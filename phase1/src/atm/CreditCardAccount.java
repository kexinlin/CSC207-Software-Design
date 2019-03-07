package atm;

import java.util.Date;

public class CreditCardAccount extends DebtAccount {
	/**
	 * Create an instance of account
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public CreditCardAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	public double getBalance() {
		return this.balance;

	}

	/**
	 * Take money out of the credit card account.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(double amount) throws InvalidOperationException{
		throw new InvalidOperationException("Sorry, operation failed. " +
			"This is not a valid account.");
	}

	/**
	 * Put money into the credit card account.
	 *
	 * @param amount the amount of money to put in
	 */
	@Override
	public void putMoneyIn(double amount) {
		this.balance += amount;

	}

	/**
	 * Pay `amount` of money to a non-user account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 *
	 * @param nonUserAccount a non-user account represented by a String
	 * @param amount         the amount of bill
	 */
	public void payBill(String nonUserAccount, double amount) throws InvalidOperationException{
		// TODO
		throw new InvalidOperationException("Sorry, operation failed. " +
			"This is not a valid account.");
	}

}
