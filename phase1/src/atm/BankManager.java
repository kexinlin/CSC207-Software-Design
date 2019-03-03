package atm;
import java.util.ArrayList;
import java.util.Date;
//import java.text.SimpleDateFormat;

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

	public User createUser(String name, String username, String password){
        User u = new User(name, username, password);
        this.users.add(u);
        return u;
    }

    public Object responseToRequest(String accountType, User owner){
		// input: Accept the request of creating the account or not
		if (false)	// communicate with UI.!!!
			return false;	// if not
		else{
			return createAccount(accountType, owner);
		}
	}

	public Account createAccount(String accountType, User owner){
		String accountId = ".........."; 	//generated randomly??
		if(accountType.equals("CreditCardAccount")){
			return new CreditCardAccount(0, ATM.date, accountId, owner);
		}
		if(accountType.equals("LineOfCreditAccount")){
			return new LineOfCreditAccount(0, ATM.date, accountId, owner);
		}
		if(accountType.equals("ChequingAccount")){
			return new ChequingAccount(0, ATM.date, accountId, owner);
		}
		if(accountType.equals("SavingAccount")){
			return new SavingAccount(0, ATM.date, accountId, owner);
		}
	}

    public boolean restockMachine(ATM theATM, int denomination, int number){
		return theATM.addCash(denomination, number);
	}

    public boolean undoTransaction(Account account){
		if (account.logEmpty())
			return false;
		else
			return account.undoTrans();
    }

    public boolean setTime(){
		//SimpleDateFormat dateForm = new SimpleDateFormat("Y/MM/dd HH:mm");
		ATM.date = new Date();
		return true;
	}
}
