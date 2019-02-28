package atm;

/**
 * The entrance to the whole program.
 */
public class Main {
	public static void main(String[] args) {
		ATM m = new ATM();
		UI ui = new CommandLineUI(m,
			System.in,
			System.out,
			System.err,
			false);
		ui.mainLoop();

	}
}
