package fclib;

import java.util.*;

// filter raw copy/paste instances

public class FilterCopy {

	// the FindCopy instance

	private FindCopy fc;

	// output list of WinLoc pairs

	private ArrayList<WinLocPair> list;

	// iterate across all signatures and add
	// copy/paste pairs to the output list

	private void collect() {
		for (WinSig sig : fc) {
			WinInfo wi = fc.get(sig);
			assert wi != null;
			if (wi instanceof WinLocList)
				processlist((WinLocList)wi);
		}
	}

	// process a WinLocList

	private void processlist(WinLocList wll) {
		assert wll != null;

		int sz = wll.size();

		// identify all pairs of WinLocs

		for (int i = 0; i < sz - 1; i++) {
			WinLoc loc1 = wll.get(i);

			for (int j = i + 1; j < sz; j++) {
				WinLoc loc2 = wll.get(j);

				assert j > i && loc2 != loc1;
				assert loc1.getNumln() == loc2.getNumln();

				WinLocPair wlp;

				// create a WinLocPair for each pair

				if (loc1.compareTo(loc2) <= 0)
					wlp = new WinLocPair(loc1, loc2);
				else
					wlp = new WinLocPair(loc2, loc1);

				// add the pair to the output list

				list.add(wlp);
			}
		}
	}

	// sort the output list

	private void sort() {
		Collections.sort(list);
	}

	// fold overlapping windows into one

	private void fold() {
		DocAttr prevda1 = null;
		DocAttr prevda2 = null;
		String previd1 = null;
		String previd2 = null;
		int currbase1 = -1;
		int currbase2 = -1;
		int currnumln1 = 0;
		int currnumln2 = 0;

		ArrayList<WinLocPair> out = new ArrayList<WinLocPair>();

		for (int i = 0; i < list.size(); i++) {

			// get the current pair

			WinLocPair wlp = list.get(i);

			WinLoc loc1 = wlp.getFirst();
			WinLoc loc2 = wlp.getSecond();

			assert loc1.getNumln() == loc2.getNumln();

			DocAttr currda1 = loc1.getDocAttr();
			DocAttr currda2 = loc2.getDocAttr();
			String currid1 = currda1.getId();
			String currid2 = currda2.getId();

			// see if it's a continuation of the previous

			boolean b = true;
			int ln1 = loc1.getStartln();
			int ln2 = loc2.getStartln();

			if (b) {
				b = (previd1 != null &&
					previd2 != null &&
					previd1.equals(currid1) &&
					previd2.equals(currid2));
			}
			
			if (b) {
				assert currbase1 >= 0 && currbase2 >= 0;

				b = (ln1 - currbase1 == ln2 - currbase2);
			}

			if (b) {
				assert currbase1 >= 0 && currnumln1 >= 1;

				b = (ln1 >= currbase1 &&
					ln1 <= currbase1 + currnumln1 - 1);
			}

			if (b) {
				assert currbase2 >= 0 && currnumln2 >= 1;

				b = (ln2 >= currbase2 &&
					ln2 <= currbase2 + currnumln2 - 1);
			}

			// if not a continuation, add old to output

			if (!b) {
				if (prevda1 != null) {
					assert currnumln1 == currnumln2;

					assert currbase1 >= 0 &&
						currnumln1 >= 1;
					WinLoc tmp1 = new WinLoc(prevda1,
						currbase1, currnumln1);

					assert currbase2 >= 0 &&
						currnumln2 >= 1;
					WinLoc tmp2 = new WinLoc(prevda2,
						currbase2, currnumln2);

					out.add(new WinLocPair(tmp1, tmp2));
				}

				// update current base

				prevda1 = currda1;
				prevda2 = currda2;

				previd1 = currid1;
				previd2 = currid2;

				currbase1 = loc1.getStartln();
				currbase2 = loc2.getStartln();

				currnumln1 = loc1.getNumln();
				currnumln2 = loc2.getNumln();
			}

			// otherwise update current information

			else {
				int oldhi1 = currbase1 + currnumln1 - 1;
				int oldhi2 = currbase2 + currnumln2 - 1;

				int newhi1 = loc1.getStartln() +
					loc1.getNumln() - 1;
				int newhi2 = loc2.getStartln() +
					loc2.getNumln() - 1;

				assert newhi1 >= oldhi1;
				assert newhi2 >= oldhi2;

				currnumln1 += newhi1 - oldhi1;
				currnumln2 += newhi2 - oldhi2;
			}

			// if the last pair, then output

			if (i + 1 == list.size()) {
				assert currnumln1 == currnumln2;

				assert currbase1 >= 0 && currnumln1 >= 1;
				WinLoc tmp1 = new WinLoc(currda1,
					currbase1, currnumln1);

				assert currbase2 >= 0 && currnumln2 >= 1;
				WinLoc tmp2 = new WinLoc(currda2,
					currbase2, currnumln2);

				out.add(new WinLocPair(tmp1, tmp2));
			}
		}

		// wrapup

		out.trimToSize();

		foldcheck(list, out);

		list = out;
	}

	// further checking on results of fold()

	private void foldcheck(ArrayList<WinLocPair> inlist,
	ArrayList<WinLocPair> outlist) {

		// general checks

		assert inlist != null && outlist != null;

		assert outlist.size() <= inlist.size();

		assert (outlist.size() != 0) == (inlist.size() != 0);

		// check that input/output lists have
		// the same number of document pairs

		LinearSet<StringPair> sp1 = new LinearSet<StringPair>();

		for (int i = 0; i < inlist.size(); i++) {
			WinLocPair wlp = inlist.get(i);
			String id1 = wlp.getFirst().getDocAttr().getId();
			String id2 = wlp.getSecond().getDocAttr().getId();
			sp1.put(new StringPair(id1, id2));
		}

		LinearSet<StringPair> sp2 = new LinearSet<StringPair>();

		for (int i = 0; i < outlist.size(); i++) {
			WinLocPair wlp = outlist.get(i);
			String id1 = wlp.getFirst().getDocAttr().getId();
			String id2 = wlp.getSecond().getDocAttr().getId();
			sp2.put(new StringPair(id1, id2));
		}

		assert sp1.size() == sp2.size();

		assert (sp1.size() != 0) == (outlist.size() != 0);

		// check for matching document pairs

		for (StringPair sp : sp1)
			assert sp2.containsKey(sp);
	}

	// constructor

	public FilterCopy(FindCopy fc) {
		if (fc == null)
			throw new IllegalArgumentException();

		this.fc = fc;

		list = new ArrayList<WinLocPair>();

		// make a list of WinLoc pairs

		collect();

		// sort them

		sort();

		// fold overlapping windows into one

		fold();
	}

	// get the size of the output list

	public int size() {
		return list.size();
	}

	// get the i-th WinLocPair from the sorted output list

	public WinLocPair get(int i) {
		if (i < 0 || i >= size())
			throw new IllegalArgumentException();

		return list.get(i);
	}

	// verify the list

	public void verifyCopies() {
		for (int i = 0; i < list.size(); i++) {
			WinLocPair wlp = list.get(i);

			WinLoc loc1 = wlp.getFirst();
			WinLoc loc2 = wlp.getSecond();

			if (!UtilFuncs.verifyWindow(loc1, loc2))
				throw new FindCopyException("verifyCopies");
		}
	}
}
