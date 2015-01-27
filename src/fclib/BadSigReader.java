package fclib;

import java.io.*;

// read a list of bad signatures from a text file

public class BadSigReader {

	// list of bad signatures

	private LinearSet<WinSig> badsig;

	// constructor

	public BadSigReader(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		// check for existence of file

		if (!new File(fn).exists())
			throw new IOException();

		// check age of file

		double d = UtilFuncs.getFileAgeDays(fn);
		if (d > CopyPasteConfig.MAX_BAD_SIG_DAYS)
			throw new FindCopyException("BadSigReader");

		// get the lines of the file

		FileLineReader flr = new FileLineReader(fn);

		// check settings used to create the file

		if (!flr.get(0).equals(UtilFuncs.getSettings()))
			throw new FindCopyException("BadSigReader");

		// read in the signatures

		badsig = new LinearSet<WinSig>();

		for (int i = 1; i < flr.size(); i++) {
			byte[] buf = UtilFuncs.hexToByte(flr.get(i));
			WinSig sig = new ByteString(buf);
			assert sig.length() == Constants.HASH_LEN_BYTES;
			badsig.put(sig);
		}
	}

	// check a signature

	public boolean isBadSig(WinSig sig) {
		if (sig == null || sig.length() != Constants.HASH_LEN_BYTES)
			throw new IllegalArgumentException();

		return badsig.containsKey(sig);
	}
}
