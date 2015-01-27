package fclib;

// class that encapsulates a pair of strings

public class StringPair {

	// values

	private String first;
	private String second;

	// constructor

	public StringPair(String first, String second) {
		if (first == null || second == null)
			throw new IllegalArgumentException();

		this.first = first;

		this.second = second;
	}

	// get the first value

	public String getFirst() {
		return first;
	}

	// get the second value

	public String getSecond() {
		return second;
	}

	// equality check

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof StringPair))
			return false;

		StringPair other = (StringPair)obj;

		if (this == other)
			return true;

		if (!getFirst().equals(other.getFirst()))
			return false;

		if (!getSecond().equals(other.getSecond()))
			return false;

		return true;
	}

	// hash code

	public int hashCode() {
		int h = 0;
		int slen;

		slen = first.length();
		for (int i = 0; i < slen; i++)
			h = h * 31 + first.charAt(i);

		slen = second.length();
		for (int i = 0; i < slen; i++)
			h = h * 31 + second.charAt(i);

		return h;
	}
}
