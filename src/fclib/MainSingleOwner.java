package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

// this version runs FindCopy for a single owner

public class MainSingleOwner {
	public static void main(String[] args) throws IOException {

		// check arguments

		if (args.length != 2) {
			System.err.println("missing docsdir / owner");
			System.exit(1);
		}

		// get list of document pathnames

		ArrayList<String> pathlist = new DirWalk(args[0]).getPaths();
		String owner = args[1];

		// load a bad signature file if any

		try {
			BadSig.loadFile(Config.BAD_SIG1);
		}
		catch (IOException e) {
		}

		// set up a FindCopy object and add owner's documents

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL_BAD);		

		for (String s : pathlist) {
			DocAttrRandDocs da = new DocAttrRandDocs(s);
			if (da.getOwner().equals(owner))
				fc.addDocument(da);
		}

		// print out the table

		fc.printTable();
	}
}
