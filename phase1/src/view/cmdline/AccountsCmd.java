package view.cmdline;

import controller.AccountFactory;
import model.accounts.*;
import model.persons.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountsCmd {
	private CommandLineUI ui;
	private AccountFactory accountFactory;

	/**
	 * Constructs the accounts command
	 * @param ui the ui to use
	 */
	AccountsCmd(CommandLineUI ui) {
		this.ui = ui;
		this.accountFactory = new AccountFactory();
	}

	/**
	 * List accounts of current user.
	 * Must log in as a user to use.
	 */
	void listAccounts() {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}

		ArrayList<Account> accounts = user.getAccounts();
		// to be thread-safe
		ui.setCurAccounts(accounts);

		int i = 0;
		ui.getOutput().println("Type & Order\tID\tBalance");
		for (Account acc : accounts) {
			String typeStr = accountFactory.getAccountType(acc);
			// TODO cache the codes
			ui.getOutput().println(typeStr + i + "\t\t"
				+ acc.getAccountId() + "\t" + acc.getBalance());
			++i;
		}
		ui.getOutput().println("\n" +
			"Summary -- Net Total: " + user.getNetTotal());
	}

	/**
	 * Gets the information about a certain account
	 * @param query the id or order of account
	 */
	void showAccount(String query) {
		User u = ui.checkUserLogin();
		if (u == null) {
			return;
		}
		// TODO: should do acc id-order check
		Account acc = ui.searchAccount(query);
		if (acc == null) {
			ui.getError().println("No such account found.");
			return;
		}

		ui.getOutput().println("Acc Id\tType\tBalance");
		ui.getOutput().println(acc.getAccountId() + ": " + accountFactory.getAccountType(acc)
			+ ": " + acc.getBalance());
	}
}
