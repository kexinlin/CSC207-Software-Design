package view.cmdline;

import controller.ATM;
import controller.AccountFactory;
import controller.BankSystem;
import model.accounts.*;
import model.persons.BankManager;
import model.persons.Loginable;
import model.persons.User;
import model.exceptions.AccountNotExistException;
import view.UI;

import java.io.*;
import java.util.ArrayList;
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
	private PersonsCmd personsCmd;
	private AccountsCmd accountsCmd;
	private TxCmd txCmd;
	private ManagerCmd managerCmd;

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
		this.personsCmd = new PersonsCmd(this);
		this.accountsCmd = new AccountsCmd(this);
		this.txCmd = new TxCmd(this);
		this.managerCmd = new ManagerCmd(this);
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

	public BufferedReader getReader() {
		return reader;
	}

	public ATM getATM() {
		return machine;
	}

	public ArrayList<Account> getCurAccounts() {
		return curAccounts;
	}

	public void setCurAccounts(ArrayList<Account> curAccounts) {
		this.curAccounts = curAccounts;
	}

	/**
	 * Gets the bank system for this UI.
	 * @return the bank system.
	 */
	BankSystem getBankSystem() {
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
					personsCmd.login();
					break;

				case "help":
					helpCmd.help(args);
					break;

				case "exit":
					break REPL;

				case "ls":
					if (args.length() > 0) {
						accountsCmd.showAccount(args);
					} else {
						accountsCmd.listAccounts();
					}
					break;

				case "mv":
					txCmd.moveMoney(args);
					break;

				case "passwd":
					personsCmd.changePassword(args);
					break;

				case "deposit":
					txCmd.deposit(args);
					break;

				case "paybill":
					txCmd.payBill(args);
					break;

				case "request":
					managerCmd.processRequests(args);
					break;

				case "mkaccount":
					accountsCmd.requestToCreate(args);
					break;

				case "msg":
					personsCmd.processMessages(args);
					break;

				case "adduser":
					managerCmd.addUser(args);
					break;

				case "logout":
					personsCmd.logout();
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

	String readPassword(String prompt) {
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
	 * Search for `query` in all accounts.
	 * @param query Account ID, `type-order`, or username
	 * @return the account matches `query`.
	 * if query is a username, return that user's primary chequing account.
	 */
	Account searchAccount(String query) {
		AccountFactory accountFactory = new AccountFactory();
		Pattern regex = Pattern.compile("^([a-z]{3})(\\d+)$");
		Matcher matcher = regex.matcher(query);
		if (matcher.matches()) { // looks like a `type-order`
			try {
				Account acc = curAccounts.get(Integer.valueOf(matcher.group(2)));
				if (accountFactory.getAccountType(acc).equals(matcher.group(1))) {
					// we found it
					return acc;
				}
			} catch(IndexOutOfBoundsException e) {
				return null;
			}
		}

		// not pure number, probably a username
		if (! query.matches("^\\d+$")) {
			Loginable l = getBankSystem().getLoginable(query);
			if (l instanceof User) {
				ChequingAccount acc = ((User) l).getPrimaryChequingAccount();
				if (acc != null) {
					return acc;
				}
			}
		}

		try {
			return getBankSystem().getAccountById(query);
		} catch (AccountNotExistException e) {
			return null;
		}
	}

}
