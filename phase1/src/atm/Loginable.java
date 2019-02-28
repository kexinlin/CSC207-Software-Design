package atm;

public interface Loginable {
	/**
	 * Check if the argument passed is the password set for this individual.
	 * @param password the password to check.
	 * @return true if the password matches, false otherwise.
	 */
	boolean verifyPassword(String password);
}
