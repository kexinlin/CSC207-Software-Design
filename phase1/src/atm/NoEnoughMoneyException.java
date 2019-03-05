package atm;

public class NoEnoughMoneyException extends Exception {
	/**
	 * Construct a NoEnoughMoneyException without a message info.
	 */
	public NoEnoughMoneyException() {
		super();
	}

	/**
	 * Construct a NoEnoughMoneyException with a message info.
	 */
	public NoEnoughMoneyException(String msg) {
		super(msg);

	}
}
