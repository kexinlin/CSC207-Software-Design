package model.persons;

import java.util.HashMap;

public class LoginableFactory {
	private HashMap<String, GenericLoginableFactory<? extends Loginable>> classToFactory;
	public LoginableFactory() {
		classToFactory = new HashMap<>();
		classToFactory.put("manager", new BankManagerFactory());
		classToFactory.put("user", new UserFactory());
		classToFactory.put("user-employee", new UserEmployeeFactory());
	}

	public Loginable fromRecord(String type, String data) {
		return classToFactory.get(type).fromRecord(data);
	}

	public String toRecord(Loginable loginable) {
		return classToFactory.get(loginable.getType())
			.toRecord(loginable);
	}
}
