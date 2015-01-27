import fclib.*;
import java.io.*;
import java.util.*;

public class LineFreq_01 {

	private static final String TMPFILE = "tmpfile";

	private static void test01() throws IOException {
		LineFreq lf = new LineFreq(TMPFILE);
		lf.write();

		LineFreqReader lfr = new LineFreqReader(TMPFILE);

		assert lfr.getWeight("zzz") == 0;

		ArrayList<String> list = new ArrayList<String>();
		assert lfr.getWeight(list) == 0;
		assert lfr.getScaledWeight(list) == 0;

		new File(TMPFILE).delete();
	}

	private static void test02() throws IOException {
		LineFreq lf = new LineFreq(TMPFILE);
		ArrayList<String> list1 = new ArrayList<String>();
		lf.addDocument(list1);
		list1.add("");
		lf.addDocument(list1);
		lf.write();

		LineFreqReader lfr = new LineFreqReader(TMPFILE);
		assert lfr.getWeight("zzz") == 0;
		ArrayList<String> list2 = new ArrayList<String>();
		assert lfr.getWeight(list2) == 0;
		assert lfr.getScaledWeight(list2) == 0;

		new File(TMPFILE).delete();
	}

	private static void test03() throws IOException {
		LineFreq lf = new LineFreq(TMPFILE);
		ArrayList<String> list1 = new ArrayList<String>();
		lf.addDocument(list1);
		list1.add("zzz");
		lf.addDocument(list1);
		lf.write();

		LineFreqReader lfr = new LineFreqReader(TMPFILE);
		assert lfr.getWeight("zzz") > 0;
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("zzz");
		assert lfr.getWeight(list2) == lfr.getWeight("zzz");
		assert lfr.getScaledWeight(list2) == 1;

		new File(TMPFILE).delete();
	}

	public static void main(String[] args) throws IOException {
		test01();
		test02();
		test03();
	}
}
