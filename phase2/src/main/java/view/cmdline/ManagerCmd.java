package view.cmdline;

import model.*;
import model.persons.AccountOwner;
import model.persons.Employee;
import model.persons.User;
import model.transactors.Account;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.exceptions.NoTransactionException;

import java.io.IOException;
import java.util.Map;

import static model.ManagerAction.*;

class ManagerCmd {
	private CommandLineUI ui;

	ManagerCmd(CommandLineUI ui) {
		this.ui = ui;
	}

	private void showRequest(Request request) {
		if (request == null) {
			return;
		}
		ui.getOutput().println(request.getUser().getUsername()
			+ " asked to create an account of type "
			+ request.getAccountType() + ". Msg: "
			+ request.getMsg());
	}

	private void showRequests() {
		int i = 0;
		for (Request req : ui.getBankSystem().getRequests()) {
			ui.getOutput().print("Req #" + i + ": ");
			showRequest(req);
			++i;
		}
	}

	private Request getRequest(String s) {
		int order;
		try {
			order = Integer.valueOf(s);
			return ui.getATM().getBankSystem().getRequests().get(order);
		} catch (NumberFormatException e) {
			ui.getError().println(s + " is not in correct format.");
			return null;
		} catch (IndexOutOfBoundsException e) {
			ui.getError().println("Request #" + s + " not found.");
			return null;
		}
	}

	private void processRequest(String query, String result) {
		Request req = getRequest(query);
		if (req == null) {
			return;
		}
		boolean accepted;
		switch (result) {
			case "y":
				accepted = true;
				break;

			case "n":
				accepted = false;
				break;

			default:
				ui.getError().println("The result must be y or n.");
				return;
		}

		ui.getBankSystem().processRequest(req, accepted);
	}

	/**
	 * View and accept/deny the requests
	 * @param data optionally, # of request and whether the bank manager accepts it.
	 */
	void processRequests(String data) {
		Employee manager = ui.checkEmployeeCan(PROCESS_REQUESTS);
		if (manager == null) {
			return;
		}
		if (data.length() == 0) {
			showRequests();
		} else {
			String[] entries = data.split("\\s+", 2);
			if (entries.length == 1) {
				showRequest(getRequest(entries[0]));
			} else { // len == 2
				processRequest(entries[0], entries[1]);
			}
		}
	}

	/**
	 * Adds the user into the bank system.
	 * @param username the username to create.
	 */
	void addUser(String username) {
		Employee manager = ui.checkEmployeeCan(ADD_USER);
		if (manager == null) {
			return;
		}

		if (username.length() == 0) {
			ui.getError().println("No username specified.");
			return;
		}
		// reserved names for account `type-order` strings. not allowed.
		if (username.matches("^(loc|chq|sav|cre)\\d+$")) {
			ui.getError().println("The username is reserved and cannot be registered.");
			return;
		}

		if (ui.getBankSystem().getLoginable(username) != null) {
			ui.getError().println("The username is occupied by another individual.");
			return;
		}

		String name;
		try {
			ui.getOutput().print("Enter the name for the user: ");
			name = ui.getReader().readLine();
		} catch (IOException e) {
			ui.getError().println("Error reading the name");
			return;
		}

		String password = ui.readPassword("Enter password for the user: ");
		String repeatPwd = ui.readPassword("Repeat the password: ");
		if (!password.equals(repeatPwd)) {
			ui.getError().println("Passwords do not match.");
			return;
		}

		AccountOwner user = new User(name, username, password);
		ui.getBankSystem().addLoginable(user);
		ui.getOutput().println("Successfully added user.");
	}

	/**
	 * Undo the last transaction on account accId.
	 * @param accId the id of the account.
	 */
	void undoTx(String accId) {
		Employee manager = ui.checkEmployeeCan(UNDO_TX);
		if (manager == null) {
			return;
		}
		try {
			Account acc = ui.getBankSystem().getAccountById(accId);

			ui.getBankSystem().undoTransaction(acc.getLastTrans());
			ui.getOutput().println("Transaction undone.");
		} catch (AccountNotExistException e) {
			ui.getError().println("No such account.");
		} catch (NoTransactionException e) {
			ui.getError().println("No transaction on that account.");
		} catch (InvalidOperationException e) {
			ui.getError().println("Error: " + e);
		} catch (NoEnoughMoneyException e) {
			ui.getError().println("Cannot undo the transaction because the " +
				"account that received the money does not have enough money.");
		}
	}

	/**
	 * put cash into the machine.
	 */
	void stockCash() {
		Employee manager = ui.checkEmployeeCan(STOCK_CASH);
		if (manager == null) {
			return;
		}
		try {
			Money m = ui.getATM().getDepositController().getDepositMoney();
			if (m instanceof CashCollection) {
				ui.getATM().stockCash(((CashCollection) m).getCashMap());
			} else {
				throw new InvalidOperationException("The envelope does not contain cash.");
			}
		} catch (InvalidOperationException e) {
			ui.getError().println("The thing you put in the machine is not cash.");
			return;
		}
		ui.getOutput().println("Done.");
	}

	/**
	 * display the amount of different kinds of cash
	 */
	void showCash() {
		Employee manager = ui.checkEmployeeCan(SHOW_CASH);
		if (manager == null) {
			return;
		}
		ui.getOutput().println("Value\tAmount");
		for (Map.Entry<Cash, Integer> kv : ui.getATM().getBillAmount().entrySet()) {
			ui.getOutput().println(kv.getKey().getNumVal() + "\t" + kv.getValue());
		}
	}
}
