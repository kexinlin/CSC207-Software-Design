package atm;

public class NoTransactionException extends Exception {
	/**
	 * Construct a NoEnoughMoneyException without a message info.
	 */
	public NoTransactionException() {
		super();
	}

	/**
	 * Construct a NoEnoughMoneyException with a message info.
	 */
	public NoTransactionException(String msg) {
		super(msg);

	}
}
