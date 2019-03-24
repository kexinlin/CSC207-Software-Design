package model.transactors;

import model.Money;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;

import java.util.Date;

public class ChequingAccount extends AssetAccount {
	private Money maximumOverdraftLimit = new Money(-100);
	private Money beforeOverdraftLimit = new Money(0);
	/**
	 * Create an instance of ChequingAccount
	 *
	 * @param balance        the balance of the account
	 * @param dateOfCreation the currentTime of creation
	 * @param accountId      account id
	 * @param owner          owner of the account
	 */
	public ChequingAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner) {
		super(balance, dateOfCreation, accountId, owner);
	}


	/**
	 * Take `amount` of money out of the account.
	 * NoEnoughMoneyException will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 */
	@Override
	public void takeMoneyOut(Money amount) throws NoEnoughMoneyException {
		if (this.balance.compareTo(this.beforeOverdraftLimit) < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"Your balance in this account is negative.");
		} else if (this.balance.subtract(amount).compareTo(this.maximumOverdraftLimit) < 0) {
			throw new NoEnoughMoneyException("Sorry, operation failed. " +
				"Your balance cannot decrease below -$100.");
		}
		this.balance = this.balance.subtract(amount);
	}
}
