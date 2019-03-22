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

	public double getValue() {
		return ((double) valInCents)/100;
	}

	public Money add(Money that) {
		return new Money(getValue() + that.getValue());
	}

	public Money subtract(Money that) {
		return new Money(getValue() - that.getValue());
	}

	public int compareTo(Money that) {
		return Double.compare(getValue(), that.getValue());
	}

	public Money getOpposite() {
		return new Money(- this.getValue());
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
}
