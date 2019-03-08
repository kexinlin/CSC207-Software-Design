package controller;

import controller.deposit.CashController;
import controller.deposit.ChequeController;
import controller.deposit.DepositController;
import controller.deposit.FileDepositController;
import model.Cash;
import model.accounts.Account;
import model.exceptions.InsufficientCashException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.persons.Loginable;
import model.transactions.DepositTransaction;
import model.transactions.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ATM {
	private BankSystem bankSystem;
	private Loginable loggedIn;
	private HashMap<Cash, Integer> billAmount;
	private CashController cashController;
	private ChequeController chequeController;
	private DepositController depositController;

	/**
	 * Constructs an ATM.
	 * @param bankSystem the bank system underneath.
	 */
	public ATM(BankSystem bankSystem) {
		this.bankSystem = bankSystem;

		this.billAmount = new HashMap<>();
		// init the num of each kind of cash to zero
		for (Cash x : Cash.values()) {
			billAmount.put(x, 0);
		}
		this.cashController = new CashController(this);
		this.chequeController = new ChequeController(this);
		this.depositController = new FileDepositController(this);
	}

	/**
	 * Gets the bank system for this atm.
	 * @return the bank system.
	 */
	public BankSystem getBankSystem() {
		return bankSystem;
	}

	/**
	 * Sets the bank system for this atm.
	 * @param bankSystem the bank system to set to.
	 */
	public void setBankSystem(BankSystem bankSystem) {
		this.bankSystem = bankSystem;
	}

	/**
	 * Sets the cash controller for this atm.
	 * @param cashController the cash controller to set.
	 */
	public void setCashController(CashController cashController) {
		this.cashController = cashController;
	}

	/**
	 * Gets the cash controller for this atm.
	 * @return the cash controller.
	 */
	public CashController getCashController() {
		return cashController;
	}

	/**
	 * Gets the deposit controller for this atm.
	 * @return the deposit controller.
	 */
	public DepositController getDepositController() {
		return depositController;
	}

	/**
	 * Sets the deposit controller for this atm.
	 * @param depositController the deposit controller.
	 */
	public void setDepositController(DepositController depositController) {
		this.depositController = depositController;
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
	}

	/**
	 * Put `Cash` into this BankSystem machine.
	 *
	 * @param inputCash a HashMap that map the denomination to its number of bills
	 */
	public void stockCash(HashMap<Cash, Integer> inputCash) {
		for (Map.Entry<Cash, Integer> pair : inputCash.entrySet()) {
			this.billAmount.put(pair.getKey(), pair.getValue());
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
	private void checkIfAbleToWithdraw(HashMap<Cash, Integer> amountWithdraw)
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
	 * Gets the cheque controller.
	 * @return cheque controller.
	 */
	public ChequeController getChequeController() {
		return this.chequeController;
	}
}
