package model;

/**
 * some amount of money.
 */
public class Money implements Comparable<Money> {
	private int valInCents;

	public Money() {
		valInCents = 0;
	}

	public Money(double value) {
		valInCents = (int) (100 * value);
	}

	public double getMoneyValue() {
		return ((double) valInCents)/100;
	}

	public Money add(Money that) {
		System.out.println(getMoneyValue() + that.getMoneyValue());
		return new Money(getMoneyValue() + that.getMoneyValue());
	}

	public Money subtract(Money that) {
		return new Money(getMoneyValue() - that.getMoneyValue());
	}

	public int compareTo(Money that) {
		return Double.compare(getMoneyValue(), that.getMoneyValue());
	}

	public Money getOpposite() {
		return new Money(- this.getMoneyValue());
	}

	@Override
	public String toString() {
		return String.valueOf(getMoneyValue());
	}
}
