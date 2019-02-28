package atm;

import jdk.internal.util.xml.impl.Input;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

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
		ui = new CommandLineUI(machine, input,
			output, error, false);
	}

	@Test(timeout=1000)
	public void testExit() {
		input = new ByteArrayInputStream("exit\n".getBytes());
		// the ui should terminate within a short period of time
		ui.mainLoop();
	}

	@Test(timeout=1000)
	public void testLogin() {
		when(machine.login("foo", "bar")).thenReturn(true);
		ByteArrayOutputStream errStream = new ByteArrayOutputStream();
		error = new PrintStream(errStream);
		input = new ByteArrayInputStream("login\nfoo\nbar\nexit\n".getBytes());

		ui.mainLoop();
		// we should see no error here
		assertEquals("", errStream.toString());
	}


}
