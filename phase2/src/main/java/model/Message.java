package model;

import model.persons.AccountOwner;

/**
 * A message that bank managers can send to users to inform them about something,
 * such as denial of account creation.
 */
public class Message {
	private AccountOwner user;
	private String text;
	/**
	 * Constructs a message.
	 * @param user the user for whom the message is for.
	 * @param text the text of the message.
	 */
	public Message(AccountOwner user, String text) {
		this.user = user;
		this.text = text;
	}

	/**
	 * Gets the user for this message.
	 * @return the user.
	 */
	public AccountOwner getUser() {
		return user;
	}

	/**
	 * Gets the text for this message.
	 * @return the text.
	 */
	public String getText() {
		return text;
	}
}
