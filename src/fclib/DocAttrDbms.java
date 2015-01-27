package fclib;

import java.io.*;
import java.util.*;

// implementation of DocAttr for a DBMS

public class DocAttrDbms extends AbstractDocAttr {

	// owner

	private String ownerid;

	// ID

	private String docid;

	// timestamp

	private static final Date UNKNOWN_TIMESTAMP = new Date(-1L);

	private Date timestamp;

	// true if the text lines of documents should be cached

	private boolean cachedocs;

	// the lines of the document, null if not cached

	private ArrayList<String> lines;

	// constructor that takes owner and document IDs,
	// along with a boolean that designates whether
	// the text lines of documents should be cached

	public DocAttrDbms(String ownerid, String docid, boolean cachedocs) {
		if (ownerid == null || ownerid.length() == 0)
			throw new IllegalArgumentException();

		if (docid == null || docid.length() == 0)
			throw new IllegalArgumentException();

		this.ownerid = ownerid;

		this.docid = docid;

		timestamp = UNKNOWN_TIMESTAMP;

		this.cachedocs = cachedocs;

		lines = null;
	}

	// constructor that forces cachedocs to false

	public DocAttrDbms(String ownerid, String docid) {
		this(ownerid, docid, false);
	}

	// return the owner

	public String getOwner() {
		return ownerid;
	}

	// return the document ID

	public String getId() {
		return docid;
	}

	// return the timestamp

	public Date getTimeStamp() {
		if (timestamp == UNKNOWN_TIMESTAMP)
			timestamp = Config.DB.getTimeStamp(docid);

		return timestamp;
	}

	// get the document lines from the database

	public ArrayList<String> getLines() {
		if (cachedocs) {
			if (lines == null)
				lines = Config.DB.getLines(docid);
			return lines;
		}
		else {
			return Config.DB.getLines(docid);
		}
	}
}
