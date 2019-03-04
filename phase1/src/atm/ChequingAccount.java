package atm;

import java.util.Date;

public class ChequingAccount extends AssetAccount {
	public ChequingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	/**
	 * Take `amount` of money out of the account.
	 *
	 * @param amount the amount of money to take out.
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean takeMoneyOut(double amount) {
		return false;
	}

	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean putMoneyIn(double amount) {
		return false;
	}



}
