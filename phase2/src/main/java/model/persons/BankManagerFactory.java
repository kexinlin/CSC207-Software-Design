package model.persons;

class BankManagerFactory extends GenericLoginableFactory<BankManager> {
	@Override
	BankManager fromRecord(String data) {
		String[] entries = data.split(",", 2);
		if (entries.length != 2) {
			// wrong format
			return null;
		}
		String username = entries[0];
		String password = entries[1];
		return new BankManager(username, password);
	}

	@Override
	String toRecord(Loginable loginable) {
		if (! (loginable instanceof BankManager)) {
			return null;
		}
		BankManager manager = (BankManager) loginable;
		return String.join(",",
			"manager",
			manager.getUsername(),
			manager.getPassword());
	}
}
