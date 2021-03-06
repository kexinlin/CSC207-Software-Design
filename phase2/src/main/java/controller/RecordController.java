package controller;

import model.Message;
import model.Money;
import model.Request;
import model.persons.*;
import model.transactors.*;
import model.exceptions.AccountNotExistException;
import model.transactions.*;
import sun.rmi.runtime.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

class RecordController {
	private BankSystem bankSystem;
	private AccountFactory accountFactory;
	private TransactionFactory txFactory;
	private LoginableFactory loginableFactory;

	/**
	 * Constructs a record controller
	 * @param bankSystem the bank system to use
	 */
	public RecordController(BankSystem bankSystem) {
		this.bankSystem = bankSystem;
		this.accountFactory = new AccountFactory();
		this.txFactory = new TransactionFactory();
		this.loginableFactory = new LoginableFactory();
	}

	/**
	 * Gets the File for recording
	 * @return a File object
	 */
	private File getRecordFile() {
		return new File(bankSystem.getRecordFileName());
	}

	/**
	 * Read the records from data file.
	 *
	 * Format for this file is:
	 *
	 * user,NAME,USERNAME,PASSWORD
	 * user-employee,USERNAME,PASSWORD
	 * manager,USERNAME,PASSWORD
	 * account,TYPE,BALANCE,DATE-OF-CREATION,ACC-ID,OWNER-USERNAME
	 * tx,FROM-ACC-ID,TO-ACC-ID,DATE,AMOUNT
	 * req,USERNAME,ACC-TYPE,MSG
	 * msg,USERNAME,MESSAGE-TEXT
	 * set,primary-acc,USERNAME,ACC-ID
	 * set,co-owners,ACC-ID,USERNAME1,USERNAME2,...
	 * set,used-credit,DEBT-ACC-ID,AMOUNT
	 */
	public void readRecords() {
		File file = getRecordFile();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] entries = line.split(",", 2);
				if (entries.length != 2) {
					// error
					continue;
				}
				switch (entries[0]) {
					case "user":
					case "user-employee":
					case "manager":
						processLoginable(entries[0], entries[1]);
						break;

					case "account":
						processAccount(entries[1]);
						break;

					case "tx":
						processTx(entries[1]);
						break;

					case "req":
						processRequest(entries[1]);
						break;

					case "msg":
						processMessage(entries[1]);
						break;

					case "set":
						processSetting(entries[1]);
						break;

					default:
						break;
				}
			}
		} catch (IOException ignore) {

		}

	}

	/**
	 * Reads setting of users
	 * @param data a line containing the setting,
	 *             excluding the "setting," prefix (first column)
	 */
	private void processSetting(String data) {
		String[] entries = data.split(",", 2);
		if (entries.length < 2) {
			return;
		}
		if (entries[0].equals("primary-acc")) {
			String[] e = entries[1].split(",", 2);
			if (e.length < 2) {
				return;
			}
			String username = e[0];
			Loginable l = bankSystem.getLoginable(username);
			if (!(l instanceof AccountOwner)) {
				return;
			}
			AccountOwner user = (AccountOwner) l;
			String accId = e[1];
			Account acc;
			try {
				acc = bankSystem.getAccountById(accId);
			} catch (AccountNotExistException ex) {
				return;
			}
			if (! (acc instanceof ChequingAccount)) {
				return;
			}
			user.setPrimaryCheuqingAccount((ChequingAccount) acc);
		} else if (entries[0].equals("co-owners")) {
			ArrayList<String> e = new ArrayList<>(Arrays.asList(entries[1].split(",")));
			Iterator<String> it = e.iterator();
			if (! it.hasNext()) {
				return;
			}
			String accountId = it.next();
			try {
				Account acc = bankSystem.getAccountById(accountId);
				for (; it.hasNext(); ) {
					String s = it.next();
					Loginable l = bankSystem.getLoginable(s);
					if (l instanceof AccountOwner) {
						bankSystem.addCoOwner(acc, (AccountOwner) l);
					}
				}
			} catch (AccountNotExistException ignore) {

			}
		} else if (entries[0].equals("used-credit")
			|| entries[0].equals("min-balance")) {
			String[] arr = entries[1].split(",", 2);
			if (arr.length < 2) {
				return;
			}
			String accountId = arr[0];
			Account acc;
			double val;
			try {
				acc = bankSystem.getAccountById(accountId);
				val = Double.valueOf(arr[1]);
			} catch (NumberFormatException|AccountNotExistException e) {
				return;
			}
			if (entries[0].equals("used-credit")) {
				if (!(acc instanceof DebtAccount)) {
					return;
				}
				((DebtAccount) acc).setUsedCredit(new Money(val));
			} else {
				if (! (acc.hasInterest())) {
					return;
				}
				acc.setMinimumBalance(new Money(val));
			}
		}

	}

	/**
	 * Reads a message from the record, and add it to the message list
	 * @param data the line of message, excluding the first column
	 */
	private void processMessage(String data) {
		String[] entries = data.split(",", 2);
		if (entries.length < 2) {
			return;
		}

		String username = entries[0];
		AccountOwner user = (AccountOwner) bankSystem.getLoginable(username);
		if (user == null) {
			return;
		}
		String text = entries[1];

		Message msg = new Message(user, text);
		bankSystem.addMessage(msg);
	}

	/**
	 * Reads a request from the file, and add it to request list.
	 * @param data the line of request, excluding the first column
	 */
	private void processRequest(String data) {
		String[] entries = data.split(",", 3);
		if (entries.length < 3) {
			return;
		}

		String username = entries[0];
		AccountOwner user = (AccountOwner) bankSystem.getLoginable(username);
		if (user == null) {
			return;
		}
		String type = entries[1];
		String msg = entries[2];
		Request req = new Request(user, type, msg);
		bankSystem.addRequest(req);
	}

	/**
	 * Reads a transaction from the file and add it to transaction list
	 * @param data the line containing the transaction, excluding the first column.
	 */
	private void processTx(String data) {
		Transaction tx = txFactory.fromRecord(data, bankSystem);
		bankSystem.addTransaction(tx);
	}

	/**
	 * Reads an account and add it to the account list
	 * @param data the line containing the account, excluding the first column
	 */
	private void processAccount(String data) {
		String[] entries = data.split(",", 5);
		if (entries.length != 5) {
			return;
		}
		String type = entries[0];
		double balance;
		Date dateCreated;
		try {
			balance = Double.valueOf(entries[1]);
			dateCreated = new Date(Long.valueOf(entries[2]));
		} catch (NumberFormatException e) {
			return;
		}
		String accountId = entries[3];
		Loginable owner = bankSystem.getLoginable(entries[4]);
		if (! (owner instanceof AccountOwner)) {
			return;
		}

		Account account = accountFactory.getAccount(type,
			new Money(balance), dateCreated, accountId, (AccountOwner)owner);
		bankSystem.addAccount(account);
	}

	/**
	 * Reads a bank manager and add it to loginable list
	 * @param data the line containing the bank manager, excluding the first column
	 */
	private void processLoginable(String type, String data) {
		bankSystem.addLoginable(loginableFactory.fromRecord(type, data));
	}

	/**
	 * Write all records to the record file.
	 */
	public void writeRecords() {
		File file = getRecordFile();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			// record users and managers
			for (Loginable l : bankSystem.getLoginables().values()) {
				recordLoginable(writer, l);
			}
			// then, accounts
			for (Account a : bankSystem.getAccounts().values()) {
				recordAccount(writer, a);
			}
			// then, transactions
			for (Transaction tx : bankSystem.getTransactions()) {
				recordTransaction(writer, tx);
			}
			// then, requests
			for (Request req : bankSystem.getRequests()) {
				recordRequest(writer, req);
			}
			// then, messages
			for (Message msg : bankSystem.getMessages()) {
				recordMessage(writer, msg);
			}
			// then, settings
			for (Loginable l : bankSystem.getLoginables().values()) {
				if (l instanceof AccountOwner) {
					Account acc = ((AccountOwner) l).getPrimaryChequingAccount();
					recordPrimaryAcc(writer, acc);
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot save records");
			//
		}
	}

	/**
	 * Records the primary account for the user
	 * @param writer the BufferedWriter to use
	 * @param acc the primary account
	 * @throws IOException when the writer fails
	 */
	private void recordPrimaryAcc(BufferedWriter writer, Account acc) throws IOException {
		if (acc == null) {
			return;
		}
		writer.write("set,primary-acc,"
			+ acc.getOwner().getUsername() + ","
			+ acc.getId() + "\n");
	}

	/**
	 * Records a message
	 * @param writer the BufferedWriter to use
	 * @param msg the Message
	 * @throws IOException when the writer fails
	 */
	private void recordMessage(BufferedWriter writer, Message msg) throws IOException {
		writer.write("msg,"
			+ msg.getUser().getUsername() + ","
			+ msg.getText() + "\n");
	}

	/**
	 * Records a Request
	 * @param writer the BufferedWriter to use
	 * @param req the request to record
	 * @throws IOException when writer fails
	 */
	private void recordRequest(BufferedWriter writer, Request req) throws IOException {
		writer.write("req,"
			+ req.getUser().getUsername() + ","
			+ req.getAccountType() + ","
			+ req.getMsg() + "\n");
	}

	/**
	 * Records an account
	 * @param writer the BufferedWriter to use
	 * @param account the account to record
	 * @throws IOException when writer fails
	 */
	private void recordAccount(BufferedWriter writer, Account account) throws IOException {
		writer.write("account,"
			+ accountFactory.getAccountType(account) + ","
			+ account.getActualBalance() + ","
			+ account.getDateOfCreation().getTime() + ","
			+ account.getId() + ","
			+ account.getOwner().getUsername() + "\n");
		ArrayList<AccountOwner> coOwners = account.getCoOwners();
		if (! coOwners.isEmpty()) {
			writer.write("set,co-owners,"
				+ account.getId() + ","
				+ String.join(",",
				coOwners.stream().map(o -> o.getUsername()).toArray( n -> new String[n] ))
				+ "\n");
		}
		if (account instanceof DebtAccount) {
			writer.write("set,used-credit," +
				account.getId() + "," + ((DebtAccount) account).getUsedCredit() + "\n");
		}
		if (account.hasInterest()) {
			writer.write("set,min-balance," +
				account.getId() + "," + account.getMinimumBalance() + "\n");
		}
	}

	private void recordLoginable(BufferedWriter writer, Loginable loginable) throws IOException {
		writer.write(loginableFactory.toRecord(loginable) + "\n");
	}

	/**
	 * Records a transaction
	 * @param writer the writer to use
	 * @param tx the transaction to record
	 * @throws IOException when writer fails
	 */
	private void recordTransaction(BufferedWriter writer, Transaction tx) throws IOException {
		writer.write(txFactory.toRecord(tx) + "\n");
	}


}
