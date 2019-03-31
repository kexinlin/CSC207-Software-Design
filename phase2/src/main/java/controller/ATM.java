package controller;

import controller.transactions.*;
import model.Cash;
import model.CashCollection;
import model.Money;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.Loginable;
import model.transactions.Transaction;
import model.transactors.Account;
import model.transactors.MoneyEnvelope;

import java.util.HashMap;

public class ATM {
	private BankSystem bankSystem;
	private Loginable loggedIn;
	private HashMap<Cash, Integer> billAmount;
	private CashController cashController;
	private DepositController depositController;
	private WithdrawController withdrawController;
	private ATMRecordController atmRecordController;
	private AlertController alertController;
	private String atmRecordFileName;
	private String alertFileName;
	private MoneyEnvelope envelope;

	/**
	 * Constructs an ATM.
	 *
	 * @param bankSystem the bank system underneath.
	 */
	public ATM(BankSystem bankSystem, String recordFileName) {
		this.bankSystem = bankSystem;

		this.atmRecordFileName = recordFileName;
		this.atmRecordController = new ATMRecordController(this);

		this.billAmount = new HashMap<>();
		// init the num of each kind of cash to zero
		for (Cash x : Cash.values()) {
			billAmount.put(x, 0);
		}
		this.cashController = new CashController(this);
		this.depositController = new FileDepositController(this);
		this.withdrawController = new FileWithdrawController(this);

		this.alertFileName = "alerts.txt";
		this.alertController = new AlertController(this, bankSystem);

		atmRecordController.readRecords();
		this.envelope = new MoneyEnvelope(this);
	}

	/**
	 * Get the name of the file that sends alert when the amount of any denomination
	 * goes below 20.
	 *
	 * @return
	 */
	public String getAlertFileName() {
		return alertFileName;
	}

	public void setAlertFileName(String alertFileName) {
		this.alertFileName = alertFileName;
	}

	/**
	 * Get the file name of record for this atm.
	 *
	 * @return record file name
	 */
	public String getAtmRecordFileName() {
		return atmRecordFileName;
	}

	/**
	 * Get the bank system for this atm.
	 *
	 * @return the bank system.
	 */
	public BankSystem getBankSystem() {
		return bankSystem;
	}

	/**
	 * Set the bank system for this atm.
	 *
	 * @param bankSystem the bank system to set to.
	 */
	public void setBankSystem(BankSystem bankSystem) {
		this.bankSystem = bankSystem;
	}

	/**
	 * Set the ATM record name for this atm.
	 *
	 * @param atmRecordFileName the name of the record file
	 */
	public void setAtmRecordFileName(String atmRecordFileName) {
		this.atmRecordFileName = atmRecordFileName;
	}

	/**
	 * Set the cash controller for this atm.
	 *
	 * @param cashController the cash controller to set.
	 */
	public void setCashController(CashController cashController) {
		this.cashController = cashController;
	}

	/**
	 * Get the cash controller for this atm.
	 *
	 * @return the cash controller.
	 */
	public CashController getCashController() {
		return cashController;
	}

	/**
	 * Get the transactions controller for this atm.
	 *
	 * @return the transactions controller.
	 */
	public DepositController getDepositController() {
		return depositController;
	}

	/**
	 * Set the transactions controller for this atm.
	 *
	 * @param depositController the transactions controller.
	 */
	public void setDepositController(DepositController depositController) {
		this.depositController = depositController;
	}

	/**
	 * Get the withdraw controller for this atm.
	 * @return the withdraw controller
	 */
	public WithdrawController getWithdrawController() {
		return withdrawController;
	}

	/**
	 * Set the withdraw controller for this atm.
	 * @param withdrawController the withdraw controller
	 */
	public void setWithdrawController(WithdrawController withdrawController) {
		this.withdrawController = withdrawController;
	}

	/**
	 * Get the bill amount in this ATM.
	 *
	 * @return the bill amount.
	 */
	public HashMap<Cash, Integer> getBillAmount() {
		return billAmount;
	}

	/**
	 * Log in a user or admin.
	 *
	 * @param username the username of that person
	 * @param password the password of that person
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean login(String username, String password) {
		Loginable person = bankSystem.getLoginable(username);
		if (person != null && person.verifyPassword(password)) {
			loggedIn = person;
			return true;
		}
		return false;
	}

	/**
	 * Log out the current individual.
	 */
	public void logout() {
		loggedIn = null;
	}


	/**
	 * Deduct the amount of cash that the user wants to withdraw from the BankSystem.
	 *
	 * @param amountWithdraw a HashMap that record the number of bills for each denomination that
	 *                       the user wants to withdraw
	 */
	public void deductCash(HashMap<Cash, Integer> amountWithdraw)
		throws InsufficientCashException {
		checkIfAbleToWithdraw(amountWithdraw);
		for (Cash cash : amountWithdraw.keySet()) {
			int oldNum = this.billAmount.get(cash);
			int newNum = oldNum - amountWithdraw.get(cash);
			this.billAmount.put(cash, newNum);
		}

		if (alertController.atmNeedReplenishment()) {
			alertController.sendAlert();
		}
	}


	/**
	 * Put `Cash` into this BankSystem machine.
	 *
	 * @param inputCash a HashMap that map the denomination to its number of bills
	 */
	public void stockCash(HashMap<Cash, Integer> inputCash) {
		for (Cash cash : inputCash.keySet()) {
			this.billAmount.put(cash, this.billAmount.get(cash) + inputCash.get(cash));
		}
	}


	/**
	 * Check whether BankSystem has sufficient amount of bills for the user to withdraw from.
	 *
	 * @param amountWithdraw a HashMap that record the number of bills
	 *                       for each denomination that
	 *                       the user wants to withdraw
	 * @throws InsufficientCashException when the number of bills of certain denomination
	 *                                   is less than the amount that the user wants
	 *                                   to withdraw
	 */
	public void checkIfAbleToWithdraw(HashMap<Cash, Integer> amountWithdraw)
		throws InsufficientCashException {
		for (Cash cash : amountWithdraw.keySet()) {
			if (this.billAmount.get(cash) < amountWithdraw.get(cash)) {
				throw new InsufficientCashException(
					"Sorry, this BankSystem does not have enough " +
						cash.getNumVal() + " dollar bills at this moment.");
			}
		}
	}

	/**
	 * Gets the individual who is currently logged in.
	 *
	 * @return the individual who is logged in, or null if none.
	 */
	public Loginable currentLoggedIn() {
		return loggedIn;
	}

	/**
	 * save records to file.
	 */
	public void close() {
		atmRecordController.writeRecords();
	}

	/**
	 * Performs a full process of withdrawing. Calculates the amount
	 * of cash needed. Takes the cash out of the machine. Reduces the amount
	 * of fund on the account. Puts the cash into the envelope. Records the
	 * transaction.
	 * @param acc the account to withdraw money from
	 * @param amount the amount of money to withdraw
	 * @throws InvalidOperationException if the account does not support withdrawing
	 * or error during giving out the cash.
	 * @throws InsufficientCashException if the atm does not have enough cash
	 * @throws NoEnoughMoneyException if the account does not have enough fund
	 */
	public Transaction withdrawMoney(Account acc, double amount)
		throws InvalidOperationException, InsufficientCashException, NoEnoughMoneyException {
		CashCollection col = getCashController().getCashToWithdraw(amount);
		Transaction tx = new Transaction(col, getBankSystem().getCurrentTime(),
			acc, envelope);
		getBankSystem().proceedTransaction(tx);
		return tx;
	}

	public MoneyEnvelope getEnvelope() {
		return envelope;
	}

	/**
	 * Performs a full process of deposit. Gets the money to deposit from the envelope.
	 * If the envelope contains cash, puts the cash into the machine. Adds fund to the
	 * account. Records the transaction.
	 * @param acc the account to deposit.
	 */
	public Transaction depositMoney(Account acc)
		throws InvalidOperationException {
		Money money = getDepositController().getDepositMoney();
		Transaction tx = new Transaction(money, getBankSystem().getCurrentTime(),
			envelope, acc);
		try {
			getBankSystem().proceedTransaction(tx);
		} catch (NoEnoughMoneyException e) {
			e.printStackTrace();
			throw new InvalidOperationException("This should not happen.");
		}
		return tx;
	}

	/**
	 * Deduct cash specified by cashMap from the machine, and put the cash into the
	 * envelope.
	 * @param cashMap the cash to withdraw, as a HashMap.
	 * @throws InvalidOperationException if error during putting the cash into the envelope.
	 * @throws InsufficientCashException if atm does not have enough cash.
	 */
	public void withdrawCash(HashMap<Cash, Integer> cashMap)
		throws InvalidOperationException, InsufficientCashException {
		deductCash(cashMap);
		getWithdrawController().withdrawMoney(cashMap);
	}
}
