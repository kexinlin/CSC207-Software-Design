package atm;

import java.util.Date;

public class LineOfCreditAccount extends DebtAccount{
	public LineOfCreditAccount(double balance, Date dateOfCreation, String accountId, User owner) {
		super(balance, dateOfCreation, accountId, owner);
	}
}
