package fclib;

import java.util.*;

// information for one copy/paste instance

public class CopyPasteEntry implements
Comparable<CopyPasteEntry>, java.io.Serializable {

	// signature

	private WinSig sig;

	// number of locations

	private int numloc;

	// list of locations

	private ArrayList<WinLoc> winloclist;

	// number of days between oldest and newest instances

	private double elapseddays;

	// true if in bad signature database

	private boolean bs;

	// true if problems with basic document syntax (e.g. all whitespace)

	private boolean sf;

	// true if on exclusion list

	private boolean elf;

	// true if should be added to exclusion list

	private boolean addexcl;

	// amount of information content in the text window,
	// relative to other documents, with 1.0 as average

	private double line_info;

	private double word_info;

	// CopyPasteScore for this instance

	private CopyPasteScore cps;

	// constructor

	public CopyPasteEntry(WinSig sig, ArrayList<WinLoc> winloclist,
	boolean bs, boolean sf, boolean elf,
	double line_info, double word_info,
	CopyPasteScore cps, boolean addexcl) {
		if (sig == null || winloclist == null || winloclist.size() == 0)
			throw new IllegalArgumentException();
		if (line_info < 0 && line_info != Constants.BAD_WEIGHT)
			throw new IllegalArgumentException();
		if (word_info < 0 && word_info != Constants.BAD_WEIGHT)
			throw new IllegalArgumentException();
		if (cps == null)
			throw new IllegalArgumentException();

		this.sig = sig;

		this.winloclist = winloclist;

		this.numloc = winloclist.size();

		this.bs = bs;

		this.sf = sf;

		this.elf = elf;

		this.line_info = line_info;
		this.word_info = word_info;

		this.cps = cps;

		this.addexcl = addexcl;

		// compute the number of days between oldest and newest WinLocs

		WinLoc first = winloclist.get(0);
		WinLoc last = winloclist.get(winloclist.size() - 1);
		long elapsed = last.getDocAttr().getTimeStamp().getTime() -
			first.getDocAttr().getTimeStamp().getTime();
		assert elapsed >= 0;
		this.elapseddays = elapsed / (double)Constants.MILLISEC_PER_DAY;
	}

	// get the signature

	public WinSig getWinSig() {
		return sig;
	}

	// get the number of locations

	public int getNumLoc() {
		return numloc;
	}

	// get the locations

	public ArrayList<WinLoc> getWinLocs() {
		return winloclist;
	}

	// get the number of days between oldest and newest

	public double getElapsedDays() {
		return elapseddays;
	}

	// get the information content

	public double getLineInfo() {
		return line_info;
	}

	public double getWordInfo() {
		return word_info;
	}

	public double getInfo() {
		return (getLineInfo() + getWordInfo()) / 2.0;
	}

	// get the bad signature setting

	public boolean isBadSig() {
		return bs;
	}

	// get the syntax filter setting

	public boolean isBadSyn() {
		return sf;
	}

	// get the exclusion list setting

	public boolean isBadExcl() {
		return elf;
	}

	// true if any isBadX() returns true

	public boolean isBadAny() {
		return isBadSig() || isBadSyn() || isBadExcl();
	}

	// get a list of actual text lines for the copy/paste instance

	public ArrayList<String> getText() {
		return Config.PT.prepWinLoc(getWinLoc(0));
	}

	// get the owner

	public String getOwner() {
		return getWinLoc(0).getDocAttr().getOwner();
	}

	// get a WinLoc

	public WinLoc getWinLoc(int i) {
		if (i < 0 || i >= winloclist.size())
			throw new IllegalArgumentException();

		return winloclist.get(i);
	}

	// get the document ID for an entry

	public String getId(int i) {
		if (i < 0 || i >= winloclist.size())
			throw new IllegalArgumentException();

		return getWinLoc(i).getDocAttr().getId();
	}

	// get the score for an entry

	public CopyPasteScore getScore() {
		return cps;
	}

	// get the add-to-exclusion list flag

	public boolean getAddExcl() {
		return addexcl;
	}

	// set the add-to-exclusion list flag

	public void setAddExcl(boolean b) {
		addexcl = b;
	}

	// compare to another CopyPasteEntry

	public int compareTo(CopyPasteEntry other) {
		if (other == null)
			throw new IllegalArgumentException();

		if (this == other)
			return 0;

		// use exclusion as first sort key

		if (isBadAny() != other.isBadAny())
			return isBadAny() == false ? -1 : 1;

		// use information content as second sort key

		if (getInfo() > other.getInfo())
			return -1;
		else if (getInfo() < other.getInfo())
			return 1;

		// use number of WinLocs as third sort key

		if (numloc > other.numloc)
			return -1;
		else if (numloc < other.numloc)
			return 1;

		return 0;
	}

	// convert to a string

	public String getFormatted(boolean showtext) {
		final char SEP = Constants.EOL_CHAR;

		StringBuilder sb = new StringBuilder();

		// owner ID

		sb.append("Owner ID: ");
		sb.append(getOwner());
		sb.append(SEP);

		// document IDs and windows

		sb.append("Document IDs and windows: ");

		sb.append(getId(0));
		sb.append(' ');
		WinLoc loc0 = getWinLoc(0);
		sb.append(loc0.getStartln());
		sb.append(' ');
		sb.append(loc0.getNumln());
		sb.append(' ');

		sb.append(getId(1));
		sb.append(' ');
		WinLoc loc1 = getWinLoc(1);
		assert !loc0.equals(loc1);
		sb.append(loc1.getStartln());
		sb.append(' ');
		sb.append(loc1.getNumln());
		sb.append(SEP);

		// exclusion flags

		sb.append("Overall exclusion: ");
		sb.append(isBadAny());
		sb.append(SEP);
		sb.append("Syntax filter: ");
		sb.append(isBadSyn());
		sb.append(SEP);
		sb.append("Exclusion list: ");
		sb.append(isBadExcl());
		sb.append(SEP);
		sb.append("Bad signature: ");
		sb.append(isBadSyn());
		sb.append(SEP);

		// information content

		sb.append("Average information: ");
		sb.append(String.format("%.2f", getInfo()));
		sb.append(SEP);
		sb.append("Line information: ");
		sb.append(String.format("%.2f", getLineInfo()));
		sb.append(SEP);
		sb.append("Word information: ");
		sb.append(String.format("%.2f", getWordInfo()));
		sb.append(SEP);

		// number of documents

		sb.append("Number of documents: ");
		sb.append(getNumLoc());
		sb.append(SEP);

		// elapsed days

		sb.append("Elapsed days: ");
		sb.append(String.format("%.2f", getElapsedDays()));
		sb.append(SEP);

		// score

		sb.append("Score: ");
		sb.append(cps);
		sb.append(SEP);

		// add to exclusion list flag

		sb.append("Addexcl: ");
		sb.append(addexcl);
		sb.append(SEP);

		// text window

		if (showtext) {
			sb.append("-----");
			sb.append(SEP);
			ArrayList<String> list = getText();
			for (String s : list) {
				sb.append(s);
				sb.append(SEP);
			}
			sb.append("-----");
			sb.append(SEP);
		}

		return sb.toString();
	}

	// get summary information formatted in a single line

	public String getSummary() {
		String fmt = "[%s:%s excl=%c info=%.2f locs=%d " +
			"elap=%d score=%s addexcl=%c]";
		return String.format(fmt,
			getOwner(), getId(0), isBadAny() ? 'T' : 'F',
			getInfo(), getNumLoc(), (int)(getElapsedDays() + 0.5),
			getScore(), getAddExcl() ? 'T' : 'F');
	}
}
