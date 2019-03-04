package atm;

public class Cash {
	private cashNum num;

	public enum cashNum{
		FIVE, TEN, TWENTY, FIFTY, HUNDRED;
	}
	private Cash(cashNum num){
		this.num = num;
	}
}
