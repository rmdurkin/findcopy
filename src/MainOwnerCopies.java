import fclib.*;
import java.io.*;
import java.util.*;

// find copy/paste instances for all owners

public class MainOwnerCopies {

	// get the policy object

	private static CopyPastePolicy getpolicy(String[] args)
	throws IOException {

		// get range of days

		String d1;
		String d2;

		if (args.length == 0) {
			d1 = "0";
			d2 = "90";
		}
		else if (args.length == 1) {
			d1 = "0";
			d2 = args[0];
		}
		else {
			d1 = args[0];
			d2 = args[1];
		}

		double days1 = Double.parseDouble(d1);
		double days2 = Double.parseDouble(d2);

		if (days1 < 0.0 || days2 < 0.0 || days1 >= days2) {
			System.err.println("Invalid days range");
			System.exit(1);
		}

		// load bad signature file

		BadSigReader bsr = new BadSigReader(CopyPasteConfig.GT_SIGS);

		// load line frequency database

		LineFreqReader lfr =
			new LineFreqReader(CopyPasteConfig.LINE_FREQ);

		// load word frequency database

		WordFreqReader wfr =
			new WordFreqReader(CopyPasteConfig.WORD_FREQ,
			CopyPasteConfig.ET_LIST);

		// create syntax filter

		SyntaxFilter sfi = new SyntaxFilter();

		// load exclusion list

		ExclusionList el =
			new ExclusionList(CopyPasteConfig.EXCLUSION_LIST);

		// create policy object

		CopyPastePolicy cpp = new CopyPastePolicy();

		cpp.setBadSigReader(bsr);
		cpp.setLineFreqReader(lfr);
		cpp.setWordFreqReader(wfr);
		cpp.setSyntaxFilter(sfi);
		cpp.setExclusionList(el);

		cpp.setRecentDays(days1, days2);

		return cpp;
	}

	public static void main(String[] args)
	throws ClassNotFoundException, IOException {

		// get policy object

		CopyPastePolicy cpp = getpolicy(args);

		// get all owners on work list

		ArrayList<String> owners = Config.DB.getAllOwnerIds(); // ???

		// create output files

		PrintWriter cp_sum =
			UtilFuncs.getPrintWriter(CopyPasteConfig.CP_SUM);

		PrintWriter cp_full =
			UtilFuncs.getPrintWriter(CopyPasteConfig.CP_FULL);

		// create output list of CopyPasteEntry objects

		ArrayList<CopyPasteEntry> cpelist =
			new ArrayList<CopyPasteEntry>();

		// process each owner

		for (String owner : owners) {

			// find the raw set of copies

			FindOwnerCopies foc = new FindOwnerCopies(owner, cpp);

			// filter the copies

			FilterOwnerCopies fioc =
				new FilterOwnerCopies(foc, cpp);

			// display summary output

			cp_sum.printf("%s [ %d %d %d ] %.1f\n",
				owner, fioc.getNumSig(), fioc.getNumBadSig(),
				fioc.getPercBadSig(), fioc.getAvgGoodLoc());

			// accumulate list of copy/paste instances

			for (CopyPasteEntry cpe : fioc)
				cpelist.add(cpe);

//???break;
		}

		// sort the list

		Collections.sort(cpelist);

		// write the list to the output file

String fn = UtilFuncs.createTempFile();
UtilFuncs.objectToFile(cpelist, fn);
System.out.println("output written to serialized file: " + fn);

/* ???
ArrayList<CopyPasteEntry> cpelist2 =
	(ArrayList<CopyPasteEntry>)UtilFuncs.fileToObject("serial.out");

		for (CopyPasteEntry cpe : cpelist) {
			String s =
				UtilFuncs.eolToLineSep(cpe.getFormatted(true));
			cp_full.print(s);
		}
*/

		// close the output files

		cp_sum.close();

		cp_full.close();
	}
}
