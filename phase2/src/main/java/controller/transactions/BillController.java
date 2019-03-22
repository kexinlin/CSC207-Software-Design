package controller.transactions;

import model.transactors.Account;
import model.exceptions.InvalidOperationException;

public interface BillController {
	/**
	 * Records the payment.
	 * @param payeeName the name of the payee
	 * @param amount the amount of money to pay
	 * @throws InvalidOperationException
	 */
	void recordPayment(String payeeName, double amount)
		throws InvalidOperationException;
}
