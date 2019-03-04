package atm;

import java.util.Date;

public class SavingAccount extends AssetAccount{
	public SavingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}
	@Override
	public boolean putMoneyIn(double amount) { return false; }

	@Override
	public boolean takeMoneyOut(double amount) { return false; }
}
