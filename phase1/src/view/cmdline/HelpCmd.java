package view.cmdline;

import java.util.HashMap;

class HelpCmd {
	private HashMap<String, String> commandHelp;
	private CommandLineUI ui;

	/**
	 * Constructs the help command
	 * @param ui the ui to use
	 */
	HelpCmd(CommandLineUI ui) {
		this.ui = ui;
		commandHelp = new HashMap<>();

		commandHelp.put("", "login\t-\tLog in as a user or admin\n"
			+ "help\t-\tDisplay this help information\n"
			+ "ls  \t-\tList accounts\n"
			+ "mv  \t-\tTransfer between your accounts\n"
			+ "passwd\t-\tChange password\n"
			+ "exit\t-\tQuit the program\n"
			+ "deposit\t-\tDeposit cash or cheque\n"
			+ "paybill\t-\tPay a bill to a payee outside the bank\n"
			+ "Enter `help COMMAND` for a detailed description for that command.\n");

		commandHelp.put("login", "login -- log into the system\n" +
			"Usage: login\n" +
			"\n" +
			"You will be prompted to enter your username and password. " +
			"If your login information is correct, you will be logged in " +
			"to the machine.\n");
		commandHelp.put("help", "help -- display help\n" +
			"Usage: help [COMMAND]\n" +
			"\n" +
			"If COMMAND presents, display the help information for COMMAND.\n" +
			"Otherwise, display a list of available commands.\n");

		commandHelp.put("ls", "ls -- list or show accounts\n" +
			"Usage: ls [QUERY]\n" +
			"\n" +
			"If QUERY presents, display the information about the account which " +
			"matches QUERY.\n" +
			"Otherwise, show a list of your accounts.\n" +
			"Must be logged in as a user to use this command.\n" +
			"\n" +
			"QUERY string can be the account ID of your account, or a `type order` " +
			"format, i.e. the first" +
			"column of what `ls` displays. It consists of two parts, the " +
			"short name of account type, and the order in the list. For example, " +
			"`chq0` is a `type order` string. A `type order` string is only effective " +
			"after you use a bare `ls`, and the order might change between different " +
			"`ls`s. This program will cache the type and order every time after you " +
			" `ls`. Only the cached info will be queried.\n");

		commandHelp.put("mv", "mv -- move between accounts\n" +
			"Usage: mv FROM-QUERY TO-QUERY AMOUNT\n" +
			"\n" +
			"Move AMOUNT of money from the account denoted by FROM-QUERY " +
			"to the account denoted by TO-QUERY.\n" +
			"\n" +
			"Must log in as a user to use this command.\n" +
			"The account represented by FROM-QUERY must be yours.\n" +
			"For more information about QUERY strings, type `help ls`.\n");

		commandHelp.put("passwd","passwd -- change password\n" +
			"Usage: passwd [USERNAME]\n" +
			"\n" +
			"If USERNAME presents, set the password of the person with USERNAME.\n" +
			"Otherwise, set the password of the currently logged-in individual.\n" +
			"\n" +
			"You will be prompted to enter your password twice.\n" +
			"The two passwords must match.\n" +
			"Must log in to use this command.\n" +
			"If you log in as a user, you can only change your password. If you log " +
			"in as a bank manager, you can change any person's password.\n");

		commandHelp.put("exit", "exit -- exit the program\n" +
			"Usage: exit\n" +
			"\n" +
			"Exit the program. Save all changes.\n");

		commandHelp.put("deposit", "deposit -- deposit cash or cheque\n" +
			"Usage: deposit QUERY\n" +
			"\n" +
			"Deposit the cash or cheque you put into the machine.\n" +
			"The money will go to the account which matches QUERY.\n" +
			"You must log in as a user to use this command.\n" +
			"The account which matches QUERY must be yours.\n" +
			"\n" +
			"For more information on QUERY strings, type `help ls`.\n");

		commandHelp.put("paybill", "paybill -- pay a bill\n" +
			"Usage: paybill QUERY PAYEE-NAME AMOUNT\n" +
			"\n" +
			"Pay AMOUNT of money from the account matching QUERY to " +
			"the payee with PAYEE-NAME.\n" +
			"\n" +
			"You must log in as a user to use this command.\n" +
			"The account which matches QUERY must be yours.\n" +
			"\n" +
			"For more information on QUERY strings, type `help ls`.\n");

	}

	/**
	 * Displays help information.
	 */
	void help(String args) {
		String helpStr = commandHelp.get(args);
		if (helpStr == null) {
			ui.getError().println("Error: Cannot find help for command `" + args + "`.");
			return;
		}
		ui.getOutput().println(helpStr);
	}
}
