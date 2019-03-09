package view.cmdline;

import controller.AccountFactory;
import model.Request;
import model.accounts.*;
import model.persons.User;

import java.util.ArrayList;
import java.util.Date;
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

		// we clone the array list since we do not want to surprise users
		// when another account is added between lines.
		ArrayList<Account> accounts = (ArrayList<Account>) (user.getAccounts().clone());
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

	/**
	 * Request the creation of the account type specified by `query`
	 * @param query the type of the account
	 */
	void requestToCreate(String query) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}

		// get a fake account, just to make sure this type exists.
		Account fakeAcc = accountFactory.getAccount(query, 0
			, ui.getBankSystem().getCurrentTime(), "", null);
		if (fakeAcc == null) {
			ui.getError().println(query + " is not a valid type of account.");
			return;
		}
		Request request = new Request(user, query, "Requested"
			+ ui.getBankSystem().getCurrentTime().toString());
		ui.getBankSystem().getRequests().add(request);
		ui.getOutput().println("The creation of accounts has been sent. " +
			"Please wait for bank managers to process it.");
	}
}
