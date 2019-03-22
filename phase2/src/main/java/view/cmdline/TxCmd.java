package view.cmdline;

import model.transactions.Transaction;
import model.transactors.Account;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.User;
import model.transactors.Transactor;

class TxCmd {
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
			ui.getBankSystem().proceedTransaction(
				ui.getBankSystem().makeTx(amount, source, dest));
			ui.getOutput().println("Transaction succeeded.");
		} catch (InvalidOperationException|NoEnoughMoneyException e) {
			ui.getError().println("Transaction failed. " + e);
		}
	}

	/**
	 * Deposits the cash or cheque in the transactions file into the
	 * account specified by `query`
	 * @param query the query string for the account
	 *
	 */
	void deposit(String query) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}

		Account acc = query.length() > 0
			? ui.searchAccount(query)
			: user.getPrimaryChequingAccount();

		if (acc == null || ! user.equals(acc.getOwner())) {
			ui.getError().println("The account is not found, or does not belong to you.");
			return;
		}

		try {
			ui.getATM().depositMoney(acc);
		} catch (InvalidOperationException e) {
			ui.getError().println("Error making a deposit: " + e);
			return;
		}
		ui.getOutput().println("Deposit successful.");
	}

	void payBill(String args) {
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

	/**
	 * Withdraw from the account.
	 * @param data the query string of the account.
	 */
	void withdraw(String data) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}
		String[] entries = data.split("\\s+", 2);
		if (entries.length != 2) {
			ui.getError().println("Wrong number of arguments.");
			return;
		}
		String query = entries[0];
		Account acc = ui.searchAccount(query);
		if (acc == null || !acc.getOwner().equals(user)) {
			ui.getError().println("The account does not exist or is not owned by you.");
			return;
		}

		double amount;
		try {
			amount = Double.valueOf(entries[1]);
		} catch (NumberFormatException e) {
			ui.getError().println("Amount is not in a correct format.");
			return;
		}
		try {
			ui.getATM().withdrawMoney(acc, amount);
		} catch (InvalidOperationException e) {
			ui.getError().println("Error when giving you money.");
			return;
		} catch (NoEnoughMoneyException e) {
			ui.getError().println("Your account does not have enough money.");
			return;
		} catch (InsufficientCashException e) {
			ui.getError().println("The machine does not have enough cash.");
			return;
		}
		ui.getOutput().println("Withdraw done. Please take your money.");

	}
}
