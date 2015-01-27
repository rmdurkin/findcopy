package fclib;

import java.io.*;
import java.util.*;

// arbitrary list of text windows to be excluded from analysis

public class ExclusionList {

	// pathname of exclusion file

	private String fn;

	// list of lines in file

	private ArrayList<String> elist;

	// list of signatures for text windows

	private LinearSet<WinSig> siglist;

	// compute the signature of a WinLoc

	private WinSig getsig(WinLoc loc) {
		assert loc != null;

		ArrayList<String> list = Config.PT.prepWinLoc(loc);
		assert list.size() >= 1;

		CryptoHash h = new CryptoHash(Constants.HASH_ALG);

		for (String s : list)
			h.addLine(s);

		return h.getWinSig();
	}

	// constructor

	public ExclusionList(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		this.fn = fn;

		// read in file if it exists, else start a new list

		if (new File(fn).exists())
			elist = new FileLineReader(fn).getList();
		else
			elist = new ArrayList<String>();

		// create a list of signatures for all the entries

		siglist = new LinearSet<WinSig>();

		int i = 0;

		while (i < elist.size()) {
			int currlen = Integer.parseInt(elist.get(i++));
			assert currlen >= 1;

			CryptoHash h = new CryptoHash(Constants.HASH_ALG);

			for (int j = 1; j <= currlen; j++) {
				String s = Config.PT.prepLine(elist.get(i++));
				h.addLine(s);
			}

			siglist.put(h.getWinSig());
		}
	}

	// see if an entry is on the list

	public boolean findEntry(WinLoc loc) {
		if (loc == null)
			throw new IllegalArgumentException();

		// get the signature for the WinLoc

		WinSig sig = getsig(loc);

		// check to see if the signature exists

		return siglist.containsKey(sig);
	}

	// add an entry to the list

	public void addEntry(WinLoc loc) throws IOException {
		if (loc == null)
			throw new IllegalArgumentException();

		// get the signature for the WinLoc

		WinSig sig = getsig(loc);

		// check to see if the signature exists

		if (siglist.containsKey(sig))
			return;

		// add the signature

		siglist.put(sig);

		// add the lines to the list

		ArrayList<String> list = Config.PT.prepWinLoc(loc);
		assert list.size() >= 1;
		elist.add(Integer.toString(list.size()));
		for (String s : list)
			elist.add(s);

		// write the list

		new FileLineWriter(fn, elist).write();
	}
}
