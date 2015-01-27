package fclib;

import java.security.*;
import java.util.*;

// wrapper for MD5 and similar cryptographic hash functions

public class CryptoHash {
	// the message digest

	private MessageDigest md;

	// buffer used to accumulate bytes

	private byte[] buf;

	// current pointer into the buffer

	private int currptr;

	// true if strings have been added

	private boolean added;

	// specify an algorithm to the constructor

	public CryptoHash(String alg) {
		if (alg == null || alg.length() == 0)
			throw new IllegalArgumentException();

		try {
			md = MessageDigest.getInstance(alg);
		}
		catch (NoSuchAlgorithmException e) {
			throw new FindCopyException("CryptoHash");
		}

		buf = new byte[1024];

		currptr = 0;

		added = false;
	}

	// add a string

	public void addString(String s) {
		if (s == null || s.length() == 0)
			throw new IllegalArgumentException();

		int len = s.length();
		int blen = len * 2;

		if (currptr + blen >= buf.length) {
			int newlen = (buf.length * 3) / 2 + 1 + blen;
			buf = Arrays.copyOf(buf, newlen);
		}

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			buf[currptr++] = (byte)((c >>> 8) & 0xff);
			buf[currptr++] = (byte)((c >>> 0) & 0xff);
		}

		added = true;
	}

	// add a line

	public void addLine(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		if (s.length() != 0)
			addString(s);

		addString(Constants.EOL);
	}

	// convert the result to a WinSig

	public WinSig getWinSig() {
		assert added;

		md.update(buf, 0, currptr);

		return new ByteString(md.digest());
	}
}
