package atm;

import java.util.Date;

public class ChequingAccount extends AssetAccount {
	/**
	 * Create an instance of ChequingAccount
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public ChequingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}


	/**
	 * Take `amount` of money out of the account.
	 * NoEnoughMoneyException will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(double amount) throws NoEnoughMoneyException {
		if (this.balance < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"Your balance in this account is negative.");
		} else if (this.balance - amount < -100) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"Your balance cannot decrease below -$100.");
		}
		this.balance -= amount;
	}


	/**
	 * Pay `amount` of money to a non-user account.
	 * Exception will be raised when the amount exceeds what is allowed.
	 * If succeeds, result will be recorded in outgoing.txt.
	 *
	 * @param nonUserAccount a non-user account represented by a String
	 * @param amount         the amount of bill
	 */
	@Override
	public void payBill(String nonUserAccount, double amount) throws NoEnoughMoneyException {
		takeMoneyOut(amount);
		super.payBill(nonUserAccount, amount);
	}
}
