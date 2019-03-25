package controller;

import model.persons.User;
import model.persons.Loginable;

import java.util.ArrayList;

public class SocialCreditScoreCalculator {

	private User user;
	private ArrayList user_accounts;

	public SocialCreditScoreCalculator(User user){
		this.user = user;
		this.user_accounts = user.getAccounts();

	}


	/**
	 * Calulate User's net asset based on its Accounts.
	 * @return score from calculating User's asset
	 */
	private int calulateAssetScore(){
		get
	}




	public int calculate(){

	}
}
