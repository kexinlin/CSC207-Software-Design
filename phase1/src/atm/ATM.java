package atm;

import java.util.Collection;

public class ATM {
	/**
	 * Constructs an instance of ATM.
	 */
	public ATM() {

	}

	public boolean login(String username, String password) {
		return true;
	}

	public boolean depositCash(Collection<? extends Cash> cash) {
		return true;
	}

	public boolean withdrawCash(Collection<? extends Cash> cash) {
		return true;
	}

}
