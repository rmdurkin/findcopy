package fclib;

import java.io.*;
import java.util.*;

// class used to extract line frequencies/weights and write them to a file

public class LineFreq {

	// records in global table to represent line frequencies

	private static class GlobCount implements Comparable<GlobCount> {
		String line;
		int numocc;
		int numdoc;
		double weight;

		// constructor

		public GlobCount(String line) {
			assert line != null && line.length() != 0;
			this.line = line;
			this.numocc = 0;
			this.numdoc = 0;
			this.weight = 0;
		}

		// compare to another GlobCount

		public int compareTo(GlobCount other) {
			assert other != null;

			if (this == other)
				return 0;

			if (numocc > other.numocc)
				return -1;
			else if (numocc < other.numocc)
				return 1;

			if (numdoc > other.numdoc)
				return -1;
			else if (numdoc < other.numdoc)
				return 1;

			return line.compareTo(other.line);
		}
	}

	// records in per-document table to represent line frequencies

	private static class LocCount {
		private String line;
		private int numocc;

		// constructor

		public LocCount(String line) {
			assert line != null && line.length() != 0;
			this.line = line;
			this.numocc = 0;
		}
	}

	// maximum lines allowed across all documents

	private static final int MAXLINES = 2 * 1000 * 1000 * 1000;

	// pathname to write weights to

	private String fn;

	// number of lines

	private int numocc = 0;

	// number of documents

	private int numdoc = 0;

	// global table of line counts

	private LinearHash<String, GlobCount> globtab =
		new LinearHash<String, GlobCount>();

	// per-document table of line counts

	private LinearHash<String, LocCount> doctab;

	// add a line to the lists

	private void doline(String line) {
		assert line != null && line.length() != 0;

		numocc++;
		assert numocc <= MAXLINES;

		LocCount lc = doctab.get(line);

		if (lc == null) {
			lc = new LocCount(line);
			doctab.put(line, lc);
		}

		lc.numocc++;
	}

	// constructor

	public LineFreq(String fn) {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		this.fn = fn;
	}

	// add a document

	public void addDocument(ArrayList<String> list) {
		if (list == null)
			throw new IllegalArgumentException();

		numdoc++;

		// set up a new hash table for this document

		doctab = new LinearHash<String, LocCount>();

		// process each line

		for (String s : list) {
			if (s != null && s.length() != 0)
				doline(s);
		}

		// update the global table

		for (String s : doctab) {
			LocCount lc = doctab.get(s);
			assert lc != null;

			GlobCount gc = globtab.get(lc.line);

			if (gc == null) {
				gc = new GlobCount(lc.line);
				globtab.put(lc.line, gc);
			}

			gc.numocc += lc.numocc;

			gc.numdoc++;
		}
	}

	// write the weights to a file

	public void write() throws IOException {

		// get the records from the global list

		ArrayList<GlobCount> list = new ArrayList<GlobCount>();

		for (String s : globtab) {
			GlobCount gc = globtab.get(s);
			assert gc != null;

			list.add(gc);
		}

		// compute the weights and the average weight

		double sum = 0;
		int cnt = 0;

		for (GlobCount gc : list) {
			assert gc.numocc >= 1;
			assert gc.numocc >= gc.numdoc;
			assert gc.numocc <= numocc;
			assert gc.numdoc >= 1;
			assert gc.numdoc <= numdoc;

			double wt1 = Math.log((double)numocc / gc.numocc + 1);
			double wt2 = Math.log((double)numdoc / gc.numdoc + 1);

			gc.weight = wt1 * wt2;

			sum += gc.weight * gc.numocc;
			cnt += gc.numocc;
		}

		// compute the average weight

		double avgwt = (cnt == 0 ? 0 : sum / cnt);

		// sort the list

		Collections.sort(list);

		// write out the header

		PrintWriter pw = UtilFuncs.getPrintWriter(fn);

		pw.printf("%s\n", UtilFuncs.getSettings());
		pw.printf("%d\n%d\n%.1f\n", numocc, numdoc, avgwt);

		// write out the values

		for (GlobCount gc : list)
			pw.printf("%s\n%.1f\n", gc.line, gc.weight);

		pw.close();
	}
}
