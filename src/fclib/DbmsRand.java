package fclib;

import java.util.*;

// interface to a database based on random documents

public class DbmsRand extends AbstractDbmsInt {

	// time base

	private static final long TIME_BASE = System.currentTimeMillis();

	// random number object

	private Rand rn;

	// base for document IDs

	private int docbase;

	// number of distinct owners in database

	private int numowners;

	// total number of documents in database

	private int numdocs;

	// information on documents - [i][0] is owner number,
	// [i][1] is timestamp, and document number is implicit
	// offset from a base and is used to index into the info array

	private int[][] info;

	// number of global templates used by documents

	private int numtemplates;

	// number of lines for each global template

	private int[] numtemplatelines;

	// number of owner templates (copy/paste instances) for each owner

	private int[] numownertemplates;

	// size of each template for each owner

	private int[][] ownertemplateinfo;

	// set up a list of owners and documents

	private void initgen() {

		// see if a random seed exists

		String rs = System.getenv("RANDSEED");

		// set up a random number object

		rn = (rs == null ? new Rand() : new Rand(Integer.parseInt(rs)));

		// record base for document IDs

		docbase = (rs == null ? Rand.getSeed(16) : 0);

		// figure out how many owners will be in database

		numowners = rn.getRange(DbmsConfig.MINOWNERS,
			DbmsConfig.MAXOWNERS);

		// figure out how many documents for each owner

		numdocs = 0;
		int numdocsowner[] = new int[numowners];

		for (int i = 0; i < numowners; i++) {
			int t = rn.getRange(DbmsConfig.MINDOCS,
				DbmsConfig.MAXDOCS);
			numdocs += t;
			numdocsowner[i] = t;
		}

		assert numdocs >= DbmsConfig.MINOWNERS * DbmsConfig.MINDOCS;

		// allocate an array for the documents

		info = new int[numdocs][2];

		// fill in the information

		int k = 0;
		for (int i = 0; i < numowners; i++) {
			for (int j = 0; j < numdocsowner[i]; j++) {
				info[k][0] = i;
				info[k][1] = rn.getRange(1,
					DbmsConfig.MAX_TS_DAYS);
				k++;
			}
		}

		assert k == numdocs;
	}

	// initialize for generating random documents

	private void initfill() {
		assert numowners >= 1 && numdocs >= 1;

		// calculate number of global templates in the database

		numtemplates = rn.getRange(DbmsConfig.MINTEMPLATES,
			DbmsConfig.MAXTEMPLATES);

		// calculate the size of each global template

		numtemplatelines = new int[numtemplates];

		for (int i = 0; i < numtemplates; i++)
			numtemplatelines[i] = rn.getRange(
				DbmsConfig.MINTEMPLATELINES,
				DbmsConfig.MAXTEMPLATELINES);

		// calculate number of templates for each owner;
		// these are the raw copy/paste instances

		numownertemplates = new int[numowners];

		for (int i = 0; i < numowners; i++)
			numownertemplates[i] = rn.getRange(
				DbmsConfig.MINOWNERTEMPLATES,
				DbmsConfig.MAXOWNERTEMPLATES);

		// calculate the size of each template for each owner

		ownertemplateinfo = new int[numowners]
			[DbmsConfig.MAXOWNERTEMPLATES];

		for (int i = 0; i < numowners; i++)
			for (int j = 0; j < DbmsConfig.MAXOWNERTEMPLATES; j++)
				ownertemplateinfo[i][j] = rn.getRange(
					DbmsConfig.MINOWNERTEMPLATELINES,
					DbmsConfig.MAXOWNERTEMPLATELINES);
	}

	// local class whose instances are work items
	// used by the fill() method

	static class FillEntry {
		// 't' for global templates
		// 'o' for owner templates
		// 'r' for random lines

		char type;

		// arbitrary value

		int val;

		FillEntry(char type, int val) {
			assert type == 't' || type == 'o' || type == 'r';

			this.type = type;

			this.val = val;
		}
	}

	// generate a random document

	private void fill(ArrayList<String> out, int docnum) {
		assert out != null && out.size() == 0;
		assert docnum >= 0 && docnum < numdocs;

		// get the owner for this document

		int owner = info[docnum][0];

		// get a random number seed and set
		// up a random number generator

		int seed = docbase + docnum;

		Rand fillrn = new Rand(seed);

		// set up a list of items to be added to the document

		ArrayList<FillEntry> work = new ArrayList<FillEntry>();

		// figure out which global templates will be used

		for (int i = 0; i < numtemplates; i++) {
			if (fillrn.getPerc(DbmsConfig.TEMPLATEPERC)) {
				FillEntry fe = new FillEntry('t', i);
				work.add(fe);
			}
		}

		// figure out which owner templates will be used

		for (int i = 0; i < numownertemplates[owner]; i++) {
			if (fillrn.getPerc(DbmsConfig.OWNERTEMPLATEPERC)) {
				FillEntry fe = new FillEntry('o', i);
				work.add(fe);
			}
		}

		// figure how how many random lines will be inserted

		int nr = fillrn.getRange(DbmsConfig.MINRANDOMLINES,
			DbmsConfig.MAXRANDOMLINES);

		for (int i = 0; i < nr; i++) {
			int r = fillrn.getRange(0, DbmsConfig.NUMRANDLINES - 1);
			FillEntry fe = new FillEntry('r', r);
			work.add(fe);
		}

		// shuffle the work list

		assert work.size() >= DbmsConfig.MINRANDOMLINES;

		Collections.shuffle(work, new Random(seed));

		// actually generate the document

		for (int i = 0; i < work.size(); i++) {
			FillEntry fe = work.get(i);

			// global templates

			if (fe.type == 't') {
				int which = fe.val;
				assert which >= 0;
				int nl = numtemplatelines[which];
				assert nl >= 1;
				for (int j = 0; j < nl; j++) {
					String s = "gt:" + which + ":" + j;
					out.add(s);
				}
			}

			// owner templates

			else if (fe.type == 'o') {
				int which = fe.val;
				assert which >= 0;
				int nl = ownertemplateinfo[owner][which];
				assert nl >= 1;
				for (int j = 0; j < nl; j++) {
					String s = "cp:" + owner + ":" +
						which + ":" + j;
					out.add(s);
				}
			}

			// random lines

			else if (fe.type == 'r') {
				int which = fe.val;
				assert which >= 0;
				String s = "rnd:" + which;
				out.add(s);
			}

			else {
				assert false;
			}

			// add some blank lines

			if (fillrn.getPerc(1)) {
				int sz = Config.WIN_SIZE_STD;
				int nb = fillrn.getRange(sz - 1, sz + 1);
				for (int j = 1; j <= nb; j++)
					out.add("");
			}
		}

		out.trimToSize();

		assert out.size() >= DbmsConfig.MINRANDOMLINES;
	}

	// convert a string to a document number

	private int stringToDocNum(String s) {
		if (s == null || s.length() == 0)
			throw new IllegalArgumentException();

		int docnum = Integer.parseInt(s) - docbase;

		assert docnum >= 0 && docnum < numdocs;

		return docnum;
	}

	// convert a document number to a string

	private String docNumToString(int docnum) {
		if (docnum < 0 || docnum >= numdocs)
			throw new IllegalArgumentException();

		return Integer.toString(docbase + docnum);
	}

	// convert a string to an owner number

	private int stringToOwnerNum(String s) {
		if (s == null || s.length() == 0)
			throw new IllegalArgumentException();

		int ownernum = Integer.parseInt(s);

		assert ownernum >= 0 && ownernum < numowners;

		return ownernum;
	}

	// convert an owner number to a string

	private String ownerNumToString(int ownernum) {
		if (ownernum < 0 || ownernum >= numowners)
			throw new IllegalArgumentException();

		return Integer.toString(ownernum);
	}

	// generate a timestamp

	private Date tsToDate(int docnum) {
		if (docnum < 0 || docnum >= numdocs)
			throw new IllegalArgumentException();

		long ms = (long)info[docnum][1] * Constants.MILLISEC_PER_DAY;

		return new Date(TIME_BASE - ms);
	}

	// constructor

	public DbmsRand() {

		// initialize

		initgen();

		// initialize fill method

		initfill();
	}

	// get a work list of owner IDs whose documents
	// need to be analyzed for copy/paste instances

	public ArrayList<String> getWorkOwnerIds() {
		ArrayList<String> out = new ArrayList<String>();

		out.add(ownerNumToString(0)); // ???

		return out;
	}

	// get a large list of random owner IDs for purposes
	// of analyzing a random sample of documents

	public ArrayList<String> getAllOwnerIds() {

		// return the whole list of owners

		ArrayList<String> out = new ArrayList<String>(numowners);

		for (int i = 0; i < numowners; i++)
			out.add(ownerNumToString(i));

		return out;
	}

	// get a list of document IDs for a specific owner

	public ArrayList<String> getDocumentIds(String ownerid) {
		int owner = stringToOwnerNum(ownerid);

		ArrayList<String> out = new ArrayList<String>();

		// make a list of all the documents

		for (int i = 0; i < numdocs; i++) {
			if (info[i][0] == owner)
				out.add(docNumToString(i));
		}

		out.trimToSize();

		return out;
	}

	// get the owner for a specific document

	public String getOwner(String docid) {
		int docnum = stringToDocNum(docid);

		return ownerNumToString(info[docnum][0]);
	}

	// get the text of a specific document

	public ArrayList<String> getLines(String docid) {
		int docnum = stringToDocNum(docid);

		// generate the document

		ArrayList<String> out = new ArrayList<String>();

		fill(out, docnum);

		return out;
	}

	// get a timestamp for a specific document

	public Date getTimeStamp(String docid) {
		int docnum = stringToDocNum(docid);

		return tsToDate(docnum);
	}

	// get all the documents for an owner

	public ArrayList<RawDoc> getAllForOwner(String ownerid) {
		int owner = stringToOwnerNum(ownerid);

		ArrayList<RawDoc> out = new ArrayList<RawDoc>();

		for (int i = 0; i < numdocs; i++) {
			if (info[i][0] == owner) {
				String docid = docNumToString(i);

				Date ts = tsToDate(i);

				ArrayList<String> tmp = new ArrayList<String>();
				fill(tmp, i);
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < tmp.size(); j++) {
					sb.append(tmp.get(j));
					if (j + 1 < tmp.size())
						sb.append(Constants.EOL_CHAR);
				}
				String text = sb.toString();

				RawDoc rd = new RawDoc(docid, text, ts);
				out.add(rd);
			}
		}

		out.trimToSize();

		return out;
	}
}
