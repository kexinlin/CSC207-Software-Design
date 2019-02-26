package atm;

public abstract class Account {
	private double balance;

	public Account() {
		this.balance = 0;
	}

	public double getBalance() {
		return this.balance;
	}

	// return boolean to indicate whether the action succeeds or not
	public abstract boolean withdrawMoney(double amount);

	public abstract boolean transferOut(Account destinationAccount);

	// increaseBalance always return true
	public abstract boolean increaseBalance(double amount);

	public abstract boolean payBill(String nonUserAccount);

}
