package fclib;

import java.io.*;

// accumulate a list of bad signatures and write them to a text file

public class BadSigWriter {

	private FileLineWriter flw;

	// constructor

	public BadSigWriter(String fn) {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		flw = new FileLineWriter(fn);

		flw.add(UtilFuncs.getSettings());
	}

	// add a signature to the list

	public void add(WinSig sig) {
		if (sig == null || sig.length() != Constants.HASH_LEN_BYTES)
			throw new IllegalArgumentException();

		flw.add(sig.toString());
	}

	// write out the signatures

	public void write() throws IOException {
		flw.write();
	}
}
