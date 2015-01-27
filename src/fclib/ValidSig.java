package fclib;

// callback mechanism to validate a window signature

// returns null for invalid windows / signatures

public interface ValidSig {
	WinInfo isValidSig(WinSig sig, WinLoc loc, WinInfo info);
}
