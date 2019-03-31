package controller;

import model.Message;
import model.Money;
import model.Request;
import controller.transactions.BillController;
import controller.transactions.FileBillController;
import model.persons.AccountOwner;
import model.persons.User;
import model.transactors.*;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.Loginable;
import model.transactions.*;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private HashMap<String, OutgoingBill> billPool;
	private MoneyEnvelope dummyEnvelope = new MoneyEnvelope(null);

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
		this.billPool = new HashMap<>();
		recordController.readRecords();
	}

	/**
	 * save records to file.
	 */
	public void close() {
		int statementDay = 1;
		int payDay = 15;
		Calendar cal = new Calendar.Builder().build();
		cal.setTime(getCurrentTime());
		// proceed to the next day, since the system shuts down at midnight
		cal.add(Calendar.DAY_OF_MONTH, 1);

		if (cal.get(Calendar.DAY_OF_MONTH) == statementDay) {
			// on the first day of each month, gain interest
			// as well as notice user of their debt accounts' statement balance
			accounts.values().forEach(a -> {
				if (a.hasInterest()) {
					a.increaseInterest();
				}
				if (a instanceof DebtAccount) {
					((DebtAccount) a).genStatement();
					addMessage(new Message(a.getOwner(),
						"Your balance of account " + a.getId() +
						" is now " + ((DebtAccount) a).getStatementBalance() +
						". You need to pay before the start of the 15th of this month."));
				}
			});
		}

		if (cal.get(Calendar.DAY_OF_MONTH) == payDay) {
			accounts.values().forEach(a -> {
				if (a instanceof DebtAccount) {
					((DebtAccount) a).gainInterest();
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
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public String timeFormater(Date date){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
	 * Get a HashMap of account id and account object for all transactors in this bank system.
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
	 * Create a new AccountOwner and save into `this.loginables`.
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
		AccountOwner u = new User(name, username, password);

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
	 * @return a ArrayList that contain all the requests for creating transactors from AccountOwner,
	 * which haven't yet been verified by BankManager
	 */
	public ArrayList<Request> getRequests() {
		return requests;
	}

	public void processRequest(Request request, boolean accepted) {
		if (accepted) {
			createAccount(request);
			Message msg = new Message(request.getUser(),
				"Your request to create a account of type " + request.getAccountType()
					+ " was accepted at " + getCurrentTimeStr());
			addMessage(msg);
		} else {
			Message msg = new Message(request.getUser(),
				"Your request to create a account of type " + request.getAccountType()
					+ " was declined at " + getCurrentTimeStr());
			addMessage(msg);
		}
		requests.remove(request);
	}

	/**
	 * Create an account based on the input Request.
	 *
	 * @param request a request from a AccountOwner containing the information for creating an account,
	 *                which has been verified by a BankManager.
	 */
	public void createAccount(Request request) {
		AccountOwner owner = request.getUser();
		String accountId = randomString();
		Account newAccount = accountFactory.getAccount(request.getAccountType()
			, new Money(0), getCurrentTime(), accountId, owner);

		// if there were no primary chequing transactors
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
		if (tx.getSource() instanceof Account
			&& tx.getDest() instanceof Account) {
			Transaction reverseTx = new Transaction(tx.getAmount(), getCurrentTime(),
				tx.getDest(), tx.getSource(), tx.getFee().getOpposite());
			proceedTransaction(reverseTx);
		} else {
			throw new InvalidOperationException(
				"Cannot undo transactions involving non-accounts.");
		}
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

		Transaction newTrans = new Transaction(new Money(amount),
			getCurrentTime(), fromAcc, toAcc);
		proceedTransaction(newTrans);
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
	 * Add the account `acc` into `this.transactors` as well as the owner of the account.
	 *
	 * @param acc the account to add
	 */
	public void addAccount(Account acc) {
		accounts.put(acc.getId(), acc);
		acc.getOwner().addAccount(acc);
	}

	/**
	 * Add the transaction tx to transaction history of the bank system, transactors, and user(s)
	 * @param tx the transaction to add.
	 */
	public void addTransaction(Transaction tx) {
		this.transactions.add(tx);

		// only accounts record their transactions.
		if (tx.getSource() instanceof Account) {
			Account fromAcc = (Account) tx.getSource();
			fromAcc.addTrans(tx);
			fromAcc.getOwner().addTransaction(tx);
		}
		if (tx.getDest() instanceof Account) {
			Account toAcc = (Account) tx.getDest();
			toAcc.addTrans(tx);
			if (tx.getSource() instanceof Account
				&& !((Account)tx.getSource()).getOwner().equals(toAcc.getOwner())) {
				toAcc.getOwner().addTransaction(tx);
			}
		}
	}

	/**
	 * Add owner to acc's co-owner list
	 * Add acc to owner's account list
	 * @param acc
	 * @param owner
	 */
	public void addCoOwner(Account acc, AccountOwner owner) {
		acc.addCoOwner(owner);
		owner.addAccount(acc);
	}

	public boolean removeCoOwner(Account acc, AccountOwner owner) {
		if (acc.getCoOwners().contains(owner)) {
			acc.getCoOwners().remove(owner);
			owner.getAccounts().remove(acc);
			return true;
		}
		return false;
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
	 *
	 */
	public void proceedTransaction(Transaction tx)
		throws NoEnoughMoneyException, InvalidOperationException {
		Money sourceAmount = tx.getAmount();
		Money destAmount = tx.getAmount();
		if (tx.getFee().compareTo(new Money(0)) > 0) {
			sourceAmount = sourceAmount.add(tx.getFee());
		} else { // negative fee indicates that we are undoing a tx
			destAmount = destAmount.subtract(tx.getFee());
		}
		tx.getSource().takeMoneyOut(sourceAmount);
		try {
			tx.getDest().putMoneyIn(destAmount);
		} catch (InvalidOperationException e) {
			// if dest cannot receive the money (e.g. there is not enough cash
			// in the machine), revert this
			tx.getSource().putMoneyIn(sourceAmount);
			throw e;
		}

		addTransaction(tx);
	}

	public Transaction makeTx(double amount, Transactor source, Transactor dest) {
		return new Transaction(new Money(amount), getCurrentTime(), source, dest);
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

	public Transactor getTransactor(String s) {
		Matcher m = Pattern.compile("<bill-(.+)>").matcher(s);
		if (m.matches()) {
			String payeeName = m.group(1);
			OutgoingBill bill = billPool.get(payeeName);
			if (bill != null) {
				return bill;
			} else {
				bill = new OutgoingBill(this, payeeName);
				billPool.put(payeeName, bill);
				return bill;
			}
		}
		if (s.equals("<envelope>")) {
			return dummyEnvelope;
		}
		try {
			return getAccountById(s);
		} catch (AccountNotExistException e) {
			return null;
		}
	}

	/**
	 * Performs the full operation of paying a bill.
	 * @param source
	 * @param payeeName
	 * @param amount
	 */
	public void payBill(Account source, String payeeName, double amount)
		throws NoEnoughMoneyException, InvalidOperationException {
		Transactor payee = getTransactor("<bill-" + payeeName + ">");
		proceedTransaction(
			new Transaction(new Money(amount), getCurrentTime(), source, payee));
	}

	public ArrayList<String> getAllAccountType(){
		ArrayList<String> accType = new ArrayList<>();
		accType.add("ChequingAccount");
		accType.add("SavingAccount");
		accType.add("LineOfCreditAccount");
		accType.add("CreditCardAccount");
		accType.add("HighInterestAccount");
		return accType;
	}

	public SimpleDateFormat getDateFormmater(){
		return new SimpleDateFormat("yyyy/MM/dd");
	}
}
