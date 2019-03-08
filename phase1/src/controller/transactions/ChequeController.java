package controller.transactions;

import controller.ATM;
import model.accounts.Account;
import model.transactions.DepositTransaction;
import model.transactions.Transaction;

public class ChequeController {
	private ATM machine;

	public ChequeController(ATM atm) {
		this.machine = atm;
	}
	/**
	 * Given a String array storing information of accountId and amount of cheque, increase
	 * balance of account
	 *
	 * @param acc The account to transactions into.
	 * @param amount amount of cheque.
	 */
	public void depositCheque(Account acc, double amount) {
		acc.putMoneyIn(amount);

		Transaction newTrans = new DepositTransaction(amount,
			machine.getBankSystem().getCurrentTime(), acc);
		acc.addTrans(newTrans);
		acc.getOwner().addTransaction(newTrans);
	}
}
