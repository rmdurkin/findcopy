package fclib;

import java.util.*;

// a raw document returned from the database

public class RawDoc {

	// document ID

	private String docid;

	// sequence of text lines delimited by Constants.EOL_CHAR

	private String text;

	// timestamp for the document

	private Date timestamp;

	// constructor

	public RawDoc(String docid, String text, Date timestamp) {
		if (docid == null || docid.length() == 0)
			throw new IllegalArgumentException();

		if (text == null)
			throw new IllegalArgumentException();

		if (timestamp == null)
			throw new IllegalArgumentException();

		this.docid = docid;

		this.text = text;

		this.timestamp = timestamp;
	}

	// get the document ID

	public String getId() {
		return docid;
	}

	// get the text of the document

	public String getText() {
		return text;
	}

	// get the timestamp of the document

	public Date getTimeStamp() {
		return timestamp;
	}
}
