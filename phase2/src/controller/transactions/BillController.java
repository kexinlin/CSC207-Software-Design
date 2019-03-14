package controller.transactions;

import model.accounts.Account;
import model.exceptions.InvalidOperationException;

public interface BillController {
	/**
	 * Records the payment.
	 * @param account the account to pay from
	 * @param payeeName the name of the payee
	 * @param amount the amount of money to pay
	 * @throws InvalidOperationException
	 */
	void recordPayment(Account account, String payeeName, double amount)
		throws InvalidOperationException;
}
