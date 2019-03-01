package atm;
import java.util.ArrayList;

public class BankManager implements Loginable {
	private ArrayList<User> users;	// stores all the users.
	/**
	 * Check if the password provided is the same as the one set for the user.
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return false;
	}

	public BankManager(){
		this.users = new ArrayList<User>();
	}

	public User createUser(String login, String password){
        User u = new User(login, password);
        this.users.add(u);
        return u;
    }

    public Object responseToRequest(){
		// input: Accept the request of creating the account or not
		if (false)
			return false;	// if not
		else{
			return createAccount();
		}
	}

	public Account createAccount(String accountType){
		if(accountType == "CreditCardsAccount"){
			//
		}
	}

    public boolean restockMachine(int faceValue, int number){
		return false;
    }

    public boolean undoTransaction(){
		return false;
    }
}
