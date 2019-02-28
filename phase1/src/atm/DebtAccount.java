package atm;

import java.util.Date;

public abstract class DebtAccount extends Account {
	/**
	 * Create an instance of account
	 * @param balance the balance of the account
	 * @param dateOfCreation the date of creation
	 * @param accountId account id
	 * @param owner owner of the account
	 */
	public DebtAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}
}
