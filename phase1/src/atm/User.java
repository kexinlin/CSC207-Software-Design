package atm;

import java.util.ArrayList;
import java.util.Date;

public class User implements Loginable {
	private String login;

	public String name;

	public String username;

	private String password;

	private ArrayList<CreditCardAccount> creditcards = new ArrayList<CreditCardAccount>();

	private ArrayList<ChequingAccount> cheuqing;

	private ArrayList<LineOfCreditAccount> lineofcredit = new ArrayList<LineOfCreditAccount>();

	private ArrayList<SavingAccount> savings = new ArrayList<SavingAccount>();

	private ArrayList<Account> allAccount = new ArrayList<Account>();

	private ChequingAccount primaryaccount;

	public Date date;



	public User(String name, String username, String password) {

		this.name = name;
		this.username = username;
		this.password = password;
	}



	

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





	/**
	 *
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String password){

		return password == this.password;
	}




	public void changePassword(String password){

		this.password = password;
	}




	public double getNetTotal(){

		double total = 0;
		for (CreditCardAccount acc:creditcards){
			total -= acc.getBalance();
		}
		for (ChequingAccount acc:cheuqing) {
			total += cheuqing.getBalance;
		}
		for (LineOfCreditAccount acc:lineofcredit){
			total -= acc.getBalance();
		}
		for (SavingAccount acc:savings){
			total += acc.getBalence();
		}
		return total;
	}



	public Date getDateOfCreation(){ return date; }




	public Account getAccount(String accountid) {

		for (Account acc:allAccount) {
			if (acc.getAccountId() == accountid) {
				return acc;
			}
		}
		return null;
	}





	public ArrayList<Account> getAccounts(){ return allAccount; }




	public Transaction getMostRecentTransaction(Account acc) {
		return acc.getMostRecentTransaction;
	}




	public void setPrimaryCheuqingAccount(ChequingAccount acc){
		this.primaryaccount = acc;
	}




	public void addAccount(ChequingAccount acc){
		this.cheuqing.add(acc);
	}



	public void addAccount(SavingAccount acc){
		this.savings.add(acc);
	}




	public void addAccount(LineOfCreditAccount acc){
		this.lineofcredit.add(acc);
	}



	public void addAccount(CreditCardAccount acc){
		this.creditcards.add(acc);
	}
}
