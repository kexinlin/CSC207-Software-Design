package model.persons;

import model.ManagerAction;

public interface Employee {
	boolean can(ManagerAction action);
}
