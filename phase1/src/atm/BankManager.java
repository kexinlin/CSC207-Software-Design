package atm;
import java.util.ArrayList;
import java.util.Date;

public class BankManager implements Loginable {
	private ArrayList<User> users;	// stores all the users.
	public static Date date;
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

    public Object responseToRequest(String accountType, User owner){
		// input: Accept the request of creating the account or not
		if (false)
			return false;	// if not
		else{
			return createAccount(accountType, owner);
		}
	}

	public Account createAccount(String accountType, User owner){
		String accountId = ".........."; 	//generated randomly??
		if(accountType == "CreditCardAccount"){
			return new CreditCardAccount(0, date, accountId, owner);
		}
		if(accountType == "LineOfCreditAccount"){
			return new LineOfCreditAccount(0, date, accountId, owner);
		}
		if(accountType == "CheuqingAccount"){
			return new ChequingAccount(0, date, accountId, owner);
		}
		if(accountType == "SavingAccount"){
			return new SavingAccount(0, date, accountId, owner);
		}
	}

    public boolean restockMachine(int faceValue, int number){
		return false;
    }

    public boolean undoTransaction(){
		return false;
    }
}
