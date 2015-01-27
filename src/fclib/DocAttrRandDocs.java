package fclib;

import java.io.*;
import java.util.*;

// read a random document from a file

public class DocAttrRandDocs extends AbstractDocAttr {

	// list of lines

	private ArrayList<String> list;

	// document ID

	private String id;

	// document owner

	private String owner;

	// output string for toString()

	private String outstr;

	// constructor from a pathname

	public DocAttrRandDocs(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		// read the lines from the random document

		list = new FileLineReader(fn).getList();

		if (list.size() < 3)
			throw new FindCopyException("DocAttrRandDocs");

		// get the id and owner

		id = list.get(0);

		owner = list.get(1);

		// compute the output string

		outstr = id + ":" + owner;
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

	// return the id + owner

	public String toString() {
		return outstr;
	}
}
