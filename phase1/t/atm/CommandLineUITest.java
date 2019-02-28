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
	@Before
	public void setUp() {
		machine = mock(ATM.class);
		input = mock(InputStream.class);
		output = mock(PrintStream.class);
		error = mock(PrintStream.class);
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
	}

	@Test(timeout=1000)
	public void testLogin() {
		when(machine.login("foo", "bar")).thenReturn(true);
		ByteArrayOutputStream errStream = new ByteArrayOutputStream();
		error = new PrintStream(errStream);
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
		ByteArrayOutputStream errStream = new ByteArrayOutputStream();
		error = new PrintStream(errStream);
		input = new ByteArrayInputStream("login\nfoo\nfuzz\nexit\n".getBytes());

		createUI();

		ui.mainLoop();
		error.flush();
		// we should see error here
		assertNotEquals("", errStream.toString());
	}


}
