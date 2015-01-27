package fclib;

import java.util.*;

// find raw copy/paste instances for a specific owner

public class FindOwnerCopies implements Iterable<WinSig> {

	// owner

	private String owner;

	// CopyPastePolicy object

	private CopyPastePolicy cpp;

	// work documents

	private ArrayList<DocAttr> workdocs;

	// hash table that maps WinSig to ArrayList<WinLoc>

	private LinearHash<WinSig, ArrayList<WinLoc>> table =
		new LinearHash<WinSig, ArrayList<WinLoc>>();

	// get recent documents

	private void getdocs() {
		ArrayList<DocAttr> docs =
			Config.DB.getAllDocAttrForOwner(owner);

		workdocs = new ArrayList<DocAttr>();

		for (DocAttr da : docs) {
			double d = da.getAgeDays();
			if (d >= cpp.getMinRecentDays() &&
			d <= cpp.getMaxRecentDays())
				workdocs.add(da);
		}
	}

	// do pairwise comparisons

	private void compare() {
		int sz = workdocs.size();

		for (int i = 0; i < sz - 1; i++)
			for (int j = i + 1; j < sz; j++)
				compare(workdocs.get(i), workdocs.get(j));
	}

	// compare two documents

	private void compare(DocAttr da1, DocAttr da2) {
		assert da1 != null && da2 != null && !da1.equals(da2);

		// get the lists of lines and prep them

		ArrayList<String> list1 = da1.getLines();
		ArrayList<String> list2 = da2.getLines();

		list1 = Config.PT.prepList(list1);
		list2 = Config.PT.prepList(list2);

		// do the actual comparison

		CommonLines cl = new CommonLines(list1, list2);

		ArrayList<CommonLinesEntry> clelist =
			cl.getSequences(Config.WIN_SIZE_STD);

		// for each common entry, get the signature
		// and update the hash table with the locations

		for (CommonLinesEntry cle : clelist) {
			int pos1 = cle.getPos1();
			int pos2 = cle.getPos2();
			int len = cle.getLen();
			assert pos1 >= 0 && pos2 >= 0 && len >= 1;

			CryptoHash h = new CryptoHash(Constants.HASH_ALG);

			for (int i = 0; i < len; i++)
				h.addLine(list1.get(pos1 + i));

			WinSig sig = h.getWinSig();
			assert sig.length() == Constants.HASH_LEN_BYTES;

			WinLoc loc1 = new WinLoc(da1, pos1, len);
			WinLoc loc2 = new WinLoc(da2, pos2, len);

			assert !loc1.equals(loc2);
			assert UtilFuncs.verifyWindow(loc1, loc2);

			ArrayList<WinLoc> loc = table.get(sig);

			if (loc == null) {
				loc = new ArrayList<WinLoc>();
				table.put(sig, loc);
			}

			if (!loc.contains(loc1))
				loc.add(loc1);

			if (!loc.contains(loc2))
				loc.add(loc2);
		}
	}

	// wrapup

	private void wrapup() {

		// make sure all the WinLoc lists are sorted

		for (WinSig sig : table) {
			ArrayList<WinLoc> list = table.get(sig);
			int sz = list.size();
			assert sz >= 2;

			Collections.sort(list);

			// check that all WinLocs reference the same text

			for (int i = 0; i < sz - 1; i++) {
				WinLoc loc1 = list.get(i);
				for (int j = i + 1; j < sz; j++) {
					WinLoc loc2 = list.get(j);
					assert !loc1.equals(loc2);
					assert UtilFuncs.verifyWindow(loc1, loc2);
				}
			}
		}
	}

	// iterator

	public Iterator<WinSig> iterator() {
		return table.iterator();
	}

	// get a hash table entry based on a signature

	public ArrayList<WinLoc> getList(WinSig sig) {
		if (sig == null)
			throw new IllegalArgumentException();

		return table.get(sig);
	}

	// constructor

	public FindOwnerCopies(String owner, CopyPastePolicy cpp) {
		if (owner == null || owner.length() == 0 || cpp == null)
			throw new IllegalArgumentException();

		this.owner = owner;
		this.cpp = cpp;

		// get list of recent documents for owner

		getdocs();

		// do pairwise comparisons

		compare();

		// make sure WinLoc lists are sorted and do final checking

		wrapup();
	}
}
