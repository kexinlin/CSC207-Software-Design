package atm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

public class User implements Loginable {
	public String name;

	public String username;

	private String password;

	private ArrayList<CreditCardAccount> creditcards = new ArrayList<CreditCardAccount>();

	private ArrayList<ChequingAccount> cheuqing = new ArrayList<>();

	private ArrayList<LineOfCreditAccount> lineofcredit = new ArrayList<LineOfCreditAccount>();

	private ArrayList<SavingAccount> savings = new ArrayList<SavingAccount>();

	private ArrayList<Account> allAccount = new ArrayList<Account>();

	private ChequingAccount primaryaccount;

	public Date date;

	private ArrayList<Transaction> transactions = new ArrayList<>();




	public User(String name, String username, String password) {

		this.name = name;
		this.username = username;
		this.password = password;
	}

	public String getUsername(){
		return this.username;
	}




	/**
	 * Check if the password provided is the same as the one set for the user.
	 * @param password the password to check.
	 * @return true if password matches, false otherwise.
	 */
	@Override
	public boolean verifyPassword(String password) {
		return this.password.equals(password); // Can I change like this?
		//return false;
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
			total += acc.getBalance();
		}
		for (LineOfCreditAccount acc:lineofcredit){
			total -= acc.getBalance();
		}
		for (SavingAccount acc:savings){
			total += acc.getBalance();
		}
		return total;
	}




	public Date getDateOfCreation(){ return date; }




	/**
	 * return specific account by entering accountid
	 * @param accountid
	 * @return
	 */
	public Account getAccount(String accountid) {

		for (Account acc:allAccount) {
			if (acc.getAccountId() == accountid) {
				return acc;
			}
		}
		return null;
	}




	/**
	 * return all available accounts
	 * @return
	 */
	public ArrayList<Account> getAccounts(){ return allAccount; }




	public Transaction getMostRecentTransaction() { return transactions.get(-1); }




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


	/**
	 * save transaction t to the last index of transactions.
	 * @param t
	 */
	public void saveTransaction(Transaction t) { this.transactions.add(t); }




	public void sendNewAccountRequest(String accounttype){}
}
