import fclib.*;
import java.io.*;
import java.util.*;

public class WordFreq_01 {

	private static final String TMPFILE = "tmpfile";

	private static void test01() throws IOException {
		WordFreq wf = new WordFreq(TMPFILE, CopyPasteConfig.ET_LIST);
		wf.write();

		WordFreqReader wfr =
			new WordFreqReader(TMPFILE, CopyPasteConfig.ET_LIST);

		assert wfr.getWeight("zzz") == 0;

		ArrayList<String> list = new ArrayList<String>();
		assert wfr.getWeight(list) == 0;
		assert wfr.getScaledWeight(list) == 0;

		new File(TMPFILE).delete();
	}

	private static void test02() throws IOException {
		WordFreq wf = new WordFreq(TMPFILE, CopyPasteConfig.ET_LIST);
		ArrayList<String> list1 = new ArrayList<String>();
		wf.addDocument(list1);
		list1.add("");
		wf.addDocument(list1);
		wf.write();

		WordFreqReader wfr =
			new WordFreqReader(TMPFILE, CopyPasteConfig.ET_LIST);

		assert wfr.getWeight("zzz") == 0;

		ArrayList<String> list2 = new ArrayList<String>();
		assert wfr.getWeight(list2) == 0;
		assert wfr.getScaledWeight(list2) == 0;

		new File(TMPFILE).delete();
	}

	private static void test03() throws IOException {
		WordFreq wf = new WordFreq(TMPFILE, CopyPasteConfig.ET_LIST);
		ArrayList<String> list1 = new ArrayList<String>();
		wf.addDocument(list1);
		list1.add("zzz");
		wf.addDocument(list1);
		wf.write();

		WordFreqReader wfr =
			new WordFreqReader(TMPFILE, CopyPasteConfig.ET_LIST);
		assert wfr.getWeight("zzz") > 0;
		ArrayList<String> list2 = new ArrayList<String>();
		assert wfr.getWeight(list2) == wfr.getWeight("zzz");
		assert wfr.getScaledWeight(list2) == 1;

		new File(TMPFILE).delete();
	}

	public static void main(String[] args) throws IOException {
		test01();
		test02();
	}
}
