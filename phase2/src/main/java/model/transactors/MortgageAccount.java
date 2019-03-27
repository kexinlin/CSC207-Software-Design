package model.transactors;

import model.Money;
import model.persons.AccountOwner;

import java.util.Date;

public class MortgageAccount extends DebtAccount {

		private double interestRate;
		private double monthlyPayment;
		private double monthlyRate;
		private int period; //Mortgage period in years
		/**
		 * Create an instance of MortgageAccount
		 *
		 * @param balance        the balance of the account
		 * @param dateOfCreation the currentTime of creation
		 * @param accountId      account id
		 * @param owner          owner of the account
		 */
		public MortgageAccount(Money balance, Date dateOfCreation, String accountId, AccountOwner owner, double interestRate
		, int period) {
			super(balance, dateOfCreation, accountId, owner);
			this.interestRate = interestRate;
			this.monthlyRate = interestRate/12;
			this.monthlyPayment = balance.getMoneyValue() * ((monthlyRate*Math.pow((1+monthlyRate),240))/(Math.pow((1+monthlyRate),240)-1));
		}

		/**
		 * @return the type of the account
		 */
		@Override
		public String getAccountType(){
			return "MortgageAccount";
		}


	/**
	 * return monthly payment amount for mortgage account
	 * @return
	 */
	public double getMonthlyPayment(){
			return this.monthlyPayment;
	}

}
