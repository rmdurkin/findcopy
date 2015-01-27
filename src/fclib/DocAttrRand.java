package fclib;

import java.io.*;
import java.util.*;

// implementation of DocAttr for simulated documents

public class DocAttrRand extends AbstractDocAttr {

	// document properties

	private static final int MINLINES = 25;
	private static final int MAXLINES = 200;

	private static final int MINWIN = 5;
	private static final int MAXWIN = 15;

	private static final int MAXOWNER = 20;

	private static final int MAXGLOBCOPY = 5;
	private static final int MAXOWNERCOPY = 25;

	private static final int MAXRAND = 20;

	// random object

	private static final Rand rn = new Rand();

	// document ID

	private String id;

	// owner number and string

	private int ownernum;
	private String owner;

	// string returned by toString()

	private String outstr;

	// list of lines for the document

	private ArrayList<String> list = new ArrayList<String>(MAXLINES * 2);

	// fill the document with random lines

	private void fill() {

		// generate the number of lines for the document

		int numleft = rn.getRange(MINLINES, MAXLINES);

		while (numleft > 0) {

			int n = rn.getRange(1, 20);

			// add global artifact

			if (n >= 1 && n <= 2) {
				int num = rn.getRange(1, MAXGLOBCOPY);
				int nl = rn.getRange(MINWIN, MAXWIN);
				for (int i = 1; i <= nl; i++) {
					String s = "global:" + num + ":" + i;
					list.add(s);
					numleft--;
				}
			}

			// add possible owner copy

			else if (n == 3) {
				int num = rn.getRange(1, MAXOWNERCOPY);
				int nl = rn.getRange(MINWIN, MAXWIN);
				for (int i = 1; i <= nl; i++) {
					String s = "owner:" + ownernum +
					":" + num + ":" + i;
					list.add(s);
					numleft--;
				}
			}

			// add random line

			else {
				String s = "random:" + rn.getRange(1, MAXRAND);
				list.add(s);
				numleft--;
			}
		}

		list.trimToSize();
	}

	// constructor that takes a document ID

	public DocAttrRand(String id) {
		if (id == null || id.length() == 0)
			throw new IllegalArgumentException();

		this.id = id;

		ownernum = rn.getRange(1, MAXOWNER);
		owner = "owner" + ownernum;

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
