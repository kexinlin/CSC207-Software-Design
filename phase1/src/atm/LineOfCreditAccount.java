package atm;

import java.util.Date;

public class LineOfCreditAccount extends DebtAccount {
	public LineOfCreditAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}

	@Override
	public void putMoneyIn(double amount) {
	}

	@Override
	public void takeMoneyOut(double amount) {
	}

	@Override
	public void payBill(String nonUserAccount, double amount) {
	}
}
