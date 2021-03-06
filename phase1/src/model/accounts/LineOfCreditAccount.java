package model.accounts;

import model.persons.User;

import java.util.Date;

public class LineOfCreditAccount extends DebtAccount {
	/**
	 * Create an instance of LineOfCreditAccount
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public LineOfCreditAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

}
