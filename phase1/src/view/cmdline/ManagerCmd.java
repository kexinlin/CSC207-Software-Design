package view.cmdline;

import model.Request;
import model.persons.BankManager;
import model.persons.User;

import java.io.IOException;

public class ManagerCmd {
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
		BankManager manager = ui.checkBankManagerLogin();
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
		BankManager manager = ui.checkBankManagerLogin();
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

		User user = new User(ui.getBankSystem(), name, username, password);
		ui.getBankSystem().addLoginable(user);
		ui.getOutput().println("Successfully added user.");
	}
}
