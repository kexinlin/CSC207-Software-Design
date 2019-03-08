package view.cmdline;

import model.accounts.*;
import model.persons.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountsCmd {
	private CommandLineUI ui;

	/**
	 * Constructs the accounts command
	 * @param ui the ui to use
	 */
	AccountsCmd(CommandLineUI ui) {
		this.ui = ui;
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
			String typeStr = getAccountType(acc);
			// TODO cache the codes
			ui.getOutput().println(typeStr + i + "\t\t"
				+ acc.getAccountId() + "\t" + acc.getBalance());
			++i;
		}
	}

	/**
	 * Gets the account type for `acc`
	 * @param acc the account
	 * @return the type of `acc`
	 */
	String getAccountType(Account acc) {
		HashMap<Class, String> nameMap = new HashMap<>();
		nameMap.put(ChequingAccount.class, "chq");
		nameMap.put(SavingAccount.class, "sav");
		nameMap.put(CreditCardAccount.class, "cre");
		nameMap.put(LineOfCreditAccount.class, "loc");
		return nameMap.get(acc.getClass());
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

		ui.getOutput().println(acc.getAccountId() + ": " + getAccountType(acc)
			+ ": " + acc.getBalance());
	}
}
