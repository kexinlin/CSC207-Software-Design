package model.transactors;

import controller.ATM;
import model.CashCollection;
import model.Money;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;

public class MoneyEnvelope extends Transactor {
	private ATM atm;
	public MoneyEnvelope(ATM atm) {
		this.atm = atm;
	}

	public String getId() {
		return "<envelope>";
	}

	public double getBalance() {
		return 0;
	}
	/**
	 * Put money in the envelope, and, at the same time, take cash out of the
	 * machine.
	 * @param amount the amount of money to put in
	 * @throws InvalidOperationException
	 */
	@Override
	public void putMoneyIn(Money amount)
		throws InvalidOperationException {
		CashCollection col;
		if (amount instanceof CashCollection) {
			col = (CashCollection) amount;
			try {
				atm.withdrawCash(col.getCashMap());
			} catch (InsufficientCashException e) {
				e.printStackTrace();
				throw new InvalidOperationException(
					"Not enough cash in the machine.");
			}
		} else {
			throw new InvalidOperationException("Outgoing cheque is not yet supported.");
		}
	}

	@Override
	public void takeMoneyOut(Money amount)
		throws InvalidOperationException {
		if (amount instanceof CashCollection) {
			atm.stockCash(((CashCollection) amount).getCashMap());
		}
		// else, taking in a cheque. nothing to do.
	}
}
