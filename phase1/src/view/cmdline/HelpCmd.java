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
			+ "request\t-\tShow or process requests to create account\n"
			+ "mkaccount\t-\tRequest the bank manager to create an account\n"
			+ "msg\t-\tRead and delete messages\n"
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

		commandHelp.put("request", "request -- show or process requests\n" +
			"Usage: request [INDEX [y/n]]\n" +
			"\n" +
			"When used on its own, show all requests.\n" +
			"When used with INDEX, show the request at INDEX.\n" +
			"When used with INDEX and y, accept the user's request, and an account " +
			"will be created for the user.\n" +
			"When used with INDEX and n, decline the user's request, and a message " +
			"will be sent to the user.\n" +
			"\n" +
			"You must log in as a bank manager to use this command.\n" +
			"\n" +
			"Examples:\n" +
			"request\n" +
			"request 0\n" +
			"request 2 n\n");

		commandHelp.put("mkaccount", "mkaccount -- request to create an account\n" +
			"Usage: mkaccount TYPE\n" +
			"\n" +
			"Request the bank manager to create a account of TYPE.\n" +
			"The available TYPE strings are:\n" +
			"chq -- chequing accounts\n" +
			"sav -- saving accounts\n" +
			"cre -- credit card accounts\n" +
			"loc -- line of credit accounts\n" +
			"\n" +
			"You must log in as a user to use this command.\n" +
			"\n" +
			"Examples:\n" +
			"mkaccount chq\n" +
			"mkaccount sav\n");

		commandHelp.put("msg", "msg -- read and delete messages\n" +
			"Usage: msg [INDEX]\n" +
			"\n" +
			"When INDEX is not specified, read all messages.\n" +
			"WHEN INDEX is specified, delete the message at INDEX.\n" +
			"\n" +
			"You must log in as a user to use this command.\n" +
			"\n" +
			"Examples:\n" +
			"msg\n" +
			"msg 0\n");
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
