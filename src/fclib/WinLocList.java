package fclib;

import java.util.*;

// stores a list of WinLocs for a specific signature

public class WinLocList implements WinInfo {

	// maximum number of displayed locations for a hash entry

	private static final int MAXOUT = 10; // <= 0 means unlimited output

	// current number of WinLocs

	private int currsize;

	// list of WinLocs

	private WinLoc[] list;

	// constructor

	public WinLocList() {
		currsize = 0;

		list = new WinLoc[1];
	}

	// get the current number of WinLocs on the list

	public int size() {
		return currsize;
	}

	// get the i-th WinLoc from the list

	public WinLoc get(int i) {
		if (i < 0 || i >= currsize)
			throw new IndexOutOfBoundsException();

		return list[i];
	}

	// update the list

	public void updateInfo(WinSig sig, WinLoc loc) {
		assert sig != null && loc != null;

		int len = list.length;

		if (currsize == len)
			list = Arrays.copyOf(list, (len * 3) / 2 + 1);

		list[currsize++] = loc;
	}

	// print the list

	public void printInfo() {
		int sz = size();
		assert sz >= 1;

		if (sz < 2)
			return;

		System.out.print(sz + "   ");

		int maxout = sz;
		if (MAXOUT >= 1 && maxout > MAXOUT)
			maxout = MAXOUT;

		for (int i = 0; i < maxout; i++)
			System.out.print(get(i) + " ");

		System.out.println();
	}

	// verify the list

	public void verifyInfo() {
		int sz = size();
		assert sz >= 1;

		if (sz < 2)
			return;

		// make a list of 64-bit hash codes for all windows

		long[] hashlist = new long[sz];

		for (int i = 0; i < sz; i++)
			hashlist[i] = verifyInfo(get(i));

		// compare all combinations

		for (int i = 0; i < sz - 1; i++) {
			for (int j = i + 1; j < sz; j++) {
				System.out.print(get(i) + " " + get(j) + " ");
				if (hashlist[i] == hashlist[j])
					System.out.println("verified ...");
				else
					System.out.println("bad ...");
			}
		}
	}

	private long verifyInfo(WinLoc loc) {
		assert loc != null;

		// compute the hash code

		Hash64 h = new Hash64();

		ArrayList<String> lines = UtilFuncs.prepLine1(loc);

		for (String s : lines)
			h.addLine(s);

		return h.getHash();
	}

	// arbitrary callback usable for any purpose

	public void walkInfo(Object obj) {}
}
