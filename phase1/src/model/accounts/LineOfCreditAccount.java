package model.accounts;

import model.exceptions.NoEnoughMoneyException;
import model.persons.User;

import java.util.Date;

public class LineOfCreditAccount extends DebtAccount {
	public LineOfCreditAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	@Override
	public void putMoneyIn(double amount) {
		this.balance -= amount;
	}

	@Override
	public void takeMoneyOut(double amount) {
		this.balance += amount;
	}

	@Override
	public void payBill(String nonUserAccount, double amount) throws NoEnoughMoneyException {
		this.balance += amount;
		super.payBill(nonUserAccount, amount);
	}
}
