package atm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ATM {
	Date currentTime = new Date();
	ArrayList<User> userList;
	ArrayList<Account> accountList;
	HashMap<Cash, Integer> billAmount = new HashMap<>();

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
	public Date getCurrentTime() {
		return this.currentTime;
	}


	/**
	 * Set a new current time for this ATM.
	 *
	 * @param newTime The new time set for this ATM.
	 */
	public void setCurrentTime(String newTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(newTime);
			long timeInMillis = date.getTime();
			this.currentTime = new Date(timeInMillis);
		} catch (ParseException e) {
		}
	}

	/**
	 * Get string representation of `currentTime` in this ATM.
	 *
	 * @return string representation of `currentTime` in this ATM
	 */
	public String getCurrentTimeStr() {
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
		/*
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
		// TODO: implement this
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


	/**
	 * @param accId          `accountId` input by the user
	 * @param amountWithdraw a HashMap that record the number of bills for each domination that
	 *                       the user wants to withdraw
	 * @throws AccountNotExistException  when account with the input id is not found
	 * @throws InsufficientCashException when the number of bills of certain domination is less
	 *                                   than the amount that the user wants to withdraw
	 * @throws NoEnoughMoneyException    when amount withdrawn from the account exceeds what is allowed
	 */
	public void withdrawCash(String accId, HashMap<Cash, Integer> amountWithdraw)
		throws AccountNotExistException, InsufficientCashException, NoEnoughMoneyException {

		ableToWithdraw(amountWithdraw);

		// calculate the total amount withdrawn
		int totalAmount = calculateTotalBillAmount(amountWithdraw);

		Account acc = getAccountById(accId); // account may not exist
		acc.takeMoneyOut(totalAmount); // money in account may not be enough

		deductCash(amountWithdraw);

		Transaction newTrans = new WithdrawTransaction(totalAmount, acc);
		acc.addTrans(newTrans);
		acc.getOwner().addTransaction(newTrans);

	}


	/**
	 * Calculate and return the total sum of money that the user wants to withdraw.
	 *
	 * @param amountOfBill a HashMap that record the number of bills for each domination that
	 *                     the user wants to withdraw
	 * @return the total sum of money that the user wants to withdraw
	 */
	private int calculateTotalBillAmount(HashMap<Cash, Integer> amountOfBill) {
		int sum = 0;
		for (Map.Entry<Cash, Integer> entry : amountOfBill.entrySet()) {
			Cash cash = entry.getKey();
			Integer num = entry.getValue();
			sum += cash.getNumVal() * num;
		}
		return sum;
	}


	/**
	 * Deduct the amount of cash that the user wants to withdraw from the ATM.
	 *
	 * @param amountWithdraw a HashMap that record the number of bills for each domination that
	 *                       the user wants to withdraw
	 */
	private void deductCash(HashMap<Cash, Integer> amountWithdraw) {
		for (Cash cash : amountWithdraw.keySet()) {
			int oldNum = this.billAmount.get(cash);
			int newNum = oldNum - amountWithdraw.get(cash);
			this.billAmount.put(cash, newNum);
		}
	}


	/**
	 * Check whether ATM has sufficient amount of bills for the user to withdraw from.
	 *
	 * @param amountWithdraw a HashMap that record the number of bills for each domination that
	 *                       the user wants to withdraw
	 * @throws InsufficientCashException when the number of bills of certain domination is less
	 *                                   than the amount that the user wants to withdraw
	 */
	private void ableToWithdraw(HashMap<Cash, Integer> amountWithdraw) throws InsufficientCashException {
		for (Cash cash : amountWithdraw.keySet()) {
			if (this.billAmount.get(cash) < amountWithdraw.get(cash)) {
				throw new InsufficientCashException("Sorry, this ATM does not have enough " +
					cash.getNumVal() + " dollar bills at this moment.");
			}
		}
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
