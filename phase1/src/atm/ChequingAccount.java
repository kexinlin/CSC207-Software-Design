package atm;

import java.util.Date;

public class ChequingAccount extends AssetAccount{
	public ChequingAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}
}
