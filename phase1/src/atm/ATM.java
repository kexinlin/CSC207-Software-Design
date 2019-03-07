package atm;

import java.io.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ATM {
	private Date currentTime;
	private ArrayList<Loginable> loginables;
	private ArrayList<Account> accounts;
	private HashMap<Cash, Integer> billAmount;
	private static final String DEPOSIT_FILE_NAME = "deposits.txt";
	private Loginable loggedIn;

	/**
	 * Constructs an instance of ATM.
	 */
	public ATM() {
		this.currentTime = new Date();
		this.billAmount = new HashMap<>();
		billAmount.put(Cash.FIVE, 0);
		billAmount.put(Cash.TEN, 0);
		billAmount.put(Cash.TWENTY, 0);
		billAmount.put(Cash.FIFTY, 0);
		billAmount.put(Cash.HUNDRED, 0);

		loggedIn = null;

		this.loginables = new ArrayList<>();
		this.accounts = new ArrayList<>();

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
	 * Get the user or admin with `username`
	 * @param username the username of wanted person
	 * @return a `Loginable` corresponding to that person, or null if not found
	 */
	public Loginable getLoginable(String username) {
		for (Loginable curPerson : loginables) {
			if (curPerson.getUsername().equals(username)) {
				return curPerson;
			}
		}
		return null;
	}

	/**
	 * Log in a user or admin.
	 *
	 * @param username the username of that person
	 * @param password the password of that person
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean login(String username, String password) {
		Loginable person = getLoginable(username);
		if (person != null && person.verifyPassword(password)) {
			loggedIn = person;
			return true;
		}
		return false;
	}

//	/**
//	 * Put cash into the machine.
//	 *
//	 * @param cash the collection of cash to put in
//	 */
//	public void depositCash(Collection<? extends Cash> cash) {
//
//
//		/*
//		File x = new File("Desktop:..");
//		private Scanner x;
//		try{
//			x = new Scanner(x);
//		}
//		catch(Expection e){
//			System.out.println("could not find file");
//		}
//
//		while(x.hasNext()){
//			String toAccID = x.next();
//			double value = double(x.next());
//
//
//		}
//		Account toAcc = getAccountById(toAccId);
//
//		toAcc.putMoneyIn(value);
//		Transaction newTrans = new Transaction(value);
//		toAcc.balance += value;
//
//		// add transaction record to both accounts
//		toAcc.addTrans(newTrans);
//
//		// add transaction record to both user
//		toAcc.getOwner().addTransaction(newTrans);
//
//		/*File y = new File("Desktop:..");
//		private Scanner y;
//		try{
//			y = new Scanner(x);
//		}
//		catch(Expection e){
//			System.out.println("could not find file");
//		}
//
//		while(y.hasNext()){
//
//
//
//		}
//		x = new Formatter("chinese.txt");
//		x.format("%s%s",)
//*/
//	}

	private void depositMoney() throws IOException, InvalidOperationException,
		AccountNotExistException {
		// when this method is called, only the first line of the file will be read
		// each line of the input file should be formatted as either:
		// ==============================
		// cheque, <<accountId>>, 30.25
		// ==============================
		// or
		// =============================================
		// cash, <<accountId>>, 5 3, 10 25, 100 3
		// =============================================
		// where the first number is domination and the second is number of bills

		File file = new File(DEPOSIT_FILE_NAME);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		String[] data;
		String accId;
		st = br.readLine(); // only read the first line of file

		data = st.split(",");
		if (data.length < 3) {
			throw new InvalidOperationException("Sorry, your deposit file is not in " +
				"correct format.");
		} else if (data[0].equals("cheque")) {
			accId = data[1];
			Account acc = getAccountById(accId);
			double amount;
			try {
				amount = Double.valueOf(data[2]);
			} catch (NumberFormatException e) {
				throw new InvalidOperationException("Sorry, " +
					"your deposit file is not in correct format.");
			}
			acc.putMoneyIn(amount);

			Transaction newTrans = new DepositTransaction(amount, getCurrentTime(), acc);
			acc.addTrans(newTrans);

			acc.getOwner().addTransaction(newTrans);

		} else if (data[0].equals("cash")) {
			accId = data[1];
			Account acc = getAccountById(accId);


		} else {
			throw new InvalidOperationException("Sorry, your deposit file is not in " +
				"correct format.");
		}
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
	 * @param fromAcc the source account of transaction
	 * @param toAcc   the destination account of transaction
	 * @param amount  amount of money of transaction
	 * @throws AccountNotExistException  when account with the input id is not found
	 * @throws InvalidOperationException when attempting to transfer out from CreditCardAccount
	 * @throws NoEnoughMoneyException    when amount of money transferred out exceeds what is allowed
	 */
	public void transferMoney(Account fromAcc, Account toAcc, double amount)
		throws InvalidOperationException, NoEnoughMoneyException {

		if (fromAcc instanceof CreditCardAccount) {
			throw new InvalidOperationException("Sorry, transfer money out of a " +
				"credit card account is not supported.");
		}

		fromAcc.takeMoneyOut(amount);
		toAcc.putMoneyIn(amount);
		Transaction newTrans = new TransferTransaction(amount, getCurrentTime(), fromAcc, toAcc);

		// add transaction record to both accounts
		fromAcc.addTrans(newTrans);
		toAcc.addTrans(newTrans);

		// add transaction record to both user
		fromAcc.getOwner().addTransaction(newTrans);
		toAcc.getOwner().addTransaction(newTrans);

	}


	/**
	 * @param acc            account of the user for withdrawal
	 * @param amountWithdraw a HashMap that record the number of bills for each domination that
	 *                       the user wants to withdraw
	 * @throws InsufficientCashException when the number of bills of certain domination is less
	 *                                   than the amount that the user wants to withdraw
	 * @throws NoEnoughMoneyException    when amount withdrawn from the account exceeds what is allowed
	 */
	public void withdrawCash(Account acc, HashMap<Cash, Integer> amountWithdraw)
		throws InsufficientCashException, NoEnoughMoneyException {

		ableToWithdraw(amountWithdraw);

		// calculate the total amount withdrawn
		int totalAmount = calculateTotalBillAmount(amountWithdraw);

		acc.takeMoneyOut(totalAmount); // money in account may not be enough

		deductCash(amountWithdraw);

		Transaction newTrans = new WithdrawTransaction(totalAmount, getCurrentTime(), acc);
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
		for (Account acc : accounts) {
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
	 *
	 * @param tx the transaction to proceed.
	 * @return true if succeeds, false otherwise.
	 */
	public boolean proceedTransaction(Transaction tx) {
		return false;
	}


}
