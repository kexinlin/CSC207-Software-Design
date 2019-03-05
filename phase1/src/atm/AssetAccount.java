package atm;

import java.util.Date;

public abstract class AssetAccount extends Account {
	public AssetAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}
}
