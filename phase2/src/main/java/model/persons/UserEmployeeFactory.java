package model.persons;

class UserEmployeeFactory extends GenericLoginableFactory<UserEmployee> {

	@Override
	UserEmployee fromRecord(String data) {
		String[] entries = data.split(",", 3);
		if (entries.length != 3) {
			// wrong format
			return null;
		}
		String name = entries[0];
		String username = entries[1];
		String password = entries[2];

		return new UserEmployee(name, username, password);
	}

	@Override
	String toRecord(Loginable loginable) {
		if (! (loginable instanceof UserEmployee)) {
			return null;
		}
		UserEmployee user = (UserEmployee) loginable;
		return String.join(",",
			"user-employee",
			user.getName(),
			user.getUsername(),
			user.getPassword());
	}
}
