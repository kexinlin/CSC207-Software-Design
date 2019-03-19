package model;

/**
 * Cash types.
 */
public enum Cash {

	FIVE(5),TEN(10),TWENTY(20),FIFTY(50);

	private int numVal;

	/**
	 * Construct a cash type of value `num`
	 * @param num the value of this cash type.
	 */
	Cash(int num) {
		this.numVal = num;
	}

	/**
	 * Gets the value of this cash type.
	 * @return the value.
	 */
	public int getNumVal(){
		return numVal;
	}
}

