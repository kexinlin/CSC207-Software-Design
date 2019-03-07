package controller;

import model.Cash;
import model.exceptions.InvalidOperationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Controls operations related to cash.
 */
public class CashController {
	private ATM machine;

	/**
	 * Constructs a cash controller.
	 * @param atm the ATM machine.
	 */
	public CashController(ATM atm) {
		this.machine = atm;
	}

	/**
	 * Gets a hash map for how many bills of each type are being deposited
	 * @param data a String read from the deposit file.
	 * @return a hash map with Key=type-of-cash and Value=number-of-that-type
	 * @throws InvalidOperationException
	 */
	public HashMap<Cash, Integer> getCashMapForDeposit(String data)
		throws InvalidOperationException {
		HashMap<Cash, Integer> bills = new HashMap<>();

		String vals[] = data.split(",");

		for (String s : vals)  {
			String kv[] = s.split("\\s+");
			try {
				bills.put(getCash(kv[0]), Integer.valueOf(kv[1]));
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new InvalidOperationException(
					"Every `Cash Amount` field should have only two sub-fields.");
			} catch (NumberFormatException e) {
				throw new InvalidOperationException("Cannot read the number of cash "
					+ kv[0] + ".");
			}
		}

		return bills;
	}

	/**
	 * Gets the cash for `amount`
	 * @param amount the value of the cash, in String.
	 * @return the cash for `amount`
	 * @throws InvalidOperationException
	 */
	private Cash getCash(String amount) throws InvalidOperationException {
		Cash bill;
		switch (amount) {
			case "5":
				bill = Cash.FIVE;
				break;

			case "10":
				bill = Cash.TEN;
				break;

			case "20":
				bill = Cash.TWENTY;
				break;

			case "50":
				bill = Cash.FIFTY;
				break;

			case "100":
				bill = Cash.HUNDRED;
				break;

			default:
				throw new InvalidOperationException(
					"Sorry, the bill denomination"
					+ " in your input file does not exist.");
		}
		return bill;
	}
}
