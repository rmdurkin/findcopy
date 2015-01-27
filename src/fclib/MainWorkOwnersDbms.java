package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// this program runs FindCopy for a group of owners,
// reading documents from a DBMS

public class MainWorkOwnersDbms {

	private static long numCopies(FindCopy fc) {
		assert fc != null;

		long cnt = 0;

		for (WinSig sig : fc) {
			WinLocList wll = (WinLocList)fc.get(sig);
			int n = wll.size();
			cnt += n * (n - 1) / 2;
		}

		return cnt;
	}

	public static void main(String[] args) {

		// load a bad signature file

		try {
			BadSig.loadFile(Config.BAD_SIG2);
		}
		catch (IOException e) {
			throw new FindCopyException("MainWorkOwnersDbms");
		}

		// get the list of owner IDs

		ArrayList<String> owners = Config.DB.getAllOwnerIds(); // ???
		assert owners.size() >= 1;

		// iterate across the owners

		for (String owner : owners) {

			// set up FindCopy objects

			FindCopy fc = new FindCopy(Config.WIN_SIZE,
				Config.VW, Config.VS_WLL);		

			FindCopy fcbad = new FindCopy(Config.WIN_SIZE,
				Config.VW, Config.VS_WLL_BAD);		

			// get the documents for an owner

			ArrayList<DocAttr> docs =
				Config.DB.getAllDocAttrForOwner(owner);
			assert docs.size() >= 1;

			// add the documents

			for (DocAttr da : docs) {
				double nd = da.getAgeDays();
				if (nd > Config.MAX_RECENT_DAYS)
					continue;
				fc.addDocument(da);
				fcbad.addDocument(da);
			}

			// set up FilterCopy objects

			FilterCopy fic = new FilterCopy(fc);
			FilterCopy ficbad = new FilterCopy(fcbad);

			// verify the copies

			fic.verifyCopies();
			ficbad.verifyCopies();

			// display statistics

			long size = numCopies(fc);
			long sizebad = numCopies(fcbad);

			int ficsize = fic.size();
			int ficsizebad = ficbad.size();

			boolean shortdisplay = false;
			boolean longdisplay = false;

			if (shortdisplay) {
				for (int i = 0; i < ficbad.size(); i++) {
					WinLocPair wlp = ficbad.get(i);
					System.out.println(wlp);
				}
				System.out.println("===============");
			}

			else if (longdisplay) {
				for (int i = 0; i < ficbad.size(); i++) {
					WinLocPair wlp = ficbad.get(i);
					wlp.printLines();
					System.out.println("----------");
				}
				System.out.println("===============");
			}

			else {
				System.out.print(owner + "   " + docs.size() + "   ");
				System.out.print(size + " -> " + sizebad + "   ");
				System.out.print(ficsize + " -> " + ficsizebad + "   ");
				System.out.println();
			}

/* ???
			assert sizebad <= size;
			assert ficsizebad <= ficsize;
			assert ficsize <= size && ficsizebad <= sizebad;
*/

		}
	}
}
