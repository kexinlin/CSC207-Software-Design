package model.persons;

public class UserFactory extends GenericLoginableFactory<User> {
	@Override
	User fromRecord(String data) {
		String[] entries = data.split(",", 3);
		if (entries.length != 3) {
			// wrong format
			return null;
		}

		String name = entries[0];
		String username = entries[1];
		String password = entries[2];

		return new User(name, username, password);
	}

	@Override
	String toRecord(Loginable loginable) {
		if (! (loginable instanceof User)) {
			return null;
		}
		User user = (User) loginable;
		return String.join(",",
			"user",
			user.getName(),
			user.getUsername(),
			user.getPassword());
	}

}
