package model.transactors;

import controller.BankSystem;
import model.Money;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;

public class OutgoingBill extends Transactor {
	private BankSystem system;
	private String payee;
	public OutgoingBill(BankSystem system, String payee) {
		this.system = system;
		this.payee = payee;
	}
	public String getId() {
		return "<bill-" + payee + ">";
	}

	@Override
	public void takeMoneyOut(Money amount)
		throws InvalidOperationException {
		throw new InvalidOperationException("Cannot take money out of a paid bill.");
	}

	@Override
	public void putMoneyIn(Money amount) throws InvalidOperationException {
		system.getBillController().recordPayment(payee, amount.getMoneyValue());
	}
}
