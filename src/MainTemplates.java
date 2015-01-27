import fclib.*;
import java.io.*;
import java.util.*;

// find lines common to random pairs of documents,
// and compile a global template database

public class MainTemplates {

	// number of owners processed during one iteration

	private static final int MAX_WORK_OWNERS = 2;

	// percentage of document pairs processed during one iteration

	private static final double PAIR_PERC = 0.03;

	// number of times CommonLines called,
	// and number of sequences found

	private static int numcl;

	private static int numseq;

	// number of iterations of the main sampling loop

	private static int numiter;

	// random number object

	private static Rand rn = new Rand();

	// the set of all owners

	private static ArrayList<String> owners;
	private static int numowners;

	// the set of work owners for the current iteration

	private static ArrayList<String> workowners;
	private static int numworkowners;

	// the set of work documents for the current iteration,
	// and the owners for each document

	private static ArrayList<ArrayList<String>> workdocs;
	private static ArrayList<String> workdocsowners;
	private static int numworkdocs;

	// pick some random owners to work on

	private static void pickowners() {
		assert MAX_WORK_OWNERS >= 2;
		numworkowners = MAX_WORK_OWNERS;
		if (numworkowners > numowners)
			numworkowners = numowners;	
		assert numowners >= 2 && numworkowners >= 2;

		// shuffle the list of owners and then pick the first few

		Collections.shuffle(owners, rn);

		workowners = new ArrayList<String>(numworkowners);

		for (int i = 0; i < numworkowners; i++)
			workowners.add(owners.get(i));

		// check for duplicate owners

		for (int i = 0; i < numworkowners - 1; i++) {
			String owner1 = workowners.get(i);
			for (int j = i + 1; j < numworkowners; j++) {
				String owner2 = workowners.get(j);
				assert !owner1.equals(owner2);
			}
		}
	}

	// get all the documents for a set of work owners

	private static void getdocs() {
		workdocs = new ArrayList<ArrayList<String>>();
		workdocsowners = new ArrayList<String>();

		// add all the documents for an owner to the document list

		for (String workowner : workowners) {
			ArrayList<DocAttr> doclist =
				Config.DB.getAllDocAttrForOwner(workowner);

			for (DocAttr da : doclist) {
				workdocs.add(Config.PT.prepList(da.getLines()));
				workdocsowners.add(da.getOwner());
			}
		}

		numworkdocs = workdocs.size();

		assert numworkdocs >= numworkowners;
	}

	// local class used to represent pairs of documents

	private static class DocPair {
		int first;
		int second;

		public DocPair(int first, int second) {
			assert first >= 0 && first < numworkdocs;
			assert second >= 0 && second < numworkdocs;

			this.first = first;
			this.second = second;
		}
	}
	
	// extract pairs of documents from the list of documents

	private static void dopairs() {
		assert numworkdocs >= 1;

		ArrayList<DocPair> doclist = new ArrayList<DocPair>();

		// collect all pairs

		for (int i = 0; i < numworkdocs - 1; i++) {
			String owner1 = workdocsowners.get(i);
			for (int j = i + 1; j < numworkdocs; j++) {
				String owner2 = workdocsowners.get(j);

				// if pair of documents with same owner, skip

				if (owner1.equals(owner2))
					continue;

				doclist.add(new DocPair(i, j));
			}
		}

		// shuffle the list of pairs

		Collections.shuffle(doclist, rn);

		// pick a certain percentage of pairs for analysis

		int n = (int)(doclist.size() * PAIR_PERC + 0.5);
		if (n == 0 && doclist.size() >= 1)
			n = 1;

		// analyze pairs of documents

		for (int i = 0; i < n; i++) {
			DocPair dp = doclist.get(i);

			int indx1 = dp.first;
			int indx2 = dp.second;

			String owner1 = workdocsowners.get(indx1);
			String owner2 = workdocsowners.get(indx2);
			assert !owner1.equals(owner2);

			extract(workdocs.get(indx1), workdocs.get(indx2));
		}
	}

	// local class used to hold data associated with a signature

	private static class SigData implements Comparable<SigData> {
		int numhits;
		int len;
		WinSig sig;
		String text;

		// constructor

		public SigData(WinSig sig, int len, String text) {
			assert sig != null && len >= Config.WIN_SIZE_STD;
			assert text != null && text.length() >= 1;

			this.numhits = 0;
			this.sig = sig;
			this.len = len;
			this.text = text;
		}

		// increment the number of hits for a signature

		public void incrHits() {
			numhits++;
		}

		// compare one SigData to another

		public int compareTo(SigData other) {
			assert other != null;

			if (numhits > other.numhits)
				return -1;
			else if (numhits < other.numhits)
				return 1;

			if (len > other.len)
				return -1;
			else if (len < other.len)
				return 1;

			return 0;
		}
	}

	// hash table to map signatures to SigData objects

	private static LinearHash<WinSig, SigData> table =
		new LinearHash<WinSig, SigData>();

	// find line sequences common to two documents

	private static void extract(ArrayList<String> doc1,
	ArrayList<String> doc2) {
		assert doc1 != null && doc2 != null && doc1 != doc2;

		// get the common sequences

		CommonLines cl = new CommonLines(doc1, doc2);
		numcl++;
cl.verifySequences(); // ???

		ArrayList<CommonLinesEntry> seq =
			cl.getSequences(Config.WIN_SIZE_STD);
		int sz = seq.size();
		numseq += sz;

		if (sz == 0)
			return;

		// check each sequence and compute a hash code for the lines

		for (CommonLinesEntry cle : seq) {
			int len = cle.getLen();
			int pos1 = cle.getPos1();
			int pos2 = cle.getPos2();

			StringBuilder sb = new StringBuilder();

			CryptoHash h = new CryptoHash(Constants.HASH_ALG);

			for (int i = 0; i < len; i++) {
				String s1 = doc1.get(pos1 + i);
				String s2 = doc2.get(pos2 + i);

				assert s1.equals(s2);

				h.addLine(s1);

				sb.append(s1);
				sb.append(Constants.EOL);
			}

			String s = sb.toString();

			// add to hash table

			WinSig sig = h.getWinSig();
			assert sig.length() == Constants.HASH_LEN_BYTES;

			SigData sd = table.get(sig);

			if (sd == null) {
				sd = new SigData(sig, len, s);
				table.put(sig, sd);
			}

			// once in hash table, increment the number of hits

			sd.incrHits();
		}
	}

	// write out current statistics

	private static void write() throws IOException {

		// create a list of SigData objects

		ArrayList<SigData> out = new ArrayList<SigData>();

		for (WinSig sig : table)
			out.add(table.get(sig));

		// sort the list

		Collections.sort(out);

		// create output files

		PrintWriter pw_freq =
			UtilFuncs.getPrintWriter(CopyPasteConfig.GT_FREQ);
		PrintWriter pw_full =
			UtilFuncs.getPrintWriter(CopyPasteConfig.GT_FULL);
		PrintWriter pw_stat =
			UtilFuncs.getPrintWriter(CopyPasteConfig.GT_STAT);

		BadSigWriter bsw = new BadSigWriter(CopyPasteConfig.GT_SIGS);

		// write out statistics

		assert numcl >= 1 && numseq >= 1 && numiter >= 1;

		pw_stat.printf("numiter = %d\n\n", numiter);
		pw_stat.printf("numcl = %d\n\n", numcl);
		pw_stat.printf("numseq = %d\n\n", numseq);
		pw_stat.printf("numsig = %d\n\n", table.size());
		pw_stat.close();

		// iterate across all the signatures

		for (SigData sd : out) {
			bsw.add(sd.sig);

			pw_freq.printf("%8d  %3d  %s\n", sd.numhits,
				sd.len, sd.sig);

			pw_full.printf("%8d  %3d  %s\n", sd.numhits,
				sd.len, sd.sig);
			pw_full.printf("%s", sd.text);
		}

		pw_freq.close();
		pw_full.close();

		bsw.write();
	}

	public static void main(String[] args) throws IOException {

		// get argument specifying number of hours to run

		if (args.length != 1) {
			System.err.println("*** missing hour argument ***");
			System.exit(1);
		}

		double hours = Double.parseDouble(args[0]);
		assert hours >= 0.01 && hours <= 100.0;

		// get a list of all owners

		owners = Config.DB.getAllOwnerIds();
		numowners = owners.size();
		assert numowners >= 1;

		// iterate

		numcl = 0;
		numseq = 0;
		numiter = 0;

		long end_time = System.currentTimeMillis() +
			(long)(Constants.MILLISEC_PER_HOUR * hours + 0.5);

		while (System.currentTimeMillis() < end_time) {

			// pick some random owners

			pickowners();

			// get all the documents for these owners

			getdocs();

			// extract templates from the documents

			dopairs();

			numiter++;
		}

		// write out the results

		write();
	}
}
