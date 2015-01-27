package fclib;

// check a signature/window for validity,
// and set up a WinLocOwner object to record owners

// this version checks the bad signature database

public class CheckSigWinLocOwner implements ValidSig {
	public WinInfo isValidSig(WinSig sig, WinLoc loc, WinInfo info) {
		assert sig != null && loc != null;

		// check global list for bad signatures

		if (BadSig.isBadSig(sig))
			return null;

		// if first time around, create an object to store owners

		if (info == null)
			info = new WinLocOwner(sig);

		// update the object

		info.updateInfo(sig, loc);

		return info;
	}
}
