package model.persons;

public class User extends AccountOwner {
	private String name;

	/**
	 * Constructs a user object.
	 * @param name the full name of this user
	 * @param username the username of this user
	 * @param password the password of this user
	 */
	public User(String name, String username, String password) {
		super(username, password);
		this.name = name;
	}

	/**
	 * Get the name of this AccountOwner.
	 *
	 * @return name of this AccountOwner
	 */
	public String getName() {
		return name;
	}
}
