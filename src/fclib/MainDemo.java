package fclib;
import fclib.*;
import java.util.*;

// driver program for randomly generated documents

public class MainDemo {
	private static final int MAXDOCS = 2500;

	// correlate the global and owner copy/paste instances

	private static void correlate(FindCopy glob, FindCopy owner) {
		assert glob != null && owner != null;

		int numgood = 0;
		int numbad = 0;

		// iterate across owner's signatures

		for (WinSig sig : owner) {
			WinLocList ownerlist = (WinLocList)owner.get(sig);

			// skip if no duplicates

			if (ownerlist.size() < 2)
				continue;

			// get global entry for signature

			WinLocList globlist = (WinLocList)glob.get(sig);

			// compare numbers of entries

			System.out.print("owner size = " + ownerlist.size());
			System.out.print(" ");
			System.out.print("glob size = " + globlist.size());

			// skip if there are other global entries

			if (ownerlist.size() < globlist.size()) {
				System.out.println(" (skip)");
				System.out.println("=====================");
				numgood++;
				continue;
			}

			// all collisions are in owner's documents

			System.out.println();

			// print out list of locations

			System.out.println("---");
			System.out.println("(locations)");

			for (int j = 0; j < ownerlist.size(); j++)
				System.out.println(ownerlist.get(j));

			System.out.println("---");

			// print out actual window

			System.out.println("(window text)");

			WinLoc loc = ownerlist.get(0);

			ArrayList<String> lines = loc.getDocAttr().getLines();

			int lo = loc.getStartln();
			int hi = lo + loc.getNumln() - 1;
			boolean bad = false;

			for (int j = lo; j <= hi; j++) {
				String s = lines.get(j);
				bad = bad || s.indexOf("global") >= 0;
				System.out.println(s);
			}

			// tally up good/bad

			if (bad)
				numbad++;
			else
				numgood++;

			System.out.println("=====================");
		}

		System.out.println("numgood = " + numgood);
		System.out.println("numbad = " + numbad);
	}

	public static void main(String[] args) {

		// create a database of documents

		ArrayList<DocAttr> db = new ArrayList<DocAttr>(MAXDOCS);

		for (int i = 1; i <= MAXDOCS; i++)
			db.add(new DocAttrRand("doc" + i));

		// create a database of signatures for the documents

		FindCopy fcglob = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL);

		for (int i = 0; i < db.size(); i++)
			fcglob.addDocument(db.get(i));

		// find documents for a specific owner

		String owner = "owner1";

		ArrayList<DocAttr> dbowner = new ArrayList<DocAttr>();

		for (int i = 0; i < db.size(); i++) {
			if (db.get(i).getOwner().equals(owner))
				dbowner.add(db.get(i));
		}

		// create a database of signatures for the owner

		FindCopy fcowner = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL);

		for (int i = 0; i < dbowner.size(); i++)
			fcowner.addDocument(dbowner.get(i));

		// correlate the lists of signatures

		correlate(fcglob, fcowner);
	}
}
