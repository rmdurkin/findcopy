package fclib;

import java.io.*;

// repository for bad signatures

public class BadSig {

	// list of bad signatures

	private static final LinearSet<WinSig> badsig = new LinearSet<WinSig>();

	// no instances of this class can be created

	private BadSig() {}

	// load signatures from a file

	public static void loadFile(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		// check for existence of file

		if (!new File(fn).exists())
			throw new IOException();

		// check age of file

		if (UtilFuncs.getFileAgeDays(fn) > Config.MAX_BAD_SIG_DAYS)
			throw new FindCopyException("loadFile");

		// get the lines of the file

		FileLineReader flr = new FileLineReader(fn);

		// check settings used to create the file

		if (!flr.get(0).equals(UtilFuncs.getSettings()))
			throw new FindCopyException("loadFile");

		// read in the signatures

		for (int i = 1; i < flr.size(); i++) {
			byte[] buf = UtilFuncs.hexToByte(flr.get(i));
			WinSig sig = new ByteString(buf);
			assert sig.length() == Constants.HASH_LEN_BYTES;
			badsig.put(sig);
		}
	}

	// check a signature

	public static boolean isBadSig(WinSig sig) {
		if (sig == null)
			throw new IllegalArgumentException();

		return badsig.containsKey(sig);
	}
}
