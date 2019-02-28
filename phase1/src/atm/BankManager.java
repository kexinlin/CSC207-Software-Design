package atm;

public class BankManager implements Loginable {
	/**
	 * Check if the password provided is the same as the one set for the user.
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return false;
	}

    public User createUser(String login, String password){
        User u = new User(login, password);
        return u;
    }

    public Account createAccount(){

	}

    public boolean restockMachine(int faceValue, int number){
		return false;
    }

    public boolean undoTransacation(){
		return false;
    }
}
