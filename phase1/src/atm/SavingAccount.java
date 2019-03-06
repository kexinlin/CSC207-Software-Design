package atm;

import java.util.Date;

public class SavingAccount extends AssetAccount {
	private static final double INTEREST = 0.1;

	/**
	 * Create an instance of SavingAccount
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public SavingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	/**
	 * Take `amount` of money out of the account.
	 * NonEnoughMoneyException will also be raised if the amount will result in a negative balance.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(double amount) throws NoEnoughMoneyException {
		if (this.balance - amount < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"The amount exceeds existing balance in this account");
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


	/**
	 * Increase the saving account balance bt a factor of 0.1%.
	 */
	public void increasInterest() {
		this.balance *= (1 + INTEREST / 100);
	}
}
