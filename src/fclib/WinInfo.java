package fclib;

// represents information stored for a particular WinSig

public interface WinInfo {

	// update the information

	void updateInfo(WinSig sig, WinLoc loc);

	// print the information

	void printInfo();

	// verify the information

	void verifyInfo();

	// arbitrary callback usable for any purpose

	void walkInfo(Object obj);
}
