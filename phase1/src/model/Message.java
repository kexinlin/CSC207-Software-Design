package model;

import model.persons.User;

public class Message {
	private User user;
	private String text;
	/**
	 * Constructs a message.
	 * @param user the user for whom the message is for.
	 * @param text the text of the message.
	 */
	public Message(User user, String text) {
		this.user = user;
		this.text = text;
	}

	/**
	 * Gets the user for this message.
	 * @return the user.
	 */
	public User getUser() {
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
