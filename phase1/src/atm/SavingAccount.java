package atm;

import java.util.Date;

public class SavingAccount extends AssetAccount{
	public SavingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}
}
