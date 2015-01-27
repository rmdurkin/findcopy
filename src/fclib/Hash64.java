package fclib;

// 64-bit hash for a list of strings

public class Hash64 {
	private long hash;

	private boolean added;

	// constructor

	public Hash64() {
		hash = 0;

		added = false;
	}

	// add a string

	public void addString(String s) {
		if (s == null || s.length() == 0)
			throw new IllegalArgumentException();

		int len = s.length();

		for (int i = 0; i < len; i++)
			hash = hash * 31 + s.charAt(i);

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

	// return the hash code

	public long getHash() {
		assert added;

		return hash;
	}
}
