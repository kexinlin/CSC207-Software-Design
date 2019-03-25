package model.transactors;

import model.Money;
import model.persons.AccountOwner;

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
	public LineOfCreditAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	/**
	 * @return the type of the account
	 */
	@Override
	public String getAccountType(){
		return "LineOfCreditAccount";
	}
}
