package controller;

import model.Cash;
import model.exceptions.InsufficientCashException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class ATMTest {
	private ATM atm;
	private String alertFN = "alerts.test.txt";

	@Before
	public void setUp() {
		String atmRecFN = "atm-records.test.txt";
		try {
			File f = new File(atmRecFN);
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write("5,20\n10,20\n20,20\n50,20\n");
			w.close();

			f = new File(alertFN);
			w = new BufferedWriter(new FileWriter(f));
			w.flush();
			w.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		BankSystem bankSystem = mock(BankSystem.class);
		atm = new ATM(bankSystem, atmRecFN);
		atm.setAlertFileName(alertFN);
	}

	@Test
	public void testAlerts() {
		HashMap<Cash, Integer> cashMap = new HashMap<>();
		cashMap.put(Cash.TEN, 1);
		try {
			atm.deductCash(cashMap);
			atm.close();
		} catch (InsufficientCashException e) {
			fail("It should have sufficient cash, but does not. " + e);
		}

		try {
			File f = new File(alertFN);
			BufferedReader r = new BufferedReader(new FileReader(f));
			String s = r.readLine();
			System.out.println(s);
			assertTrue(s.matches(".*needed.*"));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}
}
