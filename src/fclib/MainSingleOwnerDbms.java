package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// this program runs FindCopy for a single owner,
// reading documents from a DBMS

public class MainSingleOwnerDbms {
	public static void main(String[] args) throws IOException {

		// get the owner ID

		if (args.length != 1) {
			System.err.println("missing ownerid");
			System.exit(1);
		}

		String ownerid = args[0];

		// load a bad signature file if any

		try {
			BadSig.loadFile(Config.BAD_SIG1);
		}
		catch (IOException e) {
		}

		// set up a FindCopy object and add owner's documents

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL_BAD);		

		ArrayList<DocAttr> docs =
			Config.DB.getAllDocAttrForOwner(ownerid);
		assert docs.size() >= 1;

		for (DocAttr da : docs)
			fc.addDocument(da);

		// run FilterCopy

		FilterCopy fic = new FilterCopy(fc);

		// run verification on the copies

		fic.verifyCopies();

		// print out the hits

		boolean oneline = false; // true for one-line summary

		for (int i = 0; i < fic.size(); i++) {
			WinLocPair wlp = fic.get(i);
			if (oneline) {
				System.out.println(wlp);
			}
			else {
				wlp.printLines();
				if (i + 1 < fic.size())
					System.out.println("=================");
			}
		}
	}
}
