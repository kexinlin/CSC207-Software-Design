package controller.transactions;

import controller.ATM;
import model.Cash;
import model.CashCollection;
import model.exceptions.InsufficientCashException;

import java.util.*;

/**
 * The controller related to the calculation of cash.
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
	public CashCollection getCashToWithdraw(double amount)
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
			return new CashCollection(cashToWithdraw);
		}
		// greedy fails

		throw new InsufficientCashException(
			"Cannot work out a method to withdraw the amount.");
	}

}
