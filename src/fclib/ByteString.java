package fclib;

import java.util.*;

// wrapper for a byte sequence

public class ByteString implements WinSig, java.io.Serializable {
	private byte[] val;

	// constructor

	public ByteString(byte[] val) {
		if (val == null)
			throw new IllegalArgumentException();

		this.val = Arrays.copyOf(val, val.length);
	}

	// length

	public int length() {
		return val.length;
	}

	// check equality with another ByteString

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ByteString))
			return false;

		if (this == obj)
			return true;

		byte[] otherval = ((ByteString)obj).val;

		if (val.length != otherval.length)
			return false;

		for (int i = 0; i < val.length; i++) {
			if (val[i] != otherval[i])
				return false;
		}

		return true;
	}

	// compute the hash code

	public int hashCode() {
		int h = 0;

		for (int i = 0; i < val.length; i++) {
			int b = (val[i] >= 0 ? val[i] : val[i] + 256);
			h = h * 31 + b;
		}

		return h;
	}

	// get the internal byte buffer

	public byte[] getBytes() {
		return val;
	}

	// convert to a hex string

	public String toString() {
		return UtilFuncs.byteToHex(val);
	}
}
