package controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class BankSystemTest {
	private BankSystem bankSystem;
	private String recordFileName = "test-rec.txt";

	@Before
	public void setUp() {
		File file = new File(recordFileName);

		bankSystem = new BankSystem(recordFileName);
	}

	@After
	public void cleanUp() {
		bankSystem.close();
	}

	@Test
	public void createUser() {

	}

	@Test
	public void testTransferMoney() {
	}

	@Test
	public void addAccount() {
	}

	@Test
	public void testPayBill() {
	}
}
