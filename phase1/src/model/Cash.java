package model;


public enum Cash {

	FIVE(5),TEN(10),TWENTY(20),FIFTY(50);

	private int numVal;

	Cash(int num) {
		this.numVal = num;
	}

	public int getNumVal(){
		return numVal;
	}
}

