package model.transactors;

import model.Money;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;

import java.util.Observable;

/**
 * A transactor. Maybe an account, an envelope containing cash or cheque, or
 * an external payee. Instances of this interface should be able to receive
 * and/or give out fund.
 */
public abstract class Transactor extends Observable {

	/**
	 * Gets the id of this transactor
	 *
	 * @return account id
	 */
	public abstract String getId();

	/**
	 * Take `amount` of money out of the account.
	 * Note that transferring money out is not allowed for certain type of class,
	 * and in this case, exception should be raised.
	 * Exception will also be raised when the amount exceeds what is allowed.
	 *
	 * @param amount the amount of money to take out.
	 *
	 */
	public abstract void takeMoneyOut(Money amount)
		throws NoEnoughMoneyException, InvalidOperationException;


	/**
	 * Put `amount` of money into the account.
	 *
	 * @param amount the amount of money to put in
	 */
	public abstract void putMoneyIn(Money amount) throws InvalidOperationException;
}
