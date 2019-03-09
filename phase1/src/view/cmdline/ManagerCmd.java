package view.cmdline;

import model.Request;
import model.persons.BankManager;

public class ManagerCmd {
	private CommandLineUI ui;

	ManagerCmd(CommandLineUI ui) {
		this.ui = ui;
	}

	private void showRequest(Request request) {
		if (request == null) {
			return;
		}
		ui.getOutput().println(request.getUser().getUsername()
			+ " asked to create an account of type "
			+ request.getAccountType() + ". Msg: "
			+ request.getMsg());
	}

	private void showRequests() {
		int i = 0;
		for (Request req : ui.getBankSystem().getRequests()) {
			ui.getOutput().print("Req #" + i + ": ");
			showRequest(req);
			++i;
		}
	}

	private Request getRequest(String s) {
		int order;
		try {
			order = Integer.valueOf(s);
			return ui.getATM().getBankSystem().getRequests().get(order);
		} catch (NumberFormatException e) {
			ui.getError().println(s + " is not in correct format.");
			return null;
		} catch (IndexOutOfBoundsException e) {
			ui.getError().println("Request #" + s + " not found.");
			return null;
		}
	}

	private void processRequest(String query, String result) {
		Request req = getRequest(query);
		if (req == null) {
			return;
		}
		boolean accepted;
		switch (result) {
			case "y":
				accepted = true;
				break;

			case "n":
				accepted = false;
				break;

			default:
				ui.getError().println("The result must be y or n.");
				return;
		}

		ui.getBankSystem().processRequest(req, accepted);
	}

	void processRequests(String data) {
		BankManager manager = ui.checkBankManagerLogin();
		if (manager == null) {
			return;
		}
		if (data.length() == 0) {
			showRequests();
		} else {
			String[] entries = data.split("\\s+", 2);
			if (entries.length == 1) {
				showRequest(getRequest(entries[0]));
			} else { // len == 2
				processRequest(entries[0], entries[1]);
			}
		}
	}
}
