package controller.transactions;

import controller.ATM;
import model.Cash;
import model.accounts.Account;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.transactions.DepositTransaction;
import model.transactions.Transaction;
import model.transactions.WithdrawTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls operations related to cash.
 */
public class CashController {
	private ATM machine;
	private ArrayList<HashMap<Cash, Integer>>

	/**
	 * Constructs a cash controller.
	 * @param atm the ATM.
	 */
	public CashController(ATM atm) {
		this.machine = atm;
	}

	private HashMap<Cash, Integer> getCashToWithdraw(double amount) {
		HashMap<Cash, Integer> cashToWithdraw = new HashMap<>();
		HashMap<Cash, Integer> cashPool = machine.getBillAmount();

		Cash workingCashType = Cash.HUNDRED;

		boolean running = true;
		while (running) {
			if (workingCashType.getNumVal() <= )
		}
	}

	/**
	 * @param acc            account of the user for withdrawal
	 * @param amountWithdraw a HashMap that record the number of bills for each denomination that
	 *                       the user wants to withdraw
	 * @throws InsufficientCashException when the number of bills of certain denomination is less
	 *                                   than the amount that the user wants to withdraw
	 * @throws NoEnoughMoneyException    when amount withdrawn from the account exceeds what is allowed
	 */
	public void withdrawCash(Account acc, HashMap<Cash, Integer> amountWithdraw)
		throws InsufficientCashException, NoEnoughMoneyException, InvalidOperationException {

		machine.deductCash(amountWithdraw);
		// if it fails, the process ends with throwing an exception. Nothing done.

		// calculate the total amount withdrawn
		int totalAmount = calculateTotalBillAmount(amountWithdraw);

		acc.takeMoneyOut(totalAmount); // money in account may not be enough

		Transaction newTrans = new WithdrawTransaction(
			totalAmount, machine.getBankSystem().getCurrentTime(), acc);
		acc.addTrans(newTrans);
		acc.getOwner().addTransaction(newTrans);

	}

	/**
	 * Calculate and return the total sum of money that the user wants to withdraw.
	 *
	 * @param amountOfBill a HashMap that record the number of bills for each denomination that
	 *                     the user wants to withdraw
	 * @return the total sum of money that the user wants to withdraw
	 */
	private int calculateTotalBillAmount(HashMap<Cash, Integer> amountOfBill) {
		int sum = 0;
		for (Map.Entry<Cash, Integer> entry : amountOfBill.entrySet()) {
			Cash cash = entry.getKey();
			Integer num = entry.getValue();
			sum += cash.getNumVal() * num;
		}
		return sum;
	}

	/**
	 * Given a String array storing information of accountId and number of bills, stock
	 * corresponding cash into BankSystem and increase balance of account
	 *
	 * @param acc the account to transactions into.
	 * @param numOfBill a HashMap storing information of accountId and number of bills
	 */
	public void depositCash(Account acc, HashMap<Cash, Integer> numOfBill) {
		machine.stockCash(numOfBill);

		int amount = calculateTotalBillAmount(numOfBill);
		acc.putMoneyIn(amount);

		Transaction newTrans = new DepositTransaction(amount,
			machine.getBankSystem().getCurrentTime(), acc);
		acc.addTrans(newTrans);
		acc.getOwner().addTransaction(newTrans);

	}
}
