package model;

public enum ManagerAction {
	PROCESS_REQUESTS(0), ADD_USER (10),
	// placeholder for the privilege of BankManagers
	_MANAGER_PRIVILEGE(100),
	UNDO_TX(110), STOCK_CASH(120), SHOW_CASH(130);

	private int id;
	ManagerAction(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
