package fclib;

// class that encapsulates a pair of WinLocs

public class WinLocPair implements Comparable<WinLocPair> {

	// values

	private WinLoc first;
	private WinLoc second;

	// constructor

	public WinLocPair(WinLoc first, WinLoc second) {
		if (first == null || second == null)
			throw new IllegalArgumentException();

		this.first = first;

		this.second = second;
	}

	// get the first value

	public WinLoc getFirst() {
		return first;
	}

	// get the second value

	public WinLoc getSecond() {
		return second;
	}

	// convert to a string

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getFirst());

		sb.append(" <===> ");

		sb.append(getSecond());

		return sb.toString();
	}

	// print lines to standard output

	public void printLines() {
		System.out.println(getFirst());
		getFirst().printLines();
		System.out.println("---");
		System.out.println(getSecond());
		getSecond().printLines();
	}

	// compare this WinLocPair against another

	public int compareTo(WinLocPair other) {
		if (other == null)
			throw new IllegalArgumentException();

		if (this == other)
			return 0;

		// compare first document in each pair

		DocAttr da1 = getFirst().getDocAttr();
		DocAttr da2 = other.getFirst().getDocAttr();
		int ct = da1.compareTo(da2);
		if (ct != 0)
			return ct;

		// compare second document in each pair

		da1 = getSecond().getDocAttr();
		da2 = other.getSecond().getDocAttr();
		ct = da1.compareTo(da2);
		if (ct != 0)
			return ct;

		// first starting line number in each pair

		int sln1 = getFirst().getStartln();
		int sln2 = other.getFirst().getStartln();
		if (sln1 < sln2)
			return -1;
		else if (sln1 > sln2)
			return 1;

		// second starting line number in each pair

		sln1 = getSecond().getStartln();
		sln2 = other.getSecond().getStartln();
		if (sln1 < sln2)
			return -1;
		else if (sln1 > sln2)
			return 1;

		return 0;
	}
}
