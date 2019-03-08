package model;

import model.persons.User;


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
	Request(User user, String accountType, String msg){
		this.user = user;
		this.accountType = accountType;
		this.msg = msg;
	}

	public User getUser() {
		return user;
	}

	public String getAccountType() {
		return accountType;
	}

	public String getMsg() {
		return msg;
	}
}
