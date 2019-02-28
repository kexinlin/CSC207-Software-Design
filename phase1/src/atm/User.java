package atm;

public class User implements Loginable {
	private String login;
	private String password;
	/**
	 * Check if the password provided is the same as the one set for the user.
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return false;
	}

	public User(String login, String password){
		this.login = login;
		this.password = password;
	}
}
