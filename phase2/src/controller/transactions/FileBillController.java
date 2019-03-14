package controller.transactions;

import controller.BankSystem;
import model.accounts.Account;
import model.exceptions.InvalidOperationException;

import java.io.*;

public class FileBillController implements BillController {
	private String billFileName;
	public FileBillController(BankSystem sys) {
		this.billFileName = "outgoing.txt";
	}

	/**
	 * Gets the file name for outgoing bills
	 * @return the file name
	 */
	public String getBillFileName() {
		return billFileName;
	}

	/**
	 * Sets the file name for outgoing bills.
	 * @param billFileName the file name.
	 */
	public void setBillFileName(String billFileName) {
		this.billFileName = billFileName;
	}

	/**
	 * Records the payment.
	 * The payment will be recorded as the following format:
	 *
	 * AccountID,Username,payeeName,amount
	 *
	 * One line in the file indicates one payment.
	 *
	 * @param account the account to pay from
	 * @param payeeName the name of the payee
	 * @param amount the amount of money to pay
	 * @throws InvalidOperationException
	 */
	public void recordPayment(Account account, String payeeName, double amount)
		throws InvalidOperationException {
		try {
			File file = new File(billFileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(account.getAccountId()
				+ "," + account.getOwner().getUsername()
				+ "," + payeeName + "," + amount + "\n");
			writer.close();
		} catch (IOException e) {
			throw new InvalidOperationException("Cannot record payment: " + e);
		}
	}
}
