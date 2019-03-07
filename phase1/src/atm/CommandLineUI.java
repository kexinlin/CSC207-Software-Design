package atm;

import java.io.*;
import java.util.Collection;

public class CommandLineUI implements UI {
	private boolean readPasswordFromConsole;
	private ATM machine;
	private BufferedReader reader;
	private InputStream input;
	private PrintStream output;
	private PrintStream error;

	/**
	 * Constructs a new Command Line UI.
	 * @param machine the ATM to handle
	 * @param input the input stream
	 * @param output the output stream
	 * @param error the error stream
	 * @param readPasswordFromConsole whether to read password only from a console
	 */
	public CommandLineUI(ATM machine, InputStream input,
						 PrintStream output, PrintStream error,
						 boolean readPasswordFromConsole) {
		this.machine = machine;
		this.input = input;
		this.reader = new BufferedReader(new InputStreamReader(this.input));
		this.output = output;
		this.error = error;
		this.readPasswordFromConsole = readPasswordFromConsole;
	}

	public void mainLoop() {
		output.println("Welcome. Please enter `help` for help, `exit` to quit.");
		REPL:
		while (true) {
			output.print("> ");
			String command, args;
			try {
				String line = reader.readLine().trim();
				String s[] = line.split("\\s", 2);
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
					help();
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

				default:
					error.println("Unknown command: " + command);
					break;
			}
		}
	}

	/**
	 * Prompts the user to enter login info, then log in to the account.
	 */
	private void login() {
		try {
			Console console = System.console();
			String username, password;
			output.println("Enter username:");
			username = reader.readLine();

			if (readPasswordFromConsole) {
				if (console == null) {
					error.println("Error: console not found");
					return;
				} else {
					output.println("Enter password:");
					password = new String(console.readPassword());
				}
			} else {
				output.println("Enter password:");
				// FIXME: potentially dangerous.
				// Oracle suggests using char[].
				password = reader.readLine();
			}
			if (machine.login(username, password)) {
				output.println("Login successful.");
			} else {
				error.println("Login failed.");
			}
		} catch (IOException e) {
			error.println("Cannot read username and/or password.");
		}
	}

	/**
	 * Displays help information.
	 */
	private void help() {
		output.println(
			"login\t-\tLog in as a user or admin\n"
				+ "help\t-\tDisplay this help information\n"
				+ "ls  \t-\tList accounts\n"
				+ "mv  \t-\tTransfer between your accounts\n"
				+ "exit\t-\tQuit the program\n"
				+ "Enter `help COMMAND` for a detailed description for that command.\n");
	}

	/**
	 * Check whether a user has logged in.
	 * @return the currently logged-in user, or null if no user is logged in.
	 */
	private User checkUserLogin() {
		Loginable loggedIn = machine.currentLoggedIn();
		if (! (loggedIn instanceof User)) {
			error.println("The current individual logged in is not a user.");
			return null;
		}
		return (User)loggedIn;
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
		Collection<Account> accounts = user.getAccounts();
		int i = 0;
		for (Account acc : accounts) {
			String typeStr = getAccountType(acc);
			// TODO cache the codes
			output.println(typeStr + i + ":\t"
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
		return acc.getClass().getName();
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
		Transaction tx = new TransferTransaction(amount, source, dest);

		if (machine.proceedTransaction(tx)) {
			output.println("Transaction succeeded.");
		} else {
			error.println("Transaction failed.");
		}
	}

	/**
	 * Search for `query` in all accounts.
	 * @param query Account ID. TODO add account code for searching
	 * @return the account matches `query`.
	 */
	private Account searchAccount(String query) {
		Account res;
		try {
			res = machine.getAccountById(query);
		} catch (AccountNotExistException e) {
			return null;
		}
		return res;
	}
}
