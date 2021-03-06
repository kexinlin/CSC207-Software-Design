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

import java.util.*;

/**
 * Controls operations related to cash.
 */
public class CashController {
	private ATM machine;

	/**
	 * Constructs a cash controller.
	 * @param atm the ATM.
	 */
	public CashController(ATM atm) {
		this.machine = atm;
	}

	/**
	 * Get the amount of bills gives the User who withdraw money
	 * @param amount the amount of money that User wants to withdraw
	 * @return a HashMap that maps the Cash and the number of bills
	 * @throws InsufficientCashException when the cash stored in ATM cannot add up to the amount
	 */
	private HashMap<Cash, Integer> getCashToWithdraw(double amount)
		throws InsufficientCashException {
		// better to work with ints
		int amt = (int) amount;
		if (amt != amount) { // cannot withdraw < $1
			throw new InsufficientCashException("We do not have cash whose value is" +
				"below 1.");
		}
		HashMap<Cash, Integer> cashToWithdraw = new HashMap<>();
		HashMap<Cash, Integer> cashPool = machine.getBillAmount();

		ArrayList<Cash> cashTypes = new ArrayList<>(Arrays.asList(
			Cash.FIFTY,
			Cash.TWENTY,
			Cash.TEN,
			Cash.FIVE
		));

		// greedy method

		int sum = 0;

		for (Cash c : cashTypes) {
			int val = c.getNumVal();
			int num = (amt - sum)/val;
			int maximumAvailable = cashPool.get(c);
			if (num > maximumAvailable) {
				num = maximumAvailable;
			}
			sum += num * val;
			cashToWithdraw.put(c, num);
		}

		// if greedy succeeds
		if (sum == amt) {
			return cashToWithdraw;
		}
		// greedy fails

		throw new InsufficientCashException(
			"Cannot work out a method to withdraw the amount.");
/*		HashMap<Cash, Integer> maxAvailCash = new HashMap<>();

		// calculate at most how many we need & can use for each type
		for (Cash c : cashTypes) {
			maxAvailCash.put(c,
				Math.min(amt/c.getNumVal(), cashPool.get(c)));
		}*/


	}

	/**
	 * Take `amount` of money out of `acc` and put it into user's hand
	 * @param acc            account of the user for withdrawal
	 * @param amount the amount of money
	 * @throws InsufficientCashException when the number of bills of certain denomination is less
	 *                                   than the amount that the user wants to withdraw
	 * @throws NoEnoughMoneyException    when amount withdrawn from the account exceeds what is allowed
	 */
	public HashMap<Cash, Integer> withdrawCash(Account acc, double amount)
		throws InsufficientCashException, NoEnoughMoneyException, InvalidOperationException {

		HashMap<Cash, Integer> amountWithdraw = getCashToWithdraw(amount);
		machine.checkIfAbleToWithdraw(amountWithdraw);
		// if it fails, the process ends with throwing an exception. Nothing done.

		// calculate the total amount withdrawn
		int totalAmount = calculateTotalBillAmount(amountWithdraw);

		acc.takeMoneyOut(totalAmount); // money in account may not be enough
		machine.deductCash(amountWithdraw);

		Transaction newTrans = new WithdrawTransaction(
			totalAmount, machine.getBankSystem().getCurrentTime(), acc);
		machine.getBankSystem().addTransaction(newTrans);
		return amountWithdraw;
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
		machine.getBankSystem().addTransaction(newTrans);
	}
}
