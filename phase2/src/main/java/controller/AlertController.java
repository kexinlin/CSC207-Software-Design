package controller;

import model.Cash;

import java.io.*;
import java.util.HashMap;

class AlertController {
	private BankSystem bankSystem;
	private ATM atm;

	/**
	 * Instantiate an alert controller.
	 *
	 * @param atm the atm that this alert controller controls
	 */
	AlertController(ATM atm, BankSystem bankSystem) {
		this.bankSystem = bankSystem;
		this.atm = atm;
	}

	/**
	 * Get the file for recording alert information
	 * @return
	 */
	private File getRecordFile() {
		return new File(atm.getAlertFileName());
	}

	/**
	 * Check whether the atm that this controller controls needs replenishment.
	 * Replenishment is needed when the amount of any denomination goes below 20.
	 *
	 * @return true when atm needs replenishment
	 */
	public boolean atmNeedReplenishment() {
		HashMap<Cash, Integer> billAmount = atm.getBillAmount();
		for (Cash cash : billAmount.keySet()) {
			if (billAmount.get(cash) < 20) {
				return true;
			}
		}
		return false;
	}

	/**
	 *  Send an alert to corresponding alert file.
	 *  Record information of timestamp and a summary of current bill amount in ATM machine.
	 */
	public void sendAlert() {
		File file = getRecordFile();
		StringBuilder cashSummary = new StringBuilder();
		for (Cash cash : Cash.values()) {
			cashSummary.append("Current number of ").append(cash.getNumVal()).append(" dollar bills: ")
				.append(atm.getBillAmount().get(cash)).append(". ");
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(bankSystem.getCurrentTimeStr() + ": ATM replenishment needed. " +
				cashSummary.toString() + "\n");
			writer.close();
		} catch (IOException e) {
			System.err.println("Cannot write alert." + e);
		}
	}



}
