package controller;

import model.Money;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import model.transactions.Transaction;
import model.transactors.Account;
import model.exceptions.AccountNotExistException;
import model.transactors.DebtAccount;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class BankSystemTest {
	private BankSystem bankSystem;
	private String recordFileName = "records.test.txt";

	private Date beforeStatementDay() {
		Calendar.Builder builder = new Calendar.Builder();
		// the end of February
		builder.setDate(2019, Calendar.FEBRUARY,28);
		return builder.build().getTime();
	}

	private Date beforePayDay() {
		Calendar.Builder builder = new Calendar.Builder();
		// Feb 14. next day, Feb 15, is pay day.
		builder.setDate(2019, Calendar.FEBRUARY, 14);
		return builder.build().getTime();
	}

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
					"account,cre,0.0,1552172989694,999,u1\n" +
					"account,loc,0.0,1552172989694,888,u1\n" +
					"set,primary-acc,u1,127");
			writer.close();
			bankSystem = new BankSystem(recordFileName);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Test
	public void testGainInterest() {
		Date date = beforeStatementDay();
		System.out.println(date);
		bankSystem.setCurrentTime(date);
		bankSystem.close();
		try {
			Account acc = bankSystem.getAccountById("874637");
			assertNotNull(acc);
			// it should gain 0.1% interest.
			assertEquals(100.1, acc.getBalance().getMoneyValue(), 0.0001);
		} catch (AccountNotExistException e) {
			fail("Cannot find the account.");
		}
	}

	@Test
	public void testDebtAccGenStatement()
		throws AccountNotExistException, NoEnoughMoneyException {
		bankSystem.setCurrentTime(beforeStatementDay());
		Account acc = bankSystem.getAccountById("999");
		assertTrue(acc instanceof DebtAccount);
		DebtAccount debtAcc = (DebtAccount) acc;
		debtAcc.setDebtLimit(new Money(200));
		debtAcc.takeMoneyOut(new Money(20));
		bankSystem.close(); // will have a statement
		assertEquals(20, debtAcc.getStatementBalance().getMoneyValue(), 0.001);
		debtAcc.takeMoneyOut(new Money(50));
		bankSystem.close(); // will have another statement
		assertEquals(70, debtAcc.getStatementBalance().getMoneyValue(), 0.001);
	}

	@Test
	public void testDebtAccGainInterest()
		throws AccountNotExistException, NoEnoughMoneyException {
		bankSystem.setCurrentTime(beforeStatementDay());

		Account acc = bankSystem.getAccountById("999");
		assertTrue(acc instanceof DebtAccount);
		DebtAccount debtAcc = (DebtAccount) acc;
		debtAcc.setDebtLimit(new Money(200));
		debtAcc.takeMoneyOut(new Money(50));
		debtAcc.setInterestRate(0.1);
		bankSystem.close(); // will generate a statement
		assertEquals(50, debtAcc.getStatementBalance().getMoneyValue(), 0.001);

		// make next day the pay day
		bankSystem.setCurrentTime(beforePayDay());
		debtAcc.takeMoneyOut(new Money(70));
		debtAcc.putMoneyIn(new Money(10));

		// should gain an interest of 10%
		// the stmt balance now has become (50-10) = 40
		bankSystem.close();

		assertEquals(40 * (1 + 0.1),
			debtAcc.getStatementBalance().getMoneyValue(), 0.001);

	}

	@Test
	public void testFees()
		throws AccountNotExistException, NoEnoughMoneyException, InvalidOperationException {
		Account locAcc = bankSystem.getAccountById("888");
		Money locOrigBalance = locAcc.getBalance();
		Account chqAcc = bankSystem.getAccountById("127");
		Money chqOrigBalance = chqAcc.getBalance();

		Money expectedFee = new Money(5);
		Money amountTransferred = new Money(10);
		Transaction tx = bankSystem.makeTx(10, locAcc, chqAcc);
		// we have five-dollar fee for cash advance
		assertEquals(0, tx.getFee().compareTo(expectedFee));
		bankSystem.proceedTransaction(tx);

		// the fee should be deducted from LOC account
		assertEquals(0, locAcc.getBalance()
			.compareTo(locOrigBalance.add(amountTransferred).add(expectedFee)));

		// ... and does not go into CHQ account
		assertEquals(0, chqAcc.getBalance()
			.compareTo(chqOrigBalance.add(amountTransferred)));

		bankSystem.undoTransaction(tx);

		// after undo, the balance should be same as before...
		assertEquals(0, locAcc.getBalance().compareTo(locOrigBalance));
		assertEquals(0, chqAcc.getBalance().compareTo(chqOrigBalance));
	}
}
