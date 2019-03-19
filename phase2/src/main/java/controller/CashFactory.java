package controller;

import model.Cash;


public class CashFactory {

	/**
	 * Gets the cash for `amount`
	 *
	 * @param amount the value of the cash, in String.
	 * @return the cash for `amount`
	 */
	public Cash getCash(String amount) {
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

			default:
				throw new NumberFormatException(
					"Sorry, the bill denomination"
						+ " in your input file does not exist.");
		}
		return bill;
	}
}
