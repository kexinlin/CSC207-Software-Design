package model.persons;

import model.ManagerAction;

public class UserEmployee extends AccountOwner implements Employee {
	public UserEmployee(String name, String username, String password) {
		super(name, username, password);
	}

	@Override
	public boolean can(ManagerAction action) {
		return action.getId() > ManagerAction._MANAGER_PRIVILEGE.getId();
	}
}
