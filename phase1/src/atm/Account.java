package atm;

import java.util.Date;

public abstract class Account {
	private double balance;
	private Date dateOfCreation;
	private String accountId;
	private static int totalNumAccount = 1;


	public Account() {
		this.balance = 0;
		this.dateOfCreation = new Date();
		this.accountId = String.valueOf(totalNumAccount);
		totalNumAccount++;
	}

	public double getBalance() {
		return this.balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public Date getDateOfCreation(){
		return this.dateOfCreation;
	}

	// return boolean to indicate whether the action succeeds or not
	public abstract boolean withdrawMoney(double amount);

	public abstract boolean transferOut(Account destinationAccount);

	// increaseBalance always return true
	public abstract boolean increaseBalance(double amount);

	public abstract boolean payBill(String nonUserAccount);

}
