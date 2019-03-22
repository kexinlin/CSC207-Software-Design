package controller.transactions;

import controller.ATM;
import model.Cash;
import model.transactors.Account;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileWithdrawController implements WithdrawController {
	private ATM atm;
	private String withdrawFileName;
	public FileWithdrawController(ATM atm) {
		this.atm = atm;
		withdrawFileName = "withdraw.txt";
	}

	/**
	 * Gets the atm.
	 * @return the atm.
	 */
	public ATM getATM() {
		return atm;
	}

	/**
	 * Sets the atm.
	 * @param atm the ATM to set to.
	 */
	public void setATM(ATM atm) {
		this.atm = atm;
	}

	/**
	 * Sets the transactions file name.
	 * @param withdrawFileName the file name to set to.
	 */
	public void setWithdrawFileName(String withdrawFileName) {
		this.withdrawFileName = withdrawFileName;
	}

	/**
	 * Gets the transactions file name.
	 * @return the transactions file name.
	 */
	public String getWithdrawFileName() {
		return withdrawFileName;
	}
	/**
	 * Take out money from the machine.
	 * @param acc the account to take money from
	 * @param amount the amount of money
	 * @throws InvalidOperationException
	 */
	@Override
	public void withdrawMoney(HashMap<Cash, Integer> cashMap)
		throws InvalidOperationException, InsufficientCashException {
		writeWithdrawFile(cashMap);
	}

	/**
	 * Write a file to record withdrawal information.
	 * @param cashMap a HashMap that maps the number of bills withdrawn
	 * @throws InvalidOperationException when error occurs while writing file
	 */
	private void writeWithdrawFile(HashMap<Cash, Integer> cashMap)
		throws InvalidOperationException {
		File file = new File(withdrawFileName);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			String string = "cash," +
				String.join(",", cashMap.entrySet().stream().map(kv ->
					kv.getKey().getNumVal() + " " + kv.getValue()).toArray(String[]::new));
			writer.write(string);
			writer.close();
		} catch (IOException e) {
			throw new InvalidOperationException("Error writing file: " + e);
		}
	}
}
