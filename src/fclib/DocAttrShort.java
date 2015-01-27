package fclib;

import java.io.*;
import java.util.*;

// implementation of DocAttr for simulated very short documents

public class DocAttrShort extends AbstractDocAttr {
	private static final Rand rn = new Rand();

	// document ID

	private String id;

	// owner

	private String owner;

	// string returned by toString()

	private String outstr;

	// document lines

	private ArrayList<String> list = new ArrayList<String>();

	// fill the document with random lines

	private void fill() {

		// select number of lines for document

		int nl = rn.getRange(0, 25);

		// generate random lines

		for (int i = 1; i <= nl; i++) {
			StringBuilder sb = new StringBuilder();

			// put characters in line

			int nc = rn.getRange(0, 25);

			for (int j = 1; j <= nc; j++)
				sb.append(rn.getRange(0, 1) == 0 ? 'a' : ' ');

			// put on line terminator

			sb.append(Constants.EOL);

			// add line to list

			list.add(sb.toString());
		}

		list.trimToSize();
	}

	// constructor that takes a document ID

	public DocAttrShort(String id) {
		if (id == null || id.length() == 0)
			throw new IllegalArgumentException();

		this.id = id;

		owner = "owner" + rn.getRange(1, 3);

		outstr = id + ":" + owner;

		fill();
	}

	// return the ID

	public String getId() {
		return id;
	}

	// return the owner

	public String getOwner() {
		return owner;
	}

	// get the lines

	public ArrayList<String> getLines() {
		return list;
	}

	// return the pathname

	public String toString() {
		return outstr;
	}
}
