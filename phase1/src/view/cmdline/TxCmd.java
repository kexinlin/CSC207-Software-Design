package view.cmdline;

import model.accounts.Account;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.User;

public class TxCmd {
	private CommandLineUI ui;

	/**
	 * Constructs the transaction command
	 * @param ui the ui to use
	 */
	TxCmd(CommandLineUI ui) {
		this.ui = ui;
	}

	/**
	 * Transfer money from one account to another.
	 * @param args the arguments from the command line.
	 *
	 */
	void moveMoney(String args) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}
		String[] arr = args.split("\\s+", 3);
		if (arr.length < 3) {
			ui.getError().println("Not enough arguments. Type `help mv` for detailed usage.");
			return;
		}
		Account source = ui.searchAccount(arr[0]);
		Account dest = ui.searchAccount(arr[1]);
		double amount = Double.valueOf(arr[2]);
		if (source == null) {
			ui.getError().println("Source account not found.");
			return;
		}
		if (dest == null) {
			ui.getError().println("Destination account not found.");
			return;
		}

		// make sure the account belongs to the user
		if (! source.getOwner().equals(user)) {
			ui.getError().println("The source account must be yours.");
			return;
		}

		// after all checking, do the transaction
		try {
			ui.getBankSystem().transferMoney(source, dest, amount);
			ui.getOutput().println("Transaction succeeded.");
		} catch (InvalidOperationException e) {
			ui.getError().println("Transaction failed. " + e);
		} catch (NoEnoughMoneyException e) {
			ui.getError().println("Transaction failed. " + e);
		}
	}

	/**
	 * Deposits the cash or cheque in the transactions file into the
	 * account specified by `query`
	 * @param query the query string for the account
	 * @param ui
	 */
	void deposit(String query, CommandLineUI ui) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}
		Account acc = ui.searchAccount(query);
		if (acc == null) {
			ui.getError().println("No such account.");
			return;
		}
		if (! user.equals(acc.getOwner())) {
			ui.getError().println("You can only deposit into your own account.");
			return;
		}
		try {
			ui.getATM().getDepositController().depositMoney(acc);
		} catch (InvalidOperationException e) {
			ui.getError().println("Error making a deposit: " + e);
			return;
		}
		ui.getOutput().println("Deposit successful.");
	}

	void payBill(String args, CommandLineUI ui) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}

		String[] d = args.split("\\s+");
		if (d.length != 3) {
			ui.getError().println("Error: wrong number of arguments.");
			return;
		}

		Account acc = ui.searchAccount(d[0]);
		if (acc == null) {
			ui.getError().println("Error: Account not found.");
			return;
		}
		if (! user.equals(acc.getOwner())) {
			ui.getError().println("Error: the account is not yours.");
			return;
		}
		String payeeName = d[1];
		double amount;
		try {
			amount = Double.valueOf(d[2]);
		} catch (NumberFormatException e) {
			ui.getError().println("Error: value is not valid.");
			return;
		}
		try {
			ui.getBankSystem().payBill(acc, payeeName, amount);
			ui.getOutput().println("Payment succeeded.");
		} catch (NoEnoughMoneyException e) {
			ui.getError().println("Error: Your account does not have enough money.");
		} catch (InvalidOperationException e) {
			ui.getError().println("Error: " + e);
		}
	}
}
