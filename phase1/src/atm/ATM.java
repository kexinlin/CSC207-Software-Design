package atm;

import com.sun.tools.corba.se.idl.InterfaceGen;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ATM {
	static Date currentTime = new Date();
	ArrayList<User> userList;
	ArrayList<Account> accountList;
	private int totalCashSum = 0;
	HashMap<Cash, Integer> billAmount;

	/**
	 * Constructs an instance of ATM.
	 */
	public ATM() {
		/*int counter =0;
		File x = new File("Desktop:..");
		private Scanner x;
		try{
			x = new Scanner(x);
		}
		catch(Expection e){
			System.out.println("could not find file");
		}

		while(x.hasNext()){



		}


*/

	}


	/**
	 * Get current time of ATM.
	 *
	 * @return current time of ATM
	 */
	public static Date getCurrentTime() {
		return currentTime;
	}

	/**
	 * Set a new current time for this ATM.
	 *
	 * @param newTime The new time set for this ATM.
	 */
	public static void setCurrentTime(String newTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(newTime);
			long timeInMillis = date.getTime();
			currentTime = new Date(timeInMillis);
		} catch (ParseException e) {
		}
	}

	/**
	 * Get string representation of `currentTime` in this ATM.
	 *
	 * @return string representation of `currentTime` in this ATM
	 */
	public static String getCurrentTimeStr() {
		Date date = getCurrentTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}


	/**
	 * Log in a user or admin.
	 *
	 * @param username the username of that person
	 * @param password the password of that person
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean login(String username, String password) {
		for (User u : userList) {
			if (u.getUsername().equals(username)) {
				return u.getPassword().equals(password);
			}
		}
		return false;
	}

	/**
	 * Put cash into the machine.
	 *
	 * @param cash the collection of cash to put in
	 */
	public void depositCash(Collection<? extends Cash> cash) {
		// TODO: implement this
		File x = new File("Desktop:..");
		private Scanner x;
		try{
			x = new Scanner(x);
		}
		catch(Expection e){
			System.out.println("could not find file");
		}

		while(x.hasNext()){
			String toAccID = x.next();
			double value = double(x.next());




		}
		Account toAcc = getAccountById(toAccId);

		toAcc.putMoneyIn(value);
		Transaction newTrans = new Transaction(value);
		toAcc.balance += value;

		// add transaction record to both accounts
		toAcc.addTrans(newTrans);

		// add transaction record to both user
		toAcc.getOwner().addTransaction(newTrans);

		/*File y = new File("Desktop:..");
		private Scanner y;
		try{
			y = new Scanner(x);
		}
		catch(Expection e){
			System.out.println("could not find file");
		}

		while(y.hasNext()){



		}
		x = new Formatter("chinese.txt");
		x.format("%s%s",)
*/
	}


	/**
	 * Gets the individual who is currently logged in.
	 *
	 * @return the individual who is logged in, or null if none.
	 */
	public Loginable currentLoggedIn() {
		return null;
	}

	/**
	 * Transfer money from one account into another account.
	 * The two account may or may not belong to the same user.
	 *
	 * @param fromAccId `accountId` of the source account of transaction
	 * @param toAccId   `accountId` of the destination account of transaction
	 * @param amount    amount of money of transaction
	 * @throws AccountNotExistException  when account with the input id is not found
	 * @throws InvalidOperationException when attempting to transfer out from CreditCardAccount
	 * @throws NoEnoughMoneyException    when amount of money transferred out exceeds what is allowed
	 */
	public void transferMoney(String fromAccId, String toAccId, double amount)
		throws InvalidOperationException, NoEnoughMoneyException, AccountNotExistException {
		Account fromAcc = getAccountById(fromAccId);
		Account toAcc = getAccountById(toAccId);

		if (fromAcc instanceof CreditCardAccount) {
			throw new InvalidOperationException("Sorry, transfer money out of a " +
				"credit card account is not supported.");
		}

		fromAcc.takeMoneyOut(amount);
		toAcc.putMoneyIn(amount);
		Transaction newTrans = new TransferTransaction(amount, fromAcc, toAcc);

		// add transaction record to both accounts
		fromAcc.addTrans(newTrans);
		toAcc.addTrans(newTrans);

		// add transaction record to both user
		fromAcc.getOwner().addTransaction(newTrans);
		toAcc.getOwner().addTransaction(newTrans);

	}


	public void withdrawCash(String accId, HashMap<Cash, Integer> amountOfBill)
		throws AccountNotExistException, InsufficientCashException,  NoEnoughMoneyException {
		int totalAmount = Cash;
		// TODO: fix here

		if (totalAmount > totalCashSum) {
			throw new InsufficientCashException("Sorry, this ATM don't have that much cash " +
				"at this moment.");
		} else {
			Account acc = getAccountById(accId);
			acc.takeMoneyOut(totalAmount);
			Transaction newTrans = new WithdrawTransaction(totalAmount, acc);

			acc.addTrans(newTrans);
			acc.getOwner().addTransaction(newTrans);
		}

	}

	private void calculateBillAmount(HashMap<Cash, Integer> amountOfBill){
		int sum = 0;
	}


	private void deductCash(HashMap<Cash, Integer> amountOfBill) {

	}


	/**
	 * Get the Account object corresponding to the input Account id.
	 *
	 * @param id account id
	 * @return Account object corresponding to the input Account id.
	 * @throws AccountNotExistException when account with the input id is not found
	 */
	Account getAccountById(String id) throws AccountNotExistException {
		for (Account acc : accountList) {
			if (acc.getAccountId().equals(id)) {
				return acc;
			}
		}
		throw new AccountNotExistException("Sorry, can't find this account. " +
			"Please check your account number again.");
	}

	/**
	 * Proceed the transaction. Put it into transaction history. Deduct and
	 * Add fund to accounts.
	 * @param tx the transaction to proceed.
	 * @return true if succeeds, false otherwise.
	 */
	public boolean proceedTransaction(Transaction tx) {
		return false;
	}

}
