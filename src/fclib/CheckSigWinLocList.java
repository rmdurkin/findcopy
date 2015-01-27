package fclib;

// check a signature/window for validity,
// and set up a WinLocList object to record locations

public class CheckSigWinLocList implements ValidSig {
	public WinInfo isValidSig(WinSig sig, WinLoc loc, WinInfo info) {
		assert sig != null && loc != null;

		// if first time around, create an object to store WinLocs

		if (info == null)
			info = new WinLocList();

		// update the object

		info.updateInfo(sig, loc);

		return info;
	}
}
