package model;

import java.util.HashMap;

public class CashCollection extends Money {
	private HashMap<Cash, Integer> cashMap;

	public CashCollection(HashMap<Cash, Integer> cashMap) {
		this.cashMap = cashMap;
	}

	@Override
	public double getMoneyValue() {
		return cashMap.entrySet().stream()
			.mapToDouble(entry -> entry.getKey().getNumVal() * entry.getValue())
			.sum();
	}

	public CashCollection add(CashCollection that) {
		HashMap<Cash, Integer> map = new HashMap<>();
		cashMap.forEach((key, value) -> map.put(key, value
			+ that.cashMap.get(key)));

		return new CashCollection(map);
	}

	public HashMap<Cash, Integer> getCashMap() {
		return cashMap;
	}

	@Override
	public CashCollection getOpposite() {
		HashMap<Cash, Integer> map = new HashMap<>();
		cashMap.forEach((k, v) -> map.put(k, -v));
		return new CashCollection(map);
	}
}
