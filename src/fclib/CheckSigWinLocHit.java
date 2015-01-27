package fclib;

// check a signature/window for validity,
// and set up a WinLocHit object to record hits

public class CheckSigWinLocHit implements ValidSig {
	public WinInfo isValidSig(WinSig sig, WinLoc loc, WinInfo info) {
		assert sig != null && loc != null;

		// if first time around, create an object to record hits

		if (info == null)
			info = new WinLocHit(sig);

		// update the object

		info.updateInfo(sig, loc);

		return info;
	}
}
