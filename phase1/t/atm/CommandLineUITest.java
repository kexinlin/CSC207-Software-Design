package atm;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandLineUITest {
	ATM machine;
	InputStream input;
	PrintStream output;
	PrintStream error;
	CommandLineUI ui;
	ByteArrayOutputStream errStream;
	@Before
	public void setUp() {
		machine = mock(ATM.class);
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
	public void testTransferMoney() throws AccountNotExistException, NoEnoughMoneyException {
		User u = mock(User.class);
		when(machine.currentLoggedIn()).thenReturn(u);

		Account source = mock(Account.class);
		when(source.getAccountId()).thenReturn("0a");
		when(source.getOwner()).thenReturn(u);
		Account dest = mock(Account.class);
		when(dest.getAccountId()).thenReturn("8b");
		when(dest.getOwner()).thenReturn(u);

		when(machine.getAccountById("0a")).thenReturn(source);
		when(machine.getAccountById("8b")).thenReturn(dest);
		when(machine.transferMoney(any(Account.class), any(Account.class), anyDouble())).then(
			(x) -> {
				TransferTransaction tx = x.getArgumentAt(0, TransferTransaction.class);
				Account s = tx.getFromAcc();
				Account d = tx.getToAcc();
				if (s.getAccountId().equals("0a") && d.getAccountId().equals("8b")) {
					return true;
				} else {
					throw new NoEnoughMoneyException("");
				}
			});

		input = new ByteArrayInputStream("mv 0a 8b 20\nexit\n".getBytes());

		createUI();

		ui.mainLoop();
		error.flush();
		// we should see no error here
		assertEquals("", errStream.toString());
	}

}
