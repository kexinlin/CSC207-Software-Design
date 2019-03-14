package model.exceptions;

public class InsufficientCashException extends Exception {
	/**
	 * Construct a InsufficientCashException with a message.
	 */
	public InsufficientCashException(String msg){
		super(msg);
	}
}
