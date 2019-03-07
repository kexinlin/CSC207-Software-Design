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


	/**
	 * Deposit money into an account by entering a cheque or cash into the machine
	 * (an input file called deposits.txt will be read)
	 * <p>
	 * When this method is called, only the first line of the file will be read
	 * that line of the input file should be formatted as either:
	 * -------------------------------------------------------
	 * cheque,<<accountId>>,<<amount of cheque (double)>>
	 * -------------------------------------------------------
	 * or
	 * -----------------------------------------------------------------------------------------
	 * cash,<<accountId>>,<<denomination>> <<num of bills>>,<<denomination>> <<num of bills>>
	 * -----------------------------------------------------------------------------------------
	 * <p>
	 * example for cheque deposit:
	 * ----------------------------------
	 * cheque,183774393138,1231.56
	 * ----------------------------------
	 * <p>
	 * example for cash deposit:
	 * ------------------------------------------
	 * cash,183017437813,5 10,20 3,100 6
	 * ------------------------------------------
	 *
	 * @throws IOException               when file is deleted while reading or other IO problems
	 * @throws InvalidOperationException when the format of data is incorrect
	 * @throws AccountNotExistException  when the `accountId` does not exist
	 */
	public void depositMoney() throws IOException, InvalidOperationException,
		AccountNotExistException {

		File file = new File(DEPOSIT_FILE_NAME);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		String[] data;
		st = br.readLine(); // only read the first line of file

		data = st.split(",");
		if (data.length < 3) {
			throw new InvalidOperationException("Sorry, your deposit file is not in " +
				"correct format.");
		} else if (data[0].equals("cheque")) {
			depositCheque(data);

		} else if (data[0].equals("cash")) {
			depositCash(data);
		} else {
			throw new InvalidOperationException("Sorry, your deposit file is not in " +
				"correct format.");
		}
	}


	/**
	 * Given a String array storing information of accountId and amount of cheque, increase
	 * balance of account
	 *
	 * @param data a String array storing information of accountId and amount of cheque
	 * @throws InvalidOperationException when the format of data is incorrect
	 * @throws AccountNotExistException  when the `accountId` does not exist
	 */
	private void depositCheque(String[] data) throws InvalidOperationException,
		AccountNotExistException {
		String accId;
		if (data.length != 3) {
			throw new InvalidOperationException("Sorry, your deposit file is not in " +
				"correct format.");
		}
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

		Transaction newTrans = new DepositTransaction(amount, this.currentTime, acc);
		acc.addTrans(newTrans);
		acc.getOwner().addTransaction(newTrans);
	}

	/**
	 * Given a String array storing information of accountId and number of bills, stock
	 * corresponding cash into ATM and increase balance of account
	 *
	 * @param data a String array storing information of accountId and number of bills
	 * @throws InvalidOperationException when the format of data is incorrect
	 * @throws AccountNotExistException  when the `accountId` does not exist
	 */
	private void depositCash(String[] data) throws InvalidOperationException, AccountNotExistException {
		String accId = data[1];
		Account acc = getAccountById(accId);
		HashMap<Cash, Integer> numOfBill = new HashMap<>();
		for (String valPair : Arrays.copyOfRange(data, 2, data.length - 1)) {
			String[] valData = valPair.split("\\s+");
			if (valData.length != 2) {
				throw new InvalidOperationException("Sorry, your deposit file is " +
					"not in correct format.");
			}
			String denominationStr = valData[0];
			Cash bill;
			int billNum;
			try {
				billNum = Integer.valueOf(valData[1]);
			} catch (NumberFormatException e) {
				throw new InvalidOperationException("Sorry, your deposit file is " +
					"not in correct format.");
			}
			switch (denominationStr) {
				case "5":
					bill = Cash.FIVE;
					break;
				case "10":
					bill = Cash.TEN;
					break;
				case "20":
					bill = Cash.TWENTY;
					break;
				case "50":
					bill = Cash.FIFTY;
					break;
				case "100":
					bill = Cash.HUNDRED;
					break;
				default:
					throw new InvalidOperationException("Sorry, the bill denomination" +
						" in your input file does not exist.");
			}
			numOfBill.put(bill, numOfBill.get(bill) + billNum);
		}
		stockCash(numOfBill);

		int amount = calculateTotalBillAmount(numOfBill);
		acc.putMoneyIn(amount);

		Transaction newTrans = new DepositTransaction(amount, this.currentTime, acc);
		acc.addTrans(newTrans);
		acc.getOwner().addTransaction(newTrans);

	}

	/**
	 * Put `Cash` into this ATM machine.
	 *
	 * @param inputCash a HashMap that map the denomination to its number of bills
	 */
	public void stockCash(HashMap<Cash, Integer> inputCash) {
		for (Map.Entry<Cash, Integer> pair : inputCash.entrySet()) {
			this.billAmount.put(pair.getKey(), pair.getValue());
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
	 * @param amountWithdraw a HashMap that record the number of bills for each denomination that
	 *                       the user wants to withdraw
	 * @throws InsufficientCashException when the number of bills of certain denomination is less
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
	 * @param amountOfBill a HashMap that record the number of bills for each denomination that
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
	 * @param amountWithdraw a HashMap that record the number of bills for each denomination that
	 *                       the user wants to withdraw
	 */
	private void deductCash(HashMap<Cash, Integer> amountWithdraw) {
		for (Cash cash : amountWithdraw.keySet()) {
			int oldNum = this.billAmount.get(cash);
			int newNum = oldNum - amountWithdraw.get(cash);
			this.billAmount.put(cash, newNum);
		}
	}

	public void addCash(HashMap<Cash, Integer> amountAdd){
	//	for(Cash cash: amountAdd.keySet())
	}

	/**
	 * Check whether ATM has sufficient amount of bills for the user to withdraw from.
	 *
	 * @param amountWithdraw a HashMap that record the number of bills for each denomination that
	 *                       the user wants to withdraw
	 * @throws InsufficientCashException when the number of bills of certain denomination is less
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


}
