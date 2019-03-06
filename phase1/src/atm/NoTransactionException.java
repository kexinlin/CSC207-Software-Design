package atm;

public class NoTransactionException extends Exception {
	/**
	 * Construct a NoEnoughMoneyException with a message.
	 */
	public NoTransactionException(String msg) {
		super(msg);

	}
}
