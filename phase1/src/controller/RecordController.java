package controller;

import model.Message;
import model.Request;
import model.accounts.*;
import model.exceptions.AccountNotExistException;
import model.persons.BankManager;
import model.persons.Loginable;
import model.persons.User;
import model.transactions.*;

import java.io.*;
import java.nio.Buffer;
import java.util.Date;

public class RecordController {
	private BankSystem bankSystem;
	private AccountFactory accountFactory;

	public RecordController(BankSystem bankSystem) {
		this.bankSystem = bankSystem;
		this.accountFactory = new AccountFactory();
	}

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

					case "msg":
						processMessage(entries[1]);

					default:
						break;
				}
			}
		} catch (IOException e) {

		}

	}

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

	private void processTx(String data) {
		String[] entries = data.split(",", 2);
		if (entries.length != 2) {
			return;
		}
		switch (entries[0]) {
			case "transfer":
				processTransfer(entries[1]);
				break;

			case "deposit":
			case "withdraw":
				processDepositWithdraw(entries[0], entries[1]);
				break;

			case "paybill":
				processPayBill(entries[1]);

			default:
				break;
		}
	}

	private void processDepositWithdraw(String type, String data) {
		String[] entries = data.split(",", 3);
		if (entries.length != 3) {
			return;
		}
		String accId = entries[0];
		Account acc;
		try {
			acc = bankSystem.getAccountById(accId);
		} catch (AccountNotExistException e) {
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

		Transaction tx = type.equals("deposit")
			? new DepositTransaction(amount, date, acc)
			: new WithdrawTransaction(amount, date, acc);
		bankSystem.addTransaction(tx);
	}

	private void processPayBill(String data) {
		String[] entries = data.split(",", 4);
		if (entries.length != 4) {
			return;
		}

		String fromAccId = entries[0];
		String payee = entries[1];
		Account fromAcc;
		try {
			fromAcc = bankSystem.getAccountById(fromAccId);
		} catch (AccountNotExistException e) {
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

		Transaction tx = new PayBillTransaction(amount, date, fromAcc, payee);
		bankSystem.addTransaction(tx);
	}

	private void processTransfer(String data) {
		String[] entries = data.split(",", 4);
		if (entries.length != 4) {
			return;
		}

		String fromAccId = entries[0];
		String toAccId = entries[1];
		Account fromAcc, toAcc;
		try {
			fromAcc = bankSystem.getAccountById(fromAccId);
			toAcc = bankSystem.getAccountById(toAccId);
		} catch (AccountNotExistException e) {
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

		Transaction tx = new TransferTransaction(amount, date, fromAcc, toAcc);
		bankSystem.addTransaction(tx);
	}

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
			balance, dateCreated, accountId, (User)owner);
		bankSystem.addAccount(account);
	}

	private void processManager(String data) {
		String[] entries = data.split(",", 2);
		if (entries.length != 2) {
			// wrong format
			return;
		}
		String username = entries[0];
		String password = entries[1];

		BankManager bankManager = new BankManager(bankSystem, username, password);
		bankSystem.addLoginable(bankManager);
	}

	private void processUser(String data) {
		String[] entries = data.split(",", 3);
		if (entries.length != 3) {
			// wrong format
			return;
		}

		String name = entries[0];
		String username = entries[1];
		String password = entries[2];

		User user = new User(bankSystem, name, username, password);
		bankSystem.addLoginable(user);
	}

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
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot save records");
			//
		}
	}

	private void recordMessage(BufferedWriter writer, Message msg) throws IOException {
		writer.write("msg,"
			+ msg.getUser().getUsername() + ","
			+ msg.getText() + "\n");
	}

	private void recordRequest(BufferedWriter writer, Request req) throws IOException {
		writer.write("req,"
			+ req.getUser().getUsername() + ","
			+ req.getAccountType() + ","
			+ req.getMsg() + "\n");
	}

	private void recordAccount(BufferedWriter writer, Account account) throws IOException {
		writer.write("account,"
			+ accountFactory.getAccountType(account) + ","
			+ account.getBalance() + ","
			+ account.getDateOfCreation().getTime() + ","
			+ account.getAccountId() + ","
			+ account.getOwner().getUsername() + "\n");
	}

	private void recordUser(BufferedWriter writer, User user) throws IOException {
		writer.write("user,"
			+ user.getName() + ","
			+ user.getUsername() + ","
			+ user.getPassword() + "\n");
	}

	private void recordManager(BufferedWriter writer, BankManager manager) throws IOException {
		writer.write("manager,"
			+ manager.getUsername() + ","
			+ manager.getPassword() + "\n");
	}

	private void recordTransaction(BufferedWriter writer, Transaction tx) throws IOException {
		StringBuilder builder = new StringBuilder("transaction,");
		if (tx instanceof DepositTransaction) {
			builder.append("deposit,")
				.append(((DepositTransaction) tx).getAcc().getAccountId()).append(",");
		} else if (tx instanceof WithdrawTransaction) {
			builder.append("withdraw,")
				.append(((WithdrawTransaction) tx).getAcc().getAccountId()).append(",");
		} else if (tx instanceof PayBillTransaction) {
			builder.append("paybill,")
				.append(((PayBillTransaction) tx).getSource().getAccountId()).append(",")
				.append(((PayBillTransaction) tx).getPayeeName()).append(",");
		} else if (tx instanceof TransferTransaction) {
			builder.append("transfer,")
				.append(((TransferTransaction) tx).getFromAcc().getAccountId()).append(",")
				.append(((TransferTransaction) tx).getToAcc().getAccountId()).append(",");
		}
		builder.append(tx.getDate().getTime()).append(",")
			.append(tx.getAmount());

		writer.write(builder.toString());
	}


}
