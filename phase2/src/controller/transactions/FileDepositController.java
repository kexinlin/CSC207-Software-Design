package controller.transactions;

import controller.ATM;
import controller.CashFactory;
import model.Cash;
import model.accounts.Account;
import model.exceptions.InvalidOperationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FileDepositController implements DepositController {
	private ATM machine;
	private String depositFileName;
	private CashFactory cashFactory;

	public FileDepositController(ATM atm) {
		this.machine = atm;
		this.cashFactory = new CashFactory();
		depositFileName = "deposits.txt";
	}

	/**
	 * Gets the ATM for this controller.
	 * @return the ATM for this controller.
	 */
	@Override
	public ATM getATM() {
		return machine;
	}

	/**
	 * Sets the ATM to `atm`.
	 * @param atm the ATM to set to.
	 */
	@Override
	public void setATM(ATM atm) {
		this.machine = atm;
	}

	/**
	 * Sets the transactions file name.
	 * @param depositFileName the file name to set to.
	 */
	public void setDepositFileName(String depositFileName) {
		this.depositFileName = depositFileName;
	}

	/**
	 * Gets the transactions file name.
	 * @return the transactions file name.
	 */
	public String getDepositFileName() {
		return depositFileName;
	}

	/**
	 * Deposit money into an account by entering a cheque or cash into the machine
	 * (an input file called deposits.txt will be read)
	 * <p>
	 * When this method is called, only the first line of the file will be read
	 * that line of the input file should be formatted as either:
	 * -------------------------------------------------------
	 * cheque,<<amount of cheque (double)>>
	 * -------------------------------------------------------
	 * or
	 * -----------------------------------------------------------------------------------------
	 * cash,<<denomination>> <<num of bills>>,<<denomination>> <<num of bills>>
	 * -----------------------------------------------------------------------------------------
	 * <p>
	 * example for cheque transactions:
	 * ----------------------------------
	 * cheque,1231.56
	 * ----------------------------------
	 * <p>
	 * example for cash transactions:
	 * ------------------------------------------
	 * cash,5 10,20 3,50 6
	 * ------------------------------------------
	 *
	 * @param acc The account to transactions into.
	 * @throws InvalidOperationException when the format of data is incorrect
	 */
	@Override
	public void depositMoney(Account acc)
		throws InvalidOperationException {
		DepositInfo depositInfo = getDepositInfo();
		if (depositInfo.type == DepositType.CHEQUE) {
			machine.getChequeController().depositCheque(acc, depositInfo.amount);
		} else if (depositInfo.type == DepositType.CASH) {
			machine.getCashController().depositCash(acc, depositInfo.cashMap);
		}
	}

	/**
	 * Stock Cash based on getDepositInfo.
	 * @throws InvalidOperationException when non-cash is stocked
	 */
	@Override
	public void stockCash() throws InvalidOperationException {
		DepositInfo depositInfo = getDepositInfo();
		if (depositInfo.type == DepositType.CASH) {
			machine.stockCash(depositInfo.cashMap);
		} else {
			throw new InvalidOperationException("Cannot take non-cash fund into " +
				"the machine.");
		}
	}

	/**
	 * Gets a hash map for how many bills of each type are being deposited
	 * @param data a String read from the transactions file.
	 * @return a hash map with Key=type-of-cash and Value=number-of-that-type
	 * @throws InvalidOperationException when the format of input file is incorrect
	 */
	private HashMap<Cash, Integer> getCashMapForDeposit(String data)
		throws InvalidOperationException {
		HashMap<Cash, Integer> bills = new HashMap<>();

		String[] vals = data.split(",");

		for (String s : vals)  {
			String[] kv = s.split("\\s+");
			try {
				bills.put(cashFactory.getCash(kv[0]), Integer.valueOf(kv[1]));
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
	 * Gets a DepositInfo from the file.
	 * @return Deposit info
	 * @throws InvalidOperationException when transaction file is not in correct format
	 */
	private DepositInfo getDepositInfo() throws InvalidOperationException {
		try {
			File file = new File(depositFileName);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String st;
			String[] data;
			st = br.readLine(); // only read the first line of file

			data = st.split(",", 2);
			if (data.length < 2) {
				throw new InvalidOperationException("Sorry, your transactions file is not in " +
					"correct format.");
			} else if (data[0].equals("cash")) {
				return new DepositInfo(getCashMapForDeposit(data[1]));
			} else if (data[0].equals("cheque")) {
				double amount;
				try {
					amount = Double.valueOf(data[1]);
				} catch (NumberFormatException e) {
					throw new InvalidOperationException("Sorry, " +
						"your transactions file is not in correct format.");
				}
				return new DepositInfo(amount);
			} else {
				throw new InvalidOperationException("Sorry, your transactions file is not in " +
					"correct format.");
			}
		} catch (IOException e) {
			throw new InvalidOperationException("Cannot read transactions file.");
		}
	}

	/**
	 * Enum that stores the types of possible depisit.
	 */
	private enum DepositType { CASH, CHEQUE }

	/**
	 * A private class that stores the information of deposit.
	 */
	private class DepositInfo {
		DepositType type;
		HashMap<Cash, Integer> cashMap;
		double amount;
		DepositInfo (HashMap<Cash, Integer> cashMap) {
			this.type = DepositType.CASH;
			this.cashMap = cashMap;
		}
		DepositInfo (double amount) {
			this.type = DepositType.CHEQUE;
			this.amount = amount;
		}
	}
}

