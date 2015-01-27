package fclib;

import java.util.*;

// interface to specify document attributes

public interface DocAttr extends Comparable<DocAttr> {

	// document ID

	String getId();

	// document owner

	String getOwner();

	// timestamp for the document

	Date getTimeStamp();

	// number of days since last modification

	double getAgeDays();

	// text lines of the document

	ArrayList<String> getLines();

	// attributes in string form

	String toString();

	// order relative to another DocAttr

	int compareTo(DocAttr da);

	// equals

	boolean equals(Object obj);
}
