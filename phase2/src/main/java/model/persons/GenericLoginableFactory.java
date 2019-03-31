package model.persons;

abstract class GenericLoginableFactory<T extends Loginable> {
	abstract T fromRecord(String data);
	abstract String toRecord(Loginable loginable);
}
