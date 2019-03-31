package model;

import model.transactions.Transaction;

public class NormalFeeCalculator implements FeeCalculator {
	@Override
	public Money getFee(Transaction tx) {
		return tx.getSource().getFeeFor(tx);
	}
}
