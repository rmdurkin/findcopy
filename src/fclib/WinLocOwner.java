package fclib;

import java.io.*;
import java.util.*;

// stores a list of owners for a specific signature

public class WinLocOwner implements WinInfo {

	// true if saturation level reached

	private boolean bad;

	// current number of owners

	private int currsize;

	// list of owners

	private String[] list;

	// number of hits

	private int numhits;

	// signature

	private WinSig sig;

	// constructor

	public WinLocOwner(WinSig sig) {
		assert sig != null;

		currsize = 0;

		list = new String[1];

		this.sig = sig;

		numhits = 0;

		bad = false;
	}

	// get the current number of owners on the list

	public int size() {
		return currsize;
	}

	// get the i-th owner from the list

	public String get(int i) {
		if (i < 0 || i >= currsize)
			throw new IndexOutOfBoundsException();

		return list[i];
	}

	// update the list

	public void updateInfo(WinSig sig, WinLoc loc) {

		// increment the number of hits

		numhits++;

		// if already bad, then bail out

		if (bad)
			return;

		if (currsize == Config.MAX_OWNERS)
			return;

		// see if the owner is already on the list

		String owner = loc.getDocAttr().getOwner();

		for (int i = 0; i < currsize; i++) {
			if (owner.equals(list[i]))
				return;
		}

		// add the owner

		int len = list.length;

		if (currsize == len)
			list = Arrays.copyOf(list, (len * 3) / 2 + 1);

		list[currsize++] = owner;

		// see if at saturation level

		if (currsize >= Config.MAX_OWNERS && numhits >= Config.MAX_HITS)
			bad = true;
	}

	// print the list

	public void printInfo() {
		int sz = size();
		assert sz >= 1;

		System.out.print(sz + "   " + numhits + "   ");

		for (int i = 0; i < sz; i++)
			System.out.print(get(i) + " ");

		System.out.println();
	}

	// verify the list

	public void verifyInfo() {}

	// write out bad signatures

	public void walkInfo(Object obj) {
		assert obj != null;

		if (!bad)
			return;

		PrintWriter pw = (PrintWriter)obj;

		pw.printf("%s\n", sig);
	}
}
