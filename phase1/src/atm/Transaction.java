package atm;

public class Transaction {
	private Account source;
	private Account destination;
	private float amount;
	
	public Transaction(Account source, Account destination, float amount) {
		this.source = source;
		this.destination = destination;
		this.amount = amount;

	}
}
