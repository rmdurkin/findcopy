import fclib.*;
import java.io.*;
import java.util.*;

// generate random documents in a directory

public class MainRandDocs {

	// document properties

	private static final int MAXDOCS = 100;

	private static final int MAXOWNERS = 20;

	private static final int MINLINES = 25;
	private static final int MAXLINES = 200;

	private static final int MINWIN = 10;
	private static final int MAXWIN = 25;

	private static final int MAXGLOBCOPY = 3;
	private static final int MAXOWNERCOPY = 25;

	private static final int MAXRAND = 20;

	private static final Rand rn = new Rand();

	// generate a document

	private static void gen(File dir, int docnum) throws IOException {
		assert dir != null && docnum >= 1;

		// determine document and owner names and numbers

		String docname = "doc" + docnum;
		int ownernum = rn.getRange(1, MAXOWNERS);
		String ownername = "owner" + ownernum;

		// open the output file

		File out = new File(dir, docname);
		
		FileWriter fw = new FileWriter(out.getPath());
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);

		// write out the document and owner names

		pw.printf("%s\n", docname);
		pw.printf("%s\n", ownername);

		// write the rest of the document

		gen(pw, docnum, ownernum);

		pw.close();
	}

	// generate the lines of a random document

	private static void gen(PrintWriter pw, int docnum, int ownernum) {
		assert pw != null && docnum >= 1 && ownernum >= 1;

		// generate the number of lines for the document

		int numleft = rn.getRange(MINLINES, MAXLINES);

		while (numleft > 0) {

			int n = rn.getRange(1, 20);

			// add global artifact

			if (n >= 1 && n <= 4) {
				int num = rn.getRange(1, MAXGLOBCOPY);
				int nl = rn.getRange(MINWIN, MAXWIN);
				for (int i = 1; i <= nl; i++) {
					String s = "global:" + num + ":" + i;
					pw.printf("%s\n", s);
					numleft--;
				}
			}

			// add possible owner copy

			else if (n == 5) {
				int num = rn.getRange(1, MAXOWNERCOPY);
				int nl = rn.getRange(MINWIN, MAXWIN);
				for (int i = 1; i <= nl; i++) {
					String s = "owner:" + ownernum +
					":" + num + ":" + i;
					pw.printf("%s\n", s);
					numleft--;
				}
			}

			// add random line

			else {
				String s = "random:" + rn.getRange(1, MAXRAND);
				pw.printf("%s\n", s);
				numleft--;
			}
		}

	}

	public static void main(String[] args) throws IOException {

		// get the output directory

		if (args.length < 1) {
			System.err.println("*** missing output directory ***");
			System.exit(1);
		}

		File dir = new File(args[0]);

		if (!dir.isDirectory()) {
			System.err.println("*** invalid directory ***");
			System.exit(1);
		}

		// get the number of documents

		int num = (args.length >= 2 ?
			Integer.parseInt(args[1]) : MAXDOCS);

		// generate the documents

		for (int i = 1; i <= num; i++)
			gen(dir, i);
	}
}
