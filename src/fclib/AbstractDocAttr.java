package fclib;

import java.util.*;

// abstract class that partially implements DocAttr

public abstract class AbstractDocAttr implements DocAttr, Comparable<DocAttr> {

	// document ID

	public abstract String getId();

	// document owner

	public abstract String getOwner();

	// timestamp for the document

	public abstract Date getTimeStamp();

	// number of days since last modification

	public double getAgeDays() {
		return UtilFuncs.getElapsedTime(getTimeStamp());
	}

	// text lines of the document

	public abstract ArrayList<String> getLines();

	// attributes in string form

	public String toString() {
		return getTimeStamp().getTime() + ":" + getOwner() +
			":" + getId();
	}

	// order relative to another DocAttr

	public int compareTo(DocAttr other) {
		if (other == null)
			throw new IllegalArgumentException();

		if (this == other)
			return 0;

		int ct = getTimeStamp().compareTo(other.getTimeStamp());
		if (ct != 0)
			return ct;

		return getId().compareTo(other.getId());
	}

	// equals

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DocAttr))
			return false;

		DocAttr other = (DocAttr)obj;

		if (this == other)
			return true;

		if (!getTimeStamp().equals(other.getTimeStamp()))
			return false;

		if (!getOwner().equals(other.getOwner()))
			return false;

		if (!getId().equals(other.getId()))
			return false;

		return true;
	}
}
