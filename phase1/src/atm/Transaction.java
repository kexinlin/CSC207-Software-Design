package atm;

public class Transaction {
	private Account source;
	private Account destination;
	private double amount;
	
	public Transaction(Account source, Account destination, double amount) {
		this.source = source;
		this.destination = destination;
		this.amount = amount;

	}
}
