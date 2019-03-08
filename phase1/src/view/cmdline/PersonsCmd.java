package view.cmdline;

import model.persons.Loginable;
import model.persons.User;

import java.io.IOException;

class PersonsCmd {
	private CommandLineUI ui;

	/**
	 * Constructs the log-in command
	 * @param ui the ui to use
	 */
	PersonsCmd(CommandLineUI ui) {
		this.ui = ui;
	}


	/**
	 * Prompts the user to enter login info, then log in to the account.
	 */
	void login() {
		String username, password;
		ui.getOutput().print("Enter username: ");
		try {
			username = ui.getReader().readLine();
		} catch (IOException e) {
			ui.getError().println("Cannot read username.");
			return;
		}
		password = ui.readPassword("Enter password: ");
		if (password == null) {
			ui.getError().println("Cannot read password.");
			return;
		}
		if (ui.getATM().login(username, password)) {
			ui.getOutput().println("Login successful.");
		} else {
			ui.getError().println("Login failed.");
		}
	}

	/**
	 * Change password for this person, or another person if the current
	 * logged-in individual is a bank manager.
	 * @param username
	 *
	 */
	void changePassword(String username) {
		Loginable loggedIn = ui.getATM().currentLoggedIn();
		if (loggedIn == null) {
			ui.getError().println("You are not logged in.");
			return;
		}
		Loginable personToChangePassword = loggedIn;
		if (username.length() > 0) {
			personToChangePassword = ui.getBankSystem().getLoginable(username);
			if (personToChangePassword == null) {
				ui.getError().println("No user named `" + username + "' exists.");
				return;
			}
			// users can only change *their* password
			if (loggedIn instanceof User
				&& !username.equals(loggedIn.getUsername())) {
				ui.getError().println("You cannot change other user's password.");
				return;
			}
		}

		String password = ui.readPassword("Enter password: ");
		if (password == null) {
			ui.getError().println("Cannot read password.");
			return;
		}
		String confPass = ui.readPassword("Repeat password: ");
		if (confPass == null) {
			ui.getError().println("Cannot read password.");
			return;
		}

		if (! password.equals(confPass)) {
			ui.getError().println("Passwords do not match.");
			return;
		}

		personToChangePassword.setPassword(password);
		ui.getOutput().println("Password changed successfully.");
	}
}
