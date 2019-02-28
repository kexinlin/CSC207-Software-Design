package atm;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineUI implements UI {
	private final boolean allowReadingFromFile = true;
	private ATM machine;
	private BufferedReader reader;
	public CommandLineUI(ATM machine) {
		this.machine = machine;
		this.reader = new BufferedReader(new InputStreamReader(System.in));
	}

	public void mainLoop() {
		System.out.println("Welcome. Please enter `help` for help, `exit` to quit.");
		REPL:
		while (true) {
			System.out.print("> ");
			String command;
			try {
				command = reader.readLine().trim();
			} catch (IOException e) {
				System.err.println("Cannot read command.");
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

				default:
					System.err.println("Unknown command: " + command);
					break;
			}
		}
	}

	/**
	 *
	 */
	private void login() {
		try {
			Console console = System.console();
			String username, password;
			if (console == null) {
				if (! allowReadingFromFile) {
					System.err.println("Error: console not found");
					return;
				} else {
					System.out.println("Enter username:");
					username = reader.readLine();
					System.out.println("Enter password:");
					password = reader.readLine();
				}
			} else {
				System.out.println("Enter username:");
				username = console.readLine();
				System.out.println("Enter password:");
				// FIXME: potentially dangerous.
				// Oracle suggests using char[].
				password = new String(console.readPassword());
			}
			if (machine.login(username, password)) {
				System.out.println("Login successful.");
			} else {
				System.err.println("Login failed.");
			}
		} catch (IOException e) {
			System.err.println("Cannot read username and/or password.");
		}
	}

	/**
	 * Displays help information.
	 */
	private void help() {
		System.out.println(
			"login\t-\tLog in as a user or admin\n"
			+ "help\t-\tDisplay this help information\n"
			+ "exit\t-\tQuit the program\n");
	}
}
