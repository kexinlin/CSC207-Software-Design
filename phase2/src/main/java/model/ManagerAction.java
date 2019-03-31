package model;

public enum ManagerAction {
	// these action ids might change
	// one should not rely on the exact ids of individual values
	// instead, compare with the reserved placeholders to see whether
	// a certain identity is allowed to do certain actions
	PROCESS_REQUESTS(0), ADD_USER (10),
	// placeholder for the privilege of BankManagers
	_MANAGER_PRIVILEGE(100),
	LIST_USER(101), LIST_TX(102),
	UNDO_TX(110), STOCK_CASH(120), SHOW_CASH(130),
	CHANGE_USER_SETTINGS(140);

	private int id;
	ManagerAction(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
