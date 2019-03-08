package view;

import controller.ATM;
import controller.BankSystem;
import model.accounts.Account;
import model.persons.User;
import model.exceptions.AccountNotExistException;
import model.exceptions.InvalidOperationException;
import model.exceptions.NoEnoughMoneyException;
import org.junit.Before;
import org.junit.Test;
import view.cmdline.CommandLineUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandLineUITest {
	BankSystem sys;
	ATM machine;
	InputStream input;
	PrintStream output;
	PrintStream error;
	CommandLineUI ui;
	ByteArrayOutputStream errStream;
	@Before
	public void setUp() {
		machine = mock(ATM.class);
		sys = mock(BankSystem.class);
		input = mock(InputStream.class);
		output = mock(PrintStream.class);
		errStream = new ByteArrayOutputStream();
		error = new PrintStream(errStream);
	}

	private void createUI() {
		ui = new CommandLineUI(machine, input,
			output, error, false);
	}

	@Test(timeout=1000)
	public void testExit() {
		input = new ByteArrayInputStream("exit\n".getBytes());
		createUI();
		// the ui should terminate within a short period of time
		ui.mainLoop();
		error.flush();
		// we should get no errors
		assertEquals("", errStream.toString());
	}

	@Test(timeout=1000)
	public void testLogin() {
		when(machine.login("foo", "bar")).thenReturn(true);

		input = new ByteArrayInputStream("login\nfoo\nbar\nexit\n".getBytes());

		createUI();

		ui.mainLoop();
		error.flush();
		// we should see no error here
		assertEquals("", errStream.toString());
	}

	@Test(timeout = 1000)
	public void testLoginFailure() {
		when(machine.login("foo", "bar")).thenReturn(true);

		input = new ByteArrayInputStream("login\nfoo\nfuzz\nexit\n".getBytes());

		createUI();

		ui.mainLoop();
		error.flush();
		// we should see error here
		assertNotEquals("", errStream.toString());
	}

	@Test()
	public void testTransferMoney() throws AccountNotExistException, NoEnoughMoneyException
		, InvalidOperationException
	{
		User u = mock(User.class);
		when(machine.currentLoggedIn()).thenReturn(u);

		Account source = mock(Account.class);
		when(source.getAccountId()).thenReturn("0a");
		when(source.getOwner()).thenReturn(u);
		Account dest = mock(Account.class);
		when(dest.getAccountId()).thenReturn("8b");
		when(dest.getOwner()).thenReturn(u);

		when(sys.getAccountById("0a")).thenReturn(source);
		when(sys.getAccountById("8b")).thenReturn(dest);

		doAnswer(
			x -> {
				Account s = x.getArgumentAt(0, Account.class);
				Account d = x.getArgumentAt(1, Account.class);
				if (s.getAccountId().equals("0a") && d.getAccountId().equals("8b")) {

				} else {
					throw new NoEnoughMoneyException("");
				}
				return null;
			})
			.when(sys).transferMoney(any(Account.class), any(Account.class), anyDouble());

		input = new ByteArrayInputStream("mv 0a 8b 20\nexit\n".getBytes());

		createUI();

		ui.mainLoop();
		error.flush();
		// we should see no error here
		assertEquals("", errStream.toString());
	}

}
