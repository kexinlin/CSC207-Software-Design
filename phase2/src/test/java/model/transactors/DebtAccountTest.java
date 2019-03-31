package model.transactors;

import model.Money;
import model.exceptions.NoEnoughMoneyException;
import model.persons.AccountOwner;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DebtAccountTest {
	private DebtAccount acc;
	private AccountOwner owner;
	@Before
	public void setUp() {
		owner = mock(AccountOwner.class);
		acc = new CreditCardAccount(new Money(0), new Date(), "", owner);
	}
	@Test(expected = NoEnoughMoneyException.class)
	public void testLimit() throws NoEnoughMoneyException {
		acc.setDebtLimit(new Money(50));
		acc.takeMoneyOut(new Money(60));
		// should get an exception here, since we have exceeded the limit
	}

	@Test
	public void testLimit2() {
		acc.setDebtLimit(new Money(500));
		try {
			acc.takeMoneyOut(new Money(500));
		} catch (NoEnoughMoneyException e) {
			fail("Should not get an exception since we do not reach the limit.");
		}
	}
}
