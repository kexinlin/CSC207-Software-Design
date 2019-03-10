package controller;

import model.Message;
import model.Request;
import controller.transactions.BillController;
import controller.transactions.FileBillController;
import model.accounts.Account;
import model.accounts.ChequingAccount;
import model.accounts.CreditCardAccount;
import model.accounts.SavingAccount;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.Loginable;
import model.persons.User;
import model.transactions.*;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankSystem {
	private Date currentTime;
	private HashMap<String, Loginable> loginables;
	private HashMap<String, Account> accounts;
	private String recordFileName;
	private ArrayList<Request> requests;
	private BillController billController;
	private RecordController recordController;
	private ArrayList<Transaction> transactions;
	private AccountFactory accountFactory;
	private ArrayList<Message> messages;

	private static final String NUMBERS = "0123456789";  // for randomly generating accountId;
	private static SecureRandom rnd = new SecureRandom(); // for randomly generating accountId;

	/**
	 * Constructs an instance of BankSystem.
	 */
	public BankSystem(String recordFileName) {
		this.currentTime = new Date();

		this.loginables = new HashMap<>();
		this.accounts = new HashMap<>();

		this.recordFileName = recordFileName;
		this.billController = new FileBillController(this);

		this.recordController = new RecordController(this);

		this.requests = new ArrayList<>();

		this.transactions = new ArrayList<>();
		this.accountFactory = new AccountFactory();
		this.messages = new ArrayList<>();
		recordController.readRecords();
	}

	/**
	 * save records to file.
	 */
	public void close() {
		Calendar cal = new Calendar.Builder().build();
		cal.setTime(getCurrentTime());
		// proceed to the next day, since the system shuts down at midnight
		cal.add(Calendar.DAY_OF_MONTH, 1);
		// on the first day of each month, gain interest.
		if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
			accounts.values().forEach(a -> {
				if (a instanceof SavingAccount) {
					((SavingAccount) a).increaseInterest();
				}
			});
		}
		recordController.writeRecords();
	}

	/**
	 * Get current time of BankSystem.
	 *
	 * @return current time of BankSystem
	 */
	public Date getCurrentTime() {
		return this.currentTime;
	}


	/**
	 * Set a new current time for this BankSystem.
	 *
	 * @param newTime The new time set for this BankSystem.
	 */
	public void setCurrentTime(Date newTime) {
		this.currentTime = newTime;
	}

	/**
	 * Get string representation of `currentTime` in this BankSystem.
	 *
	 * @return string representation of `currentTime` in this BankSystem
	 */
	public String getCurrentTimeStr() {
		Date date = getCurrentTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	/**
	 * Get the user or admin with `username`.
	 *
	 * @param username the username of wanted person
	 * @return a `Loginable` corresponding to that person, or null if not found
	 */
	public Loginable getLoginable(String username) {
		return loginables.getOrDefault(username, null);
	}

	/**
	 * Get a HashMap of unique id and Loginable object for all loginables in this bank system.
	 *
	 * @return a map of unique id and Loginable object
	 */
	public HashMap<String, Loginable> getLoginables() {
		return loginables;
	}

	/**
	 * Get a HashMap of account id and account object for all accounts in this bank system.
	 * @return
	 */
	public HashMap<String, Account> getAccounts() {
		return accounts;
	}

	/**
	 * Get a ArrayList for all transactions in this bank system.
	 * @return
	 */
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * Create a new User and save into `this.loginables`.
	 *
	 * @param name     the name of the user
	 * @param username the username of the user
	 * @param password the password of the user
	 * @throws InvalidOperationException when the username is already taken
	 */
	public void createUser(String name, String username, String password) throws InvalidOperationException {
		if (this.loginables.containsKey(username)) {
			throw new InvalidOperationException("Sorry, " +
				"your username is already taken. Please try another one.");
		}
		User u = new User(name, username, password);

		this.loginables.put(username, u);
	}

	/**
	 * Randomly generate an `accountId`.
	 *
	 * @return the generated String
	 */
	private String randomString() {
		StringBuilder stringbuilder = new StringBuilder(6);
		do {
			for (int i = 0; i < 6; i++)
				stringbuilder.append(NUMBERS.charAt(rnd.nextInt(NUMBERS.length())));
			String temp = stringbuilder.toString();
			boolean flag = !accounts.containsKey(temp);
			if (flag)
				break;
		} while (true);
		return stringbuilder.toString();
	}

	/**
	 * @return a ArrayList that contain all the requests for creating accounts from User,
	 * which haven't yet been verified by BankManager
	 */
	public ArrayList<Request> getRequests() {
		return requests;
	}

	public void processRequest(Request request, boolean accepted) {
		if (accepted) {
			createAccount(request);
		} else {
			Message msg = new Message(request.getUser(),
				"Your request to create a account of type " + request.getAccountType()
					+ " was declined.");
			addMessage(msg);
		}
		requests.remove(request);
	}

	/**
	 * Create an account based on the input Request.
	 *
	 * @param request a request from a User containing the information for creating an account,
	 *                which has been verified by a BankManager.
	 */
	private void createAccount(Request request) {
		User owner = request.getUser();
		String accountId = randomString();
		Account newAccount = accountFactory.getAccount(request.getAccountType()
			,0, getCurrentTime(), accountId, owner);

		// if there were no primary chequing accounts
		// make this the default one.
		if (newAccount instanceof ChequingAccount &&
			owner.getAccounts().stream()
				.noneMatch(a -> a instanceof ChequingAccount)) {
			owner.setPrimaryCheuqingAccount((ChequingAccount) newAccount);
		}
		addAccount(newAccount);

	}

	/**
	 * Undo the input account's most recent transaction, except for paying bills.
	 *
	 * @param tx the transaction to undo
	 * @throws InvalidOperationException when certain invalid operation is done
	 * @throws NoEnoughMoneyException    when the account taken money out does not have enough money
	 */
	public void undoTransaction(Transaction tx) throws
		InvalidOperationException, NoEnoughMoneyException {
		if (tx instanceof PayBillTransaction) {
			throw new InvalidOperationException("Sorry, you cannot undo a transaction " +
				"for paying bill.");
		} else if (tx instanceof TransferTransaction) {
			undoTransfer((TransferTransaction) tx);
		} else if (tx instanceof WithdrawTransaction) {
			undoWithdraw((WithdrawTransaction) tx);
		} else if (tx instanceof DepositTransaction) {
			undoDeposit((DepositTransaction) tx);
		}
	}

	/**
	 * Undo a transfer transaction. A helper function for undoTransaction.
	 *
	 * @param lastTrans the transaction that needs to be undone
	 * @throws NoEnoughMoneyException    when the account taken money out does not have enough money
	 * @throws InvalidOperationException when certain invalid operation is done
	 */
	private void undoTransfer(TransferTransaction lastTrans) throws NoEnoughMoneyException,
		InvalidOperationException {
		double amount = lastTrans.getAmount();
		Account source = (lastTrans).getFromAcc();
		Account dest = (lastTrans).getToAcc();
		dest.takeMoneyOut(amount);
		source.putMoneyIn(amount);
		Transaction newTrans = new TransferTransaction(amount, getCurrentTime(), dest, source);
		addTransaction(newTrans);
	}

	/**
	 * Undo a withdraw transaction. A helper function for undoTransaction.
	 *
	 * @param lastTrans the transaction that needs to be undone
	 */
	private void undoWithdraw(WithdrawTransaction lastTrans) {
		double amount = lastTrans.getAmount();
		Account acc = (lastTrans).getAcc();
		acc.putMoneyIn(amount);
		Transaction newTrans = new DepositTransaction(amount, getCurrentTime(), acc);
		addTransaction(newTrans);
	}

	/**
	 * Undo a deposit transaction. A helper function for undoTransaction.
	 *
	 * @param lastTrans the transaction that needs to be undone
	 * @throws NoEnoughMoneyException    when the account taken money out does not have enough money
	 * @throws InvalidOperationException when certain invalid operation is done
	 */
	private void undoDeposit(DepositTransaction lastTrans) throws NoEnoughMoneyException,
		InvalidOperationException {
		double amount = lastTrans.getAmount();
		Account acc = (lastTrans).getAcc();
		acc.takeMoneyOut(amount);
		Transaction newTrans = new WithdrawTransaction(amount, getCurrentTime(), acc);
		addTransaction(newTrans);
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
		addTransaction(newTrans);
	}

	/**
	 * Get the Account object corresponding to the input Account id.
	 *
	 * @param id account id
	 * @return Account object corresponding to the input Account id.
	 * @throws AccountNotExistException when account with the input id is not found
	 */
	public Account getAccountById(String id) throws AccountNotExistException {
		if (!accounts.containsKey(id)) {
			throw new AccountNotExistException("Sorry, can't find this account. " +
				"Please check your account number again.");
		}
		return accounts.get(id);
	}

	/**
	 * Add the account `acc` into `this.accounts` as well as the owner of the account.
	 *
	 * @param acc the account to add
	 */
	public void addAccount(Account acc) {
		accounts.put(acc.getAccountId(), acc);
		acc.getOwner().addAccount(acc);
	}

	/**
	 * Add the transaction tx to transaction history of the bank system, accounts, and user(s)
	 * @param tx the transaction to add.
	 */
	public void addTransaction(Transaction tx) {
		this.transactions.add(tx);

		if (tx instanceof TransferTransaction) {
			TransferTransaction transferTx = (TransferTransaction) tx;
			Account fromAcc = transferTx.getFromAcc();
			Account toAcc = transferTx.getToAcc();

			// add transaction record to both accounts
			fromAcc.addTrans(transferTx);
			toAcc.addTrans(transferTx);

			// add transaction record to both user
			fromAcc.getOwner().addTransaction(transferTx);
			if (!fromAcc.getOwner().equals(toAcc.getOwner())) {
				toAcc.getOwner().addTransaction(transferTx);
			}
		} else if (tx instanceof DepositTransaction) {
			DepositTransaction depositTx = (DepositTransaction) tx;
			Account acc = depositTx.getAcc();

			acc.addTrans(depositTx);
			acc.getOwner().addTransaction(depositTx);
		} else if (tx instanceof WithdrawTransaction) {
			WithdrawTransaction withdrawTx = (WithdrawTransaction) tx;
			Account acc = withdrawTx.getAcc();

			acc.addTrans(withdrawTx);
			acc.getOwner().addTransaction(withdrawTx);
		} else if (tx instanceof PayBillTransaction) {
			PayBillTransaction payBillTx = (PayBillTransaction) tx;
			Account acc = payBillTx.getSource();

			acc.addTrans(payBillTx);
			acc.getOwner().addTransaction(payBillTx);
		}
	}

	public void addLoginable(Loginable loginable) {
		loginables.put(loginable.getUsername(), loginable);
	}

	/**
	 * Sets the bill controller for this bank system.
	 *
	 * @param billController the bill controller
	 */
	public void setBillController(BillController billController) {
		this.billController = billController;
	}

	/**
	 * Gets the bill controller for this bank system.
	 *
	 * @return the bill controller
	 */
	public BillController getBillController() {
		return billController;
	}

	/**
	 * Records payment of `amount` from `acc` to `destinationName`
	 *
	 * @param acc       the account to take money from
	 * @param payeeName the name of the payee
	 * @param amount    the amount of money
	 * @throws NoEnoughMoneyException    if `acc` does not have enough money to withdraw
	 * @throws InvalidOperationException
	 */
	public void payBill(Account acc, String payeeName, double amount)
		throws NoEnoughMoneyException, InvalidOperationException {
		acc.takeMoneyOut(amount);
		try {
			billController.recordPayment(acc, payeeName, amount);
		} catch (InvalidOperationException e) { // recording failed, restore prev. state.
			acc.putMoneyIn(amount);
			throw e;
		}
		Transaction tx = new PayBillTransaction(amount, getCurrentTime(), acc, payeeName);

		addTransaction(tx);
	}

	/**
	 * Get the name of the file that stores record of this BankSystem.
	 * @return the name of the file that stores record of this BankSystem
	 */
	public String getRecordFileName() {
		return recordFileName;
	}

	/**
	 * Set the name of the file that stores record of this BankSystem
	 * @param recordFileName new name for the file that stores record of this BankSystem
	 */
	public void setRecordFileName(String recordFileName) {
		this.recordFileName = recordFileName;
	}

	/**
	 * Add req to the request list.
	 * @param req the request to add.
	 */
	public void addRequest(Request req) {
		this.requests.add(req);
	}

	/**
	 * Gets all messages in the system
	 * @return an array list of messages
	 */
	public ArrayList<Message> getMessages() {
		return messages;
	}

	/**
	 * Add msg to the message for the user.
	 * @param msg the message to add.
	 */
	public void addMessage(Message msg) {
		this.messages.add(msg);
		msg.getUser().addMessage(msg);
	}

	/**
	 * Remove msg from the user.
	 * @param msg the message to remove.
	 */
	public void removeMessage(Message msg) {
		this.messages.remove(msg);
		msg.getUser().removeMessage(msg);
	}
}
