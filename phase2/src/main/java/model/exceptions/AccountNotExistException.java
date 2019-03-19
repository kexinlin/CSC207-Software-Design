package model.exceptions;

public class AccountNotExistException extends Exception{
	/**
	 * Construct a AccountNotExistException with a message.
	 */
	public AccountNotExistException(String msg) {
		super(msg);
	}

}
