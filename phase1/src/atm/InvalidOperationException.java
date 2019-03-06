package atm;

public class InvalidOperationException extends Exception {

	/**
	 * Construct a InvalidOperationException with a message.
	 */
	public InvalidOperationException(String msg) {
		super(msg);
	}
}
