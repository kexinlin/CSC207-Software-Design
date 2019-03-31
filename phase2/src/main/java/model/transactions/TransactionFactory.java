package model.transactions;

import controller.BankSystem;
import model.Money;
import model.transactors.Transactor;

import java.util.Date;

public class TransactionFactory {
	/**
	 *
	 * @param data the recorded data, except the "tx,"
	 * @param bankSystem
	 * @return
	 */
	public Transaction fromRecord(String data, BankSystem bankSystem) {
		String[] entries = data.split(",", 5);
		if (entries.length < 4) {
			return null;
		}

		String fromAccId = entries[0];
		String toAccId = entries[1];
		Transactor fromAcc, toAcc;
		fromAcc = bankSystem.getTransactor(fromAccId);
		toAcc = bankSystem.getTransactor(toAccId);
		if (fromAcc == null || toAcc == null) {
			return null;
		}

		Date date;
		double amount;
		try {
			date = new Date(Long.valueOf(entries[2]));
			amount = Double.valueOf(entries[3]);
		} catch (NumberFormatException e) {
			return null;
		}
		if (entries.length == 5) {
			try {
				double fee = Double.valueOf(entries[4]);
				return new Transaction(new Money(amount), date, fromAcc,
					toAcc, new Money(fee));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return new Transaction(new Money(amount), date, fromAcc, toAcc);
	}
	public String toRecord(Transaction tx) {
		return String.join(",",
			"tx",
			tx.getSource().getId(),
			tx.getDest().getId(),
			String.valueOf(tx.getDate().getTime()),
			String.valueOf(tx.getAmount()),
			String.valueOf(tx.getFee()));
	}
}
