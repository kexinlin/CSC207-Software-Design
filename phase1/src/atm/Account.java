package atm;

import java.util.Date;

public abstract class Account {
	private double balance;
	private Date dateOfCreation;

	public Account() {
		this.balance = 0;
		this.dateOfCreation = new Date();
	}

	public double getBalance() {
		return this.balance;
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
