package atm;

import java.util.Date;

public class SavingAccount extends AssetAccount {
	public SavingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	/**
	 * Take `amount` of money out of the account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(double amount) {
		this.balance -= amount;
		// TODO: raise Exception under certain situations
	}


	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	@Override
	public void putMoneyIn(double amount) {
		this.balance += amount;
	}


	/**
	 *
	 */
	@Override
	public void payBill(String nonUserAccount, double amount) {

	}
}
