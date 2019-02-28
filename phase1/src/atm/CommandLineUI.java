package atm;

import java.io.*;

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
			String command;
			try {
				command = reader.readLine().trim();
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

				default:
					error.println("Unknown command: " + command);
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
			output.println("Enter username:");
			username = reader.readLine();

			if (readPasswordFromConsole) {
				if (console == null) {
					error.println("Error: console not found");
					return;
				} else {
					output.println("Enter password:");
					password = reader.readLine();
				}
			} else {
				output.println("Enter password:");
				// FIXME: potentially dangerous.
				// Oracle suggests using char[].
				password = new String(console.readPassword());
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
			+ "exit\t-\tQuit the program\n");
	}
}
