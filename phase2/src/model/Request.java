package model;

import model.persons.User;

/**
 * A request sent by the user to bank managers to create an account.
 */
public class Request {
	private User user;
	private String accountType;
	private String msg;

	/**
	 * Initialize a request for the creation of account.
	 * "chq" represents ChequingAccount, "sav" represents SavingAccount
	 * "cre" represents CreditCardAccount, "loc" represents LineOfCreditAccount
	 *
	 * @param user the User who send request
	 * @param accountType the type of account User wants to create
	 * @param msg the message that the User sends to BankManager
	 */
	public Request(User user, String accountType, String msg){
		this.user = user;
		this.accountType = accountType;
		this.msg = msg;
	}

	/**
	 * Gets the user who sent this request.
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Gets the account type the user requested to create
	 * @return the type of account, in a 3-char string.
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * Gets the message sent to the bank manager.
	 * @return the message.
	 */
	public String getMsg() {
		return msg;
	}
}
