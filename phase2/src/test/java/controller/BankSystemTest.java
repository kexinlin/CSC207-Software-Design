package controller;

import model.accounts.Account;
import model.exceptions.AccountNotExistException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Calendar;

import static org.junit.Assert.*;

public class BankSystemTest {
	private BankSystem bankSystem;
	private String recordFileName = "records.test.txt";

	@Before
	public void setUp() {
		File file = new File(recordFileName);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(
				"manager,mgr1,lolol\n" +
					"user,Foo Bar,u1,xxx\n" +
					"account,chq,45.0,1552098056134,127,u1\n" +
					"account,sav,100.0,1552172989694,874637,u1\n" +
					"set,primary-acc,u1,127");
			writer.close();
			bankSystem = new BankSystem(recordFileName);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Test
	public void testGainInterest() {
		Calendar.Builder builder = new Calendar.Builder();
		// the end of February
		builder.setDate(2019, Calendar.FEBRUARY,28);
		Calendar cal = builder.build();
		System.out.println(cal.getTime());
		bankSystem.setCurrentTime(cal.getTime());
		bankSystem.close();
		try {
			Account acc = bankSystem.getAccountById("874637");
			assertNotNull(acc);
			// it should gain 0.1% interest.
			assertEquals(100.1, acc.getBalance(), 0.0001);
		} catch (AccountNotExistException e) {
			fail("Cannot find the account.");
		}
	}
}
