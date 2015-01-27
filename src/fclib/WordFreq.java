package fclib;

import java.io.*;
import java.util.*;

// class used to extract word frequencies/weights and write them to a file

public class WordFreq {

	// records in global table to represent word frequencies

	private static class GlobCount implements Comparable<GlobCount> {
		String word;
		int numocc;
		int numdoc;
		double weight;

		// constructor

		public GlobCount(String word) {
			assert word != null && word.length() != 0;
			this.word = word;
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

			return word.compareTo(other.word);
		}
	}

	// records in per-document table to represent word frequencies

	private static class LocCount {
		private String word;
		private int numocc;

		// constructor

		public LocCount(String word) {
			assert word != null && word.length() != 0;
			this.word = word;
			this.numocc = 0;
		}
	}

	// maximum number of words allowed across all documents

	private static final int MAXWORDS = 2 * 1000 * 1000 * 1000;

	// pathname to write weights to

	private String fn;

	// list of parsing objects to strip out words, numbers, etc.

	private ExtractTokens[] etlist;

	// number of words

	private int numocc = 0;

	// number of documents

	private int numdoc = 0;

	// global table of word counts

	private LinearHash<String, GlobCount> globtab =
		new LinearHash<String, GlobCount>();

	// per-document table of word counts

	private LinearHash<String, LocCount> doctab;

	// add a word to the lists

	private void doword(String w) {
		assert w != null && w.length() != 0;

		numocc++;
		assert numocc <= MAXWORDS;

		LocCount lc = doctab.get(w);

		if (lc == null) {
			lc = new LocCount(w);
			doctab.put(w, lc);
		}

		lc.numocc++;
	}

	// constructor

	public WordFreq(String fn, ExtractTokens[] etlist) {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		if (etlist == null || etlist.length == 0)
			throw new IllegalArgumentException();

		this.fn = fn;

		this.etlist = etlist;
	}

	// add a document

	public void addDocument(ArrayList<String> list) {
		if (list == null)
			throw new IllegalArgumentException();

		numdoc++;

		// set up a new table for this document

		doctab = new LinearHash<String, LocCount>();

		// process each line

		for (String s : list) {

			// run each parsing filter for each line

			for (int i = 0; i < etlist.length; i++) {
				ExtractTokens et = etlist[i];
				assert et != null;
				et.setInput(s);
				String w;
				while ((w = et.nextToken()) != null)
					doword(w);
			}
		}

		// update the global table

		for (String s : doctab) {
			LocCount lc = doctab.get(s);
			assert lc != null;

			GlobCount gc = globtab.get(lc.word);

			if (gc == null) {
				gc = new GlobCount(lc.word);
				globtab.put(lc.word, gc);
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
			pw.printf("%s\n%.1f\n", gc.word, gc.weight);

		pw.close();
	}
}
