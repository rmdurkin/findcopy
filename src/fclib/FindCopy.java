package fclib;

import java.io.*;
import java.util.*;

public class FindCopy implements Iterable<WinSig> {

	// hash table that stores information for a given hash key
	// the hash key is the result of hashing a window of text lines

	private LinearHash<WinSig, WinInfo> table =
		new LinearHash<WinSig, WinInfo>(Config.HASH_SIZE);

	// list of window sizes

	private int[] winlist;

	// ValidWin callback

	private ValidWin vw;

	// ValidSig callback

	private ValidSig vs;

	// count of total number of windows seen and recorded

	private int windowcount;

	// constructor is passed a list of window sizes,
	// along with a ValidWin callback object (may be null)

	public FindCopy(int[] winlist, ValidWin vw, ValidSig vs) {
		if (winlist == null || winlist.length == 0 || vs == null)
			throw new IllegalArgumentException();

		// check window lengths

		for (int i = 0; i < winlist.length; i++) {
			if (winlist[i] < Config.MIN_WIN)
				throw new IllegalArgumentException();
			if (winlist[i] > Config.MAX_WIN)
				throw new IllegalArgumentException();
		}

		this.winlist = winlist;

		this.vw = vw;

		this.vs = vs;

		windowcount = 0;
	}

	// add a document with the specifed DocAttr

	public void addDocument(DocAttr da) {
		if (da == null)
			throw new IllegalArgumentException();

		// preprocess document

		ArrayList<String> list = UtilFuncs.prepLine1(da.getLines());

		// iterate across window sizes

		for (int i = 0; i < winlist.length; i++)
			addDocument(da, list, winlist[i]);
	}

	// internal version of addDocument()

	private void addDocument(DocAttr da, ArrayList<String> list, int win) {
		assert da != null && list != null && win >= 1;

		int listlen = list.size();

		// iterate across the text lines

		for (int i = 0; i <= listlen - win; i++) {
			int lo = i;
			int hi = i + win - 1;
			int newwin = win;

			// don't start with a blank line

			if (list.get(lo).length() == 0)
				continue;

			// trim trailing blank lines

			while (list.get(hi).length() == 0) {
				hi--;
				newwin--;
			}

			assert newwin >= 1;

			// callback to see if window is okay

			if (vw != null && !vw.isValidWin(list, lo, newwin))
				continue;

			// compute a hash code for a given window

			CryptoHash h = new CryptoHash(Constants.HASH_ALG);

			for (int j = lo; j <= hi; j++)
				h.addLine(list.get(j));

			// get the signature and location

			WinSig sig = h.getWinSig();

			WinLoc loc = new WinLoc(da, lo, newwin);

			// do callback to validate the signature

			WinInfo wi1 = table.get(sig);

			WinInfo wi2 = vs.isValidSig(sig, loc, wi1);

			// skip window if return value from callback is null

			if (wi2 == null)
				continue;

			// put the key/location pair in the hash table

			if (wi2 != wi1)
				table.put(sig, wi2);

			// bump the count of total windows seen

			windowcount++;
		}
	}

	// get number of signatures in the table

	public int size() {
		return table.size();
	}

	// get a count of the total number of windows seen and recorded

	public int getWindowCount() {
		return windowcount;
	}

	// get the iterator

	public Iterator<WinSig> iterator() {
		return table.iterator();
	}

	// get the entry for a signature

	public WinInfo get(WinSig sig) {
		if (sig == null)
			throw new IllegalArgumentException();

		return table.get(sig);
	}

	// print the contents of the table

	public void printTable() {
		for (WinSig sig : table)
			get(sig).printInfo();
	}

	// verify the contents of the table

	public void verifyTable() {
		for (WinSig sig : table)
			get(sig).verifyInfo();
	}

	// walk through the table and call a user-defined method

	public void walkTable(Object obj) {
		for (WinSig sig : table)
			get(sig).walkInfo(obj);
	}
}
