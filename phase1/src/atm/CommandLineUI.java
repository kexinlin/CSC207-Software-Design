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
				+ "ls\t-\tList accounts\n"
				+ "exit\t-\tQuit the program\n");
	}

	/**
	 * List accounts of current user.
	 * Must log in as a user to use.
	 */
	private void listAccounts() {
		Loginable loggedIn = machine.currentLoggedIn();
		if (! (loggedIn instanceof User)) {
			error.println("The current individual logged in is not a user.");
			return;
		}

		User user = (User) loggedIn;
		Collection<Account> accounts = user.getAccounts();
		int i = 0;
		for (Account acc : accounts) {
			String typeStr = getAccountType(acc);
			output.println(i + ":\t" + acc.getAccountId() + "\t" + acc.getBalance());
			++i;
		}
	}

	private String getAccountType(Account acc) {
		return acc.getClass().toString();
	}

	/**
	 * Gets the information about a certain account
	 * @param account the id or order of account
	 */
	private void showAccount(String account) {
		// TODO: should do acc id-order check
	}
}
