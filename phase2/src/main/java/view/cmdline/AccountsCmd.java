package view.cmdline;

import controller.AccountFactory;
import model.Money;
import model.Request;
import model.persons.AccountOwner;
import model.transactors.*;
import model.exceptions.NoTransactionException;
import model.persons.Loginable;
import model.transactions.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

class AccountsCmd {
	private CommandLineUI ui;
	private AccountFactory accountFactory;

	/**
	 * Constructs the transactors command
	 * @param ui the ui to use
	 */
	AccountsCmd(CommandLineUI ui) {
		this.ui = ui;
		this.accountFactory = new AccountFactory();
	}

	/**
	 * List transactors of current user.
	 * Must log in as a user to use.
	 */
	void listAccounts() {
		Loginable l = ui.getATM().currentLoggedIn();
		if (l == null) {
			ui.getError().println("You must log in to use this command.");
			return;
		}

		ArrayList<Account> accounts;
		// the user can only view their own transactors,
		// the bank manager can view all people's transactors.
		if (l instanceof AccountOwner) {
			// we clone the array list since we do not want to surprise users
			// when another account is added between lines.
			accounts = new ArrayList<>(((AccountOwner) l).getAccounts());
		} else {
			accounts = new ArrayList<>(ui.getBankSystem().getAccounts().values());
		}
		// to be thread-safe
		ui.setCurAccounts(accounts);

		int i = 0;
		if (!(l instanceof AccountOwner)) {
			ui.getOutput().print("Owner\t");
		}
		ui.getOutput().println("Type-Order\tID\tBalance");
		for (Account acc : accounts) {
			String typeStr = accountFactory.getAccountType(acc);
			if (! (l instanceof AccountOwner)) {
				ui.getOutput().print(acc.getOwner().getUsername() + "\t");
			}
			ui.getOutput().println(typeStr + i + "\t\t"
				+ acc.getId() + "\t" + acc.getBalance());
			++i;
		}
		if (l instanceof AccountOwner) {
			ui.getOutput().println("\n" +
				"Summary -- Net Total: " + ((AccountOwner) l).getNetTotal());
		}
	}

	/**
	 * Gets the information about a certain account
	 * @param query the id or order of account
	 */
	void showAccount(String query) {
		Loginable l = ui.getATM().currentLoggedIn();
		if (l == null) {
			ui.getError().println("You must log in to use this command.");
			return;
		}

		Account acc = ui.searchAccount(query);
		if (acc == null) {
			ui.getError().println("No such account found in your control.");
			return;
		}

		// the account does not belong to the user
		// use the same error message to ensure the user
		// does not intentionally guess other people's account id
		if (l instanceof AccountOwner && ! (acc.getOwner().equals(l))) {
			ui.getError().println("No such account found in your control.");
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat("MMM d, YYYY");

		ui.getOutput().println("Acc Id\tType\tBalance\tDate of Creation");
		ui.getOutput().println(acc.getId() + "\t" + accountFactory.getAccountType(acc)
			+ "\t" + acc.getBalance() + "\t" + format.format(acc.getDateOfCreation()));
		Transaction tx;
		try {
			tx = acc.getLastTrans();
		} catch (NoTransactionException e) {
			return;
		}
		ui.getOutput().println("Last Transaction: " + tx);
	}

	/**
	 * Request the creation of the account type specified by `query`
	 * @param query the type of the account
	 */
	void requestToCreate(String query) {
		AccountOwner user = ui.checkUserLogin();
		if (user == null) {
			return;
		}

		// get a fake account, just to make sure this type exists.
		Account fakeAcc = accountFactory.getAccount(query, new Money(0)
			, ui.getBankSystem().getCurrentTime(), "", null);
		if (fakeAcc == null) {
			ui.getError().println(query + " is not a valid type of account.");
			return;
		}
		Request request = new Request(user, query, "Requested "
			+ ui.getBankSystem().getCurrentTime().toString());
		ui.getBankSystem().getRequests().add(request);
		ui.getOutput().println("The creation of transactors has been sent. " +
			"Please wait for bank managers to process it.");
	}
}
