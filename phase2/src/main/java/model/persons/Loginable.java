package model.persons;

public interface Loginable {
	/**
	 * Gets the username of this individual.
	 * @return the username.
	 */
	String getUsername();
	/**
	 * Check if the argument passed is the password set for this individual.
	 * @param password the password to check.
	 * @return true if the password matches, false otherwise.
	 */
	boolean verifyPassword(String password);

	/**
	 * Change the password of this individual to `password`
	 * @param password the password to change to.
	 * @return true if the change succeeds, false otherwise.
	 */
	boolean setPassword(String password);

	String getPassword();
}
