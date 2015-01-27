package fclib;

import java.util.*;

// stores a count of WinLocs for a specific signature

public class WinLocHit implements WinInfo {
	private WinSig sig;

	private int numhits;

	// constructor

	public WinLocHit(WinSig sig) {
		assert sig != null;

		this.sig = sig;

		numhits = 0;
	}

	// update the count

	public void updateInfo(WinSig sig, WinLoc loc) {
		numhits++;
	}

	// print the count and signature

	public void printInfo() {
		System.out.println(numhits + "   " + sig);
	}

	// verify the count

	public void verifyInfo() {}

	// arbitrary callback usable for any purpose

	public void walkInfo(Object obj) {}
}
