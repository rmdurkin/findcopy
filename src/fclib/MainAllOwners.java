package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

// this version runs FindCopy for a single owner

public class MainAllOwners {
	public static void main(String[] args) throws IOException {

		// check arguments

		if (args.length != 1) {
			System.err.println("missing docsdir");
			System.exit(1);
		}

		// get list of document pathnames

		ArrayList<String> pathlist = new DirWalk(args[0]).getPaths();

		// load a bad signature file

		try {
			BadSig.loadFile(Config.BAD_SIG2);
		}
		catch (IOException e) {
			throw new FindCopyException("MainAllOwners");
		}

		// set up a FindCopy object and add owner's documents

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL);		

		FindCopy fcbad = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL_BAD);		

		for (String s : pathlist) {
			DocAttrRandDocs da = new DocAttrRandDocs(s);
			fc.addDocument(da);
			fcbad.addDocument(da);
		}

		FilterCopy fic = new FilterCopy(fc);
		FilterCopy ficbad = new FilterCopy(fcbad);

		System.out.println(fic.size() + " -> " + ficbad.size());
	}
}
