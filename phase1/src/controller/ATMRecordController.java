package controller;

import java.io.*;
import java.util.HashMap;

import model.Cash;

public class ATMRecordController {
	private ATM atm;
	private CashFactory cashFactory;

	/**
	 * Instantiate a new ATM controller.
	 *
	 * @param atm the atm that this controller controls
	 */
	ATMRecordController(ATM atm) {
		this.atm = atm;
		this.cashFactory = new CashFactory();
	}

	public File getRecordFile() {
		return new File(atm.getAtmRecordFileName());
	}


	/**
	 * Read the number of bills for each domination stored in this ATM.
	 *
	 * Format for this file is:
	 * DENOMINATION,NUMBER OF BILLS
	 *
	 * Possible denominations are: 5, 10, 20, and 50
	 */
	public void readRecords() {
		File file = getRecordFile();
		HashMap<Cash, Integer> inputCash = new HashMap<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			Cash cash;
			int amount;
			while ((line = reader.readLine()) != null) {
				String[] entries = line.split(",");
				if (entries.length != 2) {
					// error
					continue;
				}
				try {
					cash = cashFactory.getCash(entries[0]);
					amount = Integer.valueOf(entries[1]);
					inputCash.put(cash, inputCash.get(cash) + amount);
				} catch (NumberFormatException e) {
				}
			}
		} catch (IOException e) {
		}

		atm.stockCash(inputCash);
	}

	/**
	 * Write the cash storing record into ATM record file.
	 */
	public void writeRecords() {
		File file = getRecordFile();
		HashMap<Cash, Integer> cashStored = atm.getBillAmount();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (Cash cash : cashStored.keySet()) {
				writer.write(cash.getNumVal() + "," + cashStored.get(cash) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Cannot save records");
		}
	}
}
