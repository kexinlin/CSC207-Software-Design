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
	 * Constructs a user object with income, age, and email info
	 * @param username the username of this user
	 * @param password the password of this user
	 * @param age	the age of this user
	 * @param income the income of this user
	 * @param email the email address of this user
	 */
	public User(String name, String username, String password, int age, int income, String email) {
		super(username, password, age, income, email);
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
