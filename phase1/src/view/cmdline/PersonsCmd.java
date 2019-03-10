package view.cmdline;

import model.Message;
import model.accounts.Account;
import model.accounts.ChequingAccount;
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

	private void showMessage(Message Message) {
		if (Message == null) {
			return;
		}
		ui.getOutput().println(Message.getText());
	}

	private void showMessages(User user) {
		int i = 0;
		for (Message msg : user.getMessages()) {
			ui.getOutput().print("Msg #" + i + ": ");
			showMessage(msg);
			++i;
		}
	}

	private Message getMessage(User user, String s) {
		int order;
		try {
			order = Integer.valueOf(s);
			return user.getMessages().get(order);
		} catch (NumberFormatException e) {
			ui.getError().println(s + " is not in correct format.");
			return null;
		} catch (IndexOutOfBoundsException e) {
			ui.getError().println("Message #" + s + " not found.");
			return null;
		}
	}

	private void deleteMessage(User user, String query) {
		Message msg = getMessage(user, query);
		if (msg == null) {
			return;
		}
		ui.getBankSystem().removeMessage(msg);
	}

	void processMessages(String data) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}
		if (data.length() == 0) {
			showMessages(user);
		} else {
			deleteMessage(user, data);
		}
	}

	void logout() {
		ui.getATM().logout();
		ui.getOutput().println("Logged out successfully.");
	}

	void setPrimary(String data) {
		User user = ui.checkUserLogin();
		if (user == null) {
			return;
		}
		if (data.length() == 0) {
			Account acc = user.getPrimaryChequingAccount();
			if (acc == null) {
				ui.getOutput().println("You do not have a primary chequing account.");
				return;
			}
			ui.getOutput().println("Your primary account is: " + acc.getAccountId());
			return;
		}
		Account acc = ui.searchAccount(data);

		if (acc == null || ! acc.getOwner().equals(user)) {
			ui.getError().println("Account is not found, or does not belong to you.");
			return;
		}

		if (! (acc instanceof ChequingAccount)) {
			ui.getError().println("This is not a chequing account.");
			return;
		}

		user.setPrimaryCheuqingAccount((ChequingAccount) acc);
		ui.getOutput().println("Primary account successfully set.");
	}
}
