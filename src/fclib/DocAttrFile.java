package fclib;

import java.io.*;
import java.util.*;

// implementation of DocAttr for disk files

public class DocAttrFile extends AbstractDocAttr {

	// filename

	private String fn;

	// owner

	private String owner;

	// constructor that takes a pathname

	public DocAttrFile(String fn) {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		this.fn = fn;

		// use the file basename for the owner

		int len = fn.length();
		int indx = fn.lastIndexOf('/');
		if (indx <= 0 || indx >= len - 1)
			indx = fn.lastIndexOf('\\');
		if (indx <= 0 || indx >= len - 1)
			owner = fn;
		else
			owner = fn.substring(indx + 1);
	}

	// return the pathname for the ID

	public String getId() {
		return fn;
	}

	// return the owner

	public String getOwner() {
		return owner;
	}

	// get the lines from the file

	public ArrayList<String> getLines() {
		try {
			return new FileLineReader(fn).getList();
		}
		catch (IOException e) {
			throw new FindCopyException("getLines");
		}
	}

	// convert to a string

	public String toString() {
		return getId();
	}
}
