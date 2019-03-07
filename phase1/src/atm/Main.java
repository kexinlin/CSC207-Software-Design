package atm;

/**
 * The entrance to the whole program.
 */
public class Main {
	public static void main(String[] args) {
		String recordFileName = "data/records";
		ATM m = new ATM(recordFileName);
		UI ui = new CommandLineUI(m,
			System.in,
			System.out,
			System.err,
			false);
		ui.mainLoop();
		m.close();
	}
}
