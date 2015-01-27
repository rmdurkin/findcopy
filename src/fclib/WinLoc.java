package fclib;

import java.util.*;

// represents a specific location of a window of text

public class WinLoc implements Comparable<WinLoc>, java.io.Serializable {

	// the underlying document where the window occurs

	private DocAttr da;

	// starting line >= 0

	private int startln;

	// number of lines >= 1

	private int numln;

	// constructor

	public WinLoc(DocAttr da, int startln, int numln) {
		if (da == null)
			throw new IllegalArgumentException();
		if (startln < 0 || numln < 1)
			throw new IllegalArgumentException();
		if (startln + numln - 1 >= da.getLines().size())
			throw new IllegalArgumentException();

		this.da = da;

		this.startln = startln;

		this.numln = numln;
	}

	// return the DocAttr

	public DocAttr getDocAttr() {
		return da;
	}

	// return the starting line >= 0

	public int getStartln() {
		return startln;
	}

	// return the number of lines in the window

	public int getNumln() {
		return numln;
	}

	// format the location into a string

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(da.toString());
		sb.append('[');
		sb.append(getStartln());
		sb.append(':');
		sb.append(getNumln());
		sb.append(']');

		return sb.toString();
	}

	// print window lines to standard output

	public void printLines() {
		ArrayList<String> lines = getDocAttr().getLines();

		int base = getStartln();
		int num = getNumln();

		for (int i = base; i < base + num; i++)
			System.out.println(lines.get(i));
	}

	// equals

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof WinLoc))
			return false;

		WinLoc other = (WinLoc)obj;

		if (this == other)
			return true;

		if (!da.equals(other.da))
			return false;

		if (startln != other.startln)
			return false;

		if (numln != other.numln)
			return false;

		return true;
	}

	// compute order relative to another WinLoc

	public int compareTo(WinLoc other) {
		if (other == null)
			throw new IllegalArgumentException();

		if (this == other)
			return 0;

		// look at order of underlying documents

		int ct = da.compareTo(other.da);
		if (ct != 0)
			return ct;

		// compare starting lines

		if (startln < other.startln)
			return -1;
		else if (startln > other.startln)
			return 1;

		// compare number of lines

		if (numln < other.numln)
			return -1;
		else if (numln > other.numln)
			return 1;

		return 0;
	}
}
