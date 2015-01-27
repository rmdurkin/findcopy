package fclib;

import java.util.*;

// implementation of DocAttr based on RawDoc

public class DocAttrRaw extends AbstractDocAttr
implements java.io.Serializable {

	// owner ID

	private String ownerid;

	// document ID

	private String docid;

	// timestamp

	private Date timestamp;

	// list of text lines

	private ArrayList<String> lines;

	// constructor

	public DocAttrRaw(String ownerid, String docid, String text, Date ts) {
		if (ownerid == null || ownerid.length() == 0)
			throw new IllegalArgumentException();

		if (docid == null || docid.length() == 0)
			throw new IllegalArgumentException();

		if (text == null)
			throw new IllegalArgumentException();

		if (ts == null)
			throw new IllegalArgumentException();

		this.ownerid = ownerid;

		this.docid = docid;

		this.lines = UtilFuncs.stringToLines(text, Constants.EOL_CHAR);

		this.timestamp = ts;
	}

	// constructor from a RawDoc object

	public DocAttrRaw(String ownerid, RawDoc rd) {
		if (ownerid == null || ownerid.length() == 0)
			throw new IllegalArgumentException();

		if (rd == null)
			throw new IllegalArgumentException();

		this.ownerid = ownerid;

		this.docid = rd.getId();

		this.lines = UtilFuncs.stringToLines(rd.getText(),
			Constants.EOL_CHAR);

		this.timestamp = rd.getTimeStamp();
	}

	// get the owner ID

	public String getOwner() {
		return ownerid;
	}

	// get the document ID

	public String getId() {
		return docid;
	}

	// get the timestamp

	public Date getTimeStamp() {
		return timestamp;
	}

	// get the list of text lines

	public ArrayList<String> getLines() {
		return lines;
	}
}
