package view.cmdline;

import controller.ATM;
import controller.BankSystem;
import model.accounts.*;
import model.persons.BankManager;
import model.persons.Loginable;
import model.persons.User;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import view.UI;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineUI implements UI {
	private boolean readPasswordFromConsole;
	private ATM machine;
	private BufferedReader reader;
	private InputStream input;
	private PrintStream output;
	private PrintStream error;
	private ArrayList<Account> curAccounts;
	private HelpCmd helpCmd;

	/**
	 * Constructs a new Command Line UI.
	 * @param atm the machine to handle
	 * @param input the input stream
	 * @param output the output stream
	 * @param error the error stream
	 * @param readPasswordFromConsole whether to read password only from a console
	 */
	public CommandLineUI(ATM atm, InputStream input,
						 PrintStream output, PrintStream error,
						 boolean readPasswordFromConsole) {
		this.machine = atm;
		this.input = input;
		this.reader = new BufferedReader(new InputStreamReader(this.input));
		this.output = output;
		this.error = error;
		this.readPasswordFromConsole = readPasswordFromConsole;
		this.curAccounts = new ArrayList<>();
		this.helpCmd = new HelpCmd(this);
	}

	public InputStream getInput() {
		return input;
	}

	public PrintStream getOutput() {
		return output;
	}

	public PrintStream getError() {
		return error;
	}

	/**
	 * Gets the bank system for this UI.
	 * @return the bank system.
	 */
	private BankSystem getBankSystem() {
		return this.machine.getBankSystem();
	}
	/**
	 * Starts the main loop.
	 */
	public void mainLoop() {
		output.println("Welcome. Please enter `help` for help, `exit` to quit.");
		REPL:
		while (true) {
			output.print("> ");
			String command, args;
			try {
				String line = reader.readLine().trim();
				String[] s = line.split("\\s", 2);
				command = s[0];
				args = s.length == 1 ? "" : s[1];
			} catch (IOException e) {
				error.println("Cannot read command.");
				break;
			}
			switch (command) {
				case "login":
					login();
					break;

				case "help":
					helpCmd.help(args);
					break;

				case "exit":
					break REPL;

				case "ls":
					if (args.length() > 0) {
						showAccount(args);
					} else {
						listAccounts();
					}
					break;

				case "mv":
					moveMoney(args);
					break;

				case "passwd":
					changePassword(args);
					break;

				case "deposit":
					deposit(args);
					break;

				case "paybill":
					payBill(args);
					break;

				default:
					error.println("Unknown command: " + command);
					break;
			}
		}
	}

	/**
	 * Read password from console, or `input` if there is no console and
	 * we allow to read from things other than a console
	 * @param prompt The string to prompt the individual to input a password
	 * @return the password we have read
	 */

	private String readPassword(String prompt) {
		String password;
		Console console = System.console();
		output.print(prompt);
		if (readPasswordFromConsole) {
			if (console == null) {
				error.println("Error: console not found");
				return null;
			} else {
				password = new String(console.readPassword());
			}
		} else {
			// FIXME: potentially dangerous.
			// Oracle suggests using char[].
			try {
				password = reader.readLine();
			} catch (IOException e) {
				password = null;
			}
		}
		return password;
	}

	/**
	 * Prompts the user to enter login info, then log in to the account.
	 */
	private void login() {
		String username, password;
		output.print("Enter username: ");
		try {
			username = reader.readLine();
		} catch (IOException e) {
			error.println("Cannot read username.");
			return;
		}
		password = readPassword("Enter password: ");
		if (password == null) {
			error.println("Cannot read password.");
			return;
		}
		if (machine.login(username, password)) {
			output.println("Login successful.");
		} else {
			error.println("Login failed.");
		}
	}

	/**
	 * Check whether a user has logged in.
	 * @return the currently logged-in user, or null if no user is logged in.
	 */
	User checkUserLogin() {
		Loginable loggedIn = machine.currentLoggedIn();
		if (! (loggedIn instanceof User)) {
			error.println("The current individual logged in is not a user.");
			return null;
		}
		return (User)loggedIn;
	}

	/**
	 * Check whether a bank manager has logged in.
	 * @return the currently logged-in bank manager, or null if no bank manager
	 * is logged in.
	 */
	BankManager checkBankManagerLogin() {
		Loginable loggedIn = machine.currentLoggedIn();
		if (! (loggedIn instanceof BankManager)) {
			error.println("The current individual logged in is not a bank manager.");
			return null;
		}
		return (BankManager)loggedIn;
	}

	/**
	 * List accounts of current user.
	 * Must log in as a user to use.
	 */
	private void listAccounts() {
		User user = checkUserLogin();
		if (user == null) {
			return;
		}

		ArrayList<Account> accounts = user.getAccounts();
		// to be thread-safe
		curAccounts = accounts;

		int i = 0;
		output.println("Type & Order\tID\tBalance");
		for (Account acc : accounts) {
			String typeStr = getAccountType(acc);
			// TODO cache the codes
			output.println(typeStr + i + "\t\t"
				+ acc.getAccountId() + "\t" + acc.getBalance());
			++i;
		}
	}

	/**
	 * Gets the account type for `acc`
	 * @param acc the account
	 * @return the type of `acc`
	 */
	private String getAccountType(Account acc) {
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
	private void showAccount(String query) {
		User u = checkUserLogin();
		if (u == null) {
			return;
		}
		// TODO: should do acc id-order check
		Account acc = searchAccount(query);
		if (acc == null) {
			error.println("No such account found.");
			return;
		}

		output.println(acc.getAccountId() + ": " + getAccountType(acc)
			+ ": " + acc.getBalance());
	}

	/**
	 * Transfer money from one account to another.
	 * @param args the arguments from the command line.
	 */
	private void moveMoney(String args) {
		User user = checkUserLogin();
		if (user == null) {
			return;
		}
		String[] arr = args.split("\\s+", 3);
		if (arr.length < 3) {
			error.println("Not enough arguments. Type `help mv` for detailed usage.");
			return;
		}
		Account source = searchAccount(arr[0]);
		Account dest = searchAccount(arr[1]);
		double amount = Double.valueOf(arr[2]);
		if (source == null) {
			error.println("Source account not found.");
			return;
		}
		if (dest == null) {
			error.println("Destination account not found.");
			return;
		}

		// make sure the account belongs to the user
		if (! source.getOwner().equals(user)) {
			error.println("The source account must be yours.");
			return;
		}

		// after all checking, do the transaction
		try {
			getBankSystem().transferMoney(source, dest, amount);
			output.println("Transaction succeeded.");
		} catch (InvalidOperationException e) {
			error.println("Transaction failed. " + e);
		} catch (NoEnoughMoneyException e) {
			error.println("Transaction failed. " + e);
		}
	}

	/**
	 * Search for `query` in all accounts.
	 * @param query Account ID. TODO add account code for searching
	 * @return the account matches `query`.
	 */
	private Account searchAccount(String query) {
		Pattern regex = Pattern.compile("^([a-z]{3})(\\d+)$");
		Matcher matcher = regex.matcher(query);
		if (matcher.matches()) { // looks like a `type-order`
			try {
				Account acc = curAccounts.get(Integer.valueOf(matcher.group(2)));
				if (getAccountType(acc).equals(matcher.group(1))) {
					// we found it
					return acc;
				}
			} catch(IndexOutOfBoundsException e) {
				return null;
			}
		}
		try {
			return getBankSystem().getAccountById(query);
		} catch (AccountNotExistException e) {
			return null;
		}
	}

	/**
	 * Change password for this person, or another person if the current
	 * logged-in individual is a bank manager.
	 */
	private void changePassword(String username) {
		Loginable loggedIn = machine.currentLoggedIn();
		if (loggedIn == null) {
			error.println("You are not logged in.");
			return;
		}
		Loginable personToChangePassword = loggedIn;
		if (username.length() > 0) {
			personToChangePassword = getBankSystem().getLoginable(username);
			if (personToChangePassword == null) {
				error.println("No user named `" + username + "' exists.");
				return;
			}
			// users can only change *their* password
			if (loggedIn instanceof User
				&& !username.equals(loggedIn.getUsername())) {
				error.println("You cannot change other user's password.");
				return;
			}
		}

		String password = readPassword("Enter password: ");
		if (password == null) {
			error.println("Cannot read password.");
			return;
		}
		String confPass = readPassword("Repeat password: ");
		if (confPass == null) {
			error.println("Cannot read password.");
			return;
		}

		if (! password.equals(confPass)) {
			error.println("Passwords do not match.");
			return;
		}

		personToChangePassword.setPassword(password);
		output.println("Password changed successfully.");
	}

	/**
	 * Deposits the cash or cheque in the transactions file into the
	 * account specified by `query`
	 * @param query the query string for the account
	 */
	private void deposit(String query) {
		User user = checkUserLogin();
		if (user == null) {
			return;
		}
		Account acc = searchAccount(query);
		if (acc == null) {
			error.println("No such account.");
			return;
		}
		if (! user.equals(acc.getOwner())) {
			error.println("You can only deposit into your own account.");
			return;
		}
		try {
			machine.getDepositController().depositMoney(acc);
		} catch (InvalidOperationException e) {
			error.println("Error making a deposit: " + e);
			return;
		}
		output.println("Deposit successful.");
	}

	private void payBill(String args) {
		User user = checkUserLogin();
		if (user == null) {
			return;
		}

		String[] d = args.split("\\s+");
		if (d.length != 3) {
			error.println("Error: wrong number of arguments.");
			return;
		}

		Account acc = searchAccount(d[0]);
		if (acc == null) {
			error.println("Error: Account not found.");
			return;
		}
		if (! user.equals(acc.getOwner())) {
			error.println("Error: the account is not yours.");
			return;
		}
		String payeeName = d[1];
		double amount;
		try {
			amount = Double.valueOf(d[2]);
		} catch (NumberFormatException e) {
			error.println("Error: value is not valid.");
			return;
		}
		try {
			getBankSystem().payBill(acc, payeeName, amount);
			output.println("Payment succeeded.");
		} catch (NoEnoughMoneyException e) {
			error.println("Error: Your account does not have enough money.");
		} catch (InvalidOperationException e) {
			error.println("Error: " + e);
		}
	}
}
