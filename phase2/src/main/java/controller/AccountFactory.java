package controller;

import model.Money;
import model.persons.AccountOwner;
import model.transactors.*;

import java.util.Date;

public class AccountFactory {

	/**
	 * Constructs an account for a certain type
	 * @param type the type of account, in 3-char string.
	 * @param balance the balance of the account
	 * @param time the time of creation
	 * @param accountId the account id
	 * @param owner the owner
	 * @return the account created, or null if type is invalid
	 */
	public Account getAccount(String type, Money balance,
							  Date time, String accountId, AccountOwner owner) {
		switch (type) {
			case "chq":
			case "ChequingAccount":
			case "Chequing Account":
				return new ChequingAccount(balance, time, accountId, owner);
				
			case "sav":
			case "SavingAccount":
			case "Saving Account":
				return new SavingAccount(balance, time, accountId, owner);

			case "cre":
			case "CreditCardAccount":
			case "Credit Card Account":
				return new CreditCardAccount(balance, time, accountId, owner);

			case "loc":
			case "LineOfCreditAccount":
			case "Line Of Credit Account":
				return new LineOfCreditAccount(balance, time, accountId, owner);

			default:
				return null;
		}
	}

	/**
	 * Gets the type string for this account.
	 * @param account the account to get type.
	 * @return
	 */
	public String getAccountType(Account account) {
		if (account instanceof ChequingAccount) {
			return "chq";
		} else if (account instanceof SavingAccount) {
			return "sav";
		} else if (account instanceof CreditCardAccount) {
			return "cre";
		} else if (account instanceof LineOfCreditAccount) {
			return "loc";
		} else {
			return null;
		}
	}
}
