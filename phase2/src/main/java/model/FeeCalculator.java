package model;

import model.transactions.Transaction;

public interface FeeCalculator {
	Money getFee(Transaction tx);
}
