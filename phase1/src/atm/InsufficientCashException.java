package atm;

public class InsufficientCashException extends Exception {


	InsufficientCashException(String msg){
		super(msg);
	}
}
