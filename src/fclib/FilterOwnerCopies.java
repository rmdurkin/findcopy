package fclib;

import java.io.*;
import java.util.*;

// filter the results of FindOwnerCopies

public class FilterOwnerCopies implements Iterable<CopyPasteEntry> {

	// CopyPastePolicy object;

	private CopyPastePolicy cpp;

	// list of copy/paste entries

	private ArrayList<CopyPasteEntry> cpelist;

	// number of signatures

	private int num_sig = 0;

	// number of signatures after filtering

	private int num_good_sig = 0;

	// number of bad signatures

	private int num_bad_sig = 0;

	// number of locations across all signatures

	private int num_loc = 0;

	// number of locations after filtering

	private int num_good_loc = 0;

	// number of bad locations

	private int num_bad_loc = 0;

	// process the entries in the hash table

	private void process(FindOwnerCopies foc) {
		assert foc != null;

		for (WinSig sig : foc) {
			num_sig++;

			ArrayList<WinLoc> wll = foc.getList(sig);

			num_loc += wll.size();

			// check bad signature database

			BadSigReader bsr = cpp.getBadSigReader();
			boolean bs = (bsr != null && bsr.isBadSig(sig));

			// check SyntaxFilter

			SyntaxFilter sfi = cpp.getSyntaxFilter();
			boolean sf = (sfi != null && sfi.isBadSyn(wll.get(0)));

			// check exclusion list

			ExclusionList el = cpp.getExclusionList();
			boolean elf = (el != null && el.findEntry(wll.get(0)));

			// get line-based information content

			double line_info = Constants.BAD_WEIGHT;
			LineFreqReader lfr = cpp.getLineFreqReader();
			if (lfr != null) {
				ArrayList<String> list =
					Config.PT.prepWinLoc(wll.get(0));
				line_info = lfr.getScaledWeight(list);
				assert line_info >= 0;
			}

			// get word-based information content

			double word_info = Constants.BAD_WEIGHT;
			WordFreqReader wfr = cpp.getWordFreqReader();
			if (wfr != null) {
				ArrayList<String> list =
					Config.PT.prepWinLoc(wll.get(0));
				word_info = wfr.getScaledWeight(list);
				assert word_info >= 0;
			}

			boolean good = (!bs && !sf && !elf &&
				line_info >= 1.0 && word_info >= 1.0);

			if (good) {
				num_good_sig++;
				num_good_loc += wll.size();
			}
			else {
				num_bad_sig++;
				num_bad_loc += wll.size();
			}

			CopyPasteScore cps = new CopyPasteScore();

			cpelist.add(new CopyPasteEntry(sig, wll, bs, sf, elf,
				line_info, word_info, cps, false));
		}

		assert num_good_sig + num_bad_sig == num_sig;
		assert num_good_loc + num_bad_loc == num_loc;

		Collections.sort(cpelist);
	}

	// constructor

	public FilterOwnerCopies(FindOwnerCopies foc, CopyPastePolicy cpp)
	throws IOException {
		if (foc == null || cpp == null)
			throw new IllegalArgumentException();

		this.cpp = cpp;

		cpelist = new ArrayList<CopyPasteEntry>();

		process(foc);
	}

	// iterator

	public Iterator<CopyPasteEntry> iterator() {
		return cpelist.iterator();
	}

	// get number of signatures

	public int getNumSig() {
		return num_sig;
	}

	// get number of signatures after filtering

	public int getNumGoodSig() {
		return num_good_sig;
	}

	// get number of bad signatures

	public int getNumBadSig() {
		return num_bad_sig;
	}

	// get percentage of bad signatures

	public int getPercBadSig() {
		if (num_sig == 0)
			return 0;
		else
			return (int)(num_bad_sig * 100.0 / num_sig + 0.5);
	}

	// get number of locations

	public int getNumLoc() {
		return num_loc;
	}

	// get number of locations after filtering

	public int getNumGoodLoc() {
		return num_good_loc;
	}

	// get number of bad locations

	public int getNumBadLoc() {
		return num_bad_loc;
	}

	// get average number of locations per signature

	public double getAvgGoodLoc() {
		if (num_good_loc == 0)
			return 0;
		else
			return (double)num_good_loc / num_good_sig;
	}
}
