package fclib;
import fclib.*;
import java.util.*;

// driver program for randomly generated documents

public class MainTest2 {
	private static Rand rn = new Rand();

	// ValidWin object that lets all windows through

	private static final ValidWin VW =
	new ValidWin() {
		public boolean isValidWin(ArrayList<String> list,
		int startln, int numln) {
			return true;
		}
	};

	// run one iteration of simulation

	private static void doit() {
		// create a FindCopy object and add some documents

		FindCopy fc = new FindCopy(new int[]{rn.getRange(1, 20)},
			VW, Config.VS_WLL);

		int nd = rn.getRange(0, 25);

		for (int i = 1; i <= nd; i++)
			fc.addDocument(new DocAttrShort("doc" + i));

		// execute various methods on FindCopy object

		fc.printTable();
		fc.verifyTable();
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 1000; i++)
			doit();
	}
}
