package controller;

import model.Message;
import model.Money;
import model.Request;
import model.transactors.*;
import model.exceptions.AccountNotExistException;
import model.persons.BankManager;
import model.persons.Loginable;
import model.persons.User;
import model.transactions.*;

import java.io.*;
import java.util.Date;

class RecordController {
	private BankSystem bankSystem;
	private AccountFactory accountFactory;

	/**
	 * Constructs a record controller
	 * @param bankSystem the bank system to use
	 */
	public RecordController(BankSystem bankSystem) {
		this.bankSystem = bankSystem;
		this.accountFactory = new AccountFactory();
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
	 * manager,USERNAME,PASSWORD
	 * account,TYPE,BALANCE,DATE-OF-CREATION,ACC-ID,OWNER-USERNAME
	 * tx,transfer,FROM-ACC-ID,TO-ACC-ID,DATE,AMOUNT
	 * tx,deposit/withdraw,ACC-ID,DATE,AMOUNT
	 * tx,paybill,ACC-ID,PAYEE,DATE,AMOUNT
	 * req,USERNAME,ACC-TYPE,MSG
	 * msg,USERNAME,MESSAGE-TEXT
	 * set,primary-acc,USERNAME,ACC-ID
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
						processUser(entries[1]);
						break;

					case "manager":
						processManager(entries[1]);
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
		} catch (IOException e) {

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
			if (!(l instanceof User)) {
				return;
			}
			User user = (User) l;
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
		User user = (User) bankSystem.getLoginable(username);
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
		User user = (User) bankSystem.getLoginable(username);
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
		String[] entries = data.split(",", 4);
		if (entries.length != 4) {
			return;
		}

		String fromAccId = entries[0];
		String toAccId = entries[1];
		Transactor fromAcc, toAcc;
		fromAcc = bankSystem.getTransactor(fromAccId);
		toAcc = bankSystem.getTransactor(toAccId);
		if (fromAcc == null || toAcc == null) {
			return;
		}

		Date date;
		double amount;
		try {
			date = new Date(Long.valueOf(entries[2]));
			amount = Double.valueOf(entries[3]);
		} catch (NumberFormatException e) {
			return;
		}

		Transaction tx = new Transaction(new Money(amount), date, fromAcc, toAcc);
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
		if (! (owner instanceof User)) {
			return;
		}

		Account account = accountFactory.getAccount(type,
			new Money(balance), dateCreated, accountId, (User)owner);
		bankSystem.addAccount(account);
	}

	/**
	 * Reads a bank manager and add it to loginable list
	 * @param data the line containing the bank manager, excluding the first column
	 */
	private void processManager(String data) {
		String[] entries = data.split(",", 2);
		if (entries.length != 2) {
			// wrong format
			return;
		}
		String username = entries[0];
		String password = entries[1];

		BankManager bankManager = new BankManager(username, password);
		bankSystem.addLoginable(bankManager);
	}

	/**
	 * Reads a user and add it to the loginable list
	 * @param data the line containing the user, excluding the first column
	 */
	private void processUser(String data) {
		String[] entries = data.split(",", 3);
		if (entries.length != 3) {
			// wrong format
			return;
		}

		String name = entries[0];
		String username = entries[1];
		String password = entries[2];

		User user = new User(name, username, password);
		bankSystem.addLoginable(user);
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
				if (l instanceof User) {
					recordUser(writer, (User) l);
				} else if (l instanceof BankManager) {
					recordManager(writer, (BankManager) l);
				}
			}
			// then, transactors
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
				if (l instanceof User) {
					Account acc = ((User) l).getPrimaryChequingAccount();
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
			+ account.getBalance() + ","
			+ account.getDateOfCreation().getTime() + ","
			+ account.getId() + ","
			+ account.getOwner().getUsername() + "\n");
	}

	/**
	 * Records a user
	 * @param writer the BufferedWriter to use
	 * @param user the user to record
	 * @throws IOException
	 */
	private void recordUser(BufferedWriter writer, User user) throws IOException {
		writer.write("user,"
			+ user.getName() + ","
			+ user.getUsername() + ","
			+ user.getPassword() + "\n");
	}

	/**
	 * Records a bank manager
	 * @param writer the writer to use
	 * @param manager the bank manager to record
	 * @throws IOException when writer fails
	 */
	private void recordManager(BufferedWriter writer, BankManager manager) throws IOException {
		writer.write("manager,"
			+ manager.getUsername() + ","
			+ manager.getPassword() + "\n");
	}

	/**
	 * Records a transaction
	 * @param writer the writer to use
	 * @param tx the transaction to record
	 * @throws IOException when writer fails
	 */
	private void recordTransaction(BufferedWriter writer, Transaction tx) throws IOException {
		StringBuilder builder = new StringBuilder("tx,");
		builder
			.append(tx.getSource().getId()).append(",")
			.append(tx.getDest().getId()).append(",");
		builder.append(tx.getDate().getTime()).append(",")
			.append(tx.getAmount())
			.append("\n");

		writer.write(builder.toString());
	}


}
