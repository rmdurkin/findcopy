package fclib;

// class used to represent common sequences produced by CommonLines

public class CommonLinesEntry implements Comparable<CommonLinesEntry> {

	// length of the sequence >= 1

	private int len;

	// starting positions in the two documents >= 0

	private int pos1;
	private int pos2;

	// used when folding sequences

	private boolean used;

	// constructor

	public CommonLinesEntry(int len, int pos1, int pos2) {
		if (len < 1 || pos1 < 0 || pos2 < 0)
			throw new IllegalArgumentException();

		this.len = len;

		this.pos1 = pos1;
		this.pos2 = pos2;

		this.used = false;
	}

	// get the length

	public int getLen() {
		return len;
	}

	// get the starting position in the first document

	public int getPos1() {
		return pos1;
	}

	// get the starting position in the second document

	public int getPos2() {
		return pos2;
	}

	// get the used field

	public boolean getUsed() {
		return used;
	}

	// set the used field

	public void setUsed() {
		used = true;
	}

	// equals

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CommonLinesEntry))
			return false;

		CommonLinesEntry other = (CommonLinesEntry)obj;

		if (this == other)
			return true;

		if (len != other.len)
			return false;

		if (pos1 != other.pos1)
			return false;

		if (pos2 != other.pos2)
			return false;

		return true;
	}

	// compare one CommonLinesEntry to another

	public int compareTo(CommonLinesEntry other) {
		if (other == null)
			throw new IllegalArgumentException();

		if (this == other)
			return 0;

		if (len > other.len)
			return -1;
		else if (len < other.len)
			return 1;

		if (pos1 < other.pos1)
			return -1;
		else if (pos1 > other.pos1)
			return 1;

		if (pos2 < other.pos2)
			return -1;
		else if (pos2 > other.pos2)
			return 1;

		return 0;
	}
}
