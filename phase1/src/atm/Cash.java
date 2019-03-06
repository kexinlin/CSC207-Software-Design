package atm;


public enum Cash {

	FIVE(5),TEN(10),TWENTY(20),FIFTY(30),HUNDRED(100);

	private int numVal;

	private Cash(int num) {
		this.numVal = num;
	}

	public int getNumVal(){
		return numVal;
	}
}

