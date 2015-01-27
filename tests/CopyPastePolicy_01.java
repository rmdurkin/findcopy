import fclib.*;

import java.io.*;

public class CopyPastePolicy_01 {

	private static final String TMPFILE = "tmpfile";

	private static void test01() {
		CopyPastePolicy cpp = new CopyPastePolicy();

		assert cpp.getBadSigReader() == null;

		assert cpp.getSyntaxFilter() == null;

		assert cpp.getLineFreqReader() == null;

		assert cpp.getWordFreqReader() == null;

		assert cpp.getExclusionList() == null;

		assert cpp.getMinRecentDays() == 0;
		assert cpp.getMaxRecentDays() ==
		CopyPasteConfig.DEFAULT_RECENT_DAYS;
	}

	private static void test02() throws IOException {
		CopyPastePolicy cpp = new CopyPastePolicy();

		ExclusionList el = new ExclusionList("zzz");
		cpp.setExclusionList(el);
		assert cpp.getExclusionList() == el;

		SyntaxFilter sf = new SyntaxFilter();
		cpp.setSyntaxFilter(sf);
		assert cpp.getSyntaxFilter() == sf;

		cpp.setRecentDays(CopyPasteConfig.MIN_RECENT_DAYS,
			CopyPasteConfig.MAX_RECENT_DAYS);
		assert cpp.getMinRecentDays() ==
			CopyPasteConfig.MIN_RECENT_DAYS;
		assert cpp.getMaxRecentDays() ==
			CopyPasteConfig.MAX_RECENT_DAYS;
	}

	private static void test03() throws IOException {
		FileLineWriter flw = new FileLineWriter(TMPFILE);

		flw.add(UtilFuncs.getSettings());
		flw.add("1");
		flw.add("1");
		flw.add("1");
		flw.add("the");
		flw.add("1");
		flw.write();

		LineFreqReader lfr = new LineFreqReader(TMPFILE);

		CopyPastePolicy cpp = new CopyPastePolicy();

		cpp.setLineFreqReader(lfr);
		assert cpp.getLineFreqReader() == lfr;

		new File(TMPFILE).delete();
	}

	private static void test04() throws IOException {
		FileLineWriter flw = new FileLineWriter(TMPFILE);

		flw.add(UtilFuncs.getSettings());
		flw.add("1");
		flw.add("1");
		flw.add("1");
		flw.add("the");
		flw.add("1");
		flw.write();

		WordFreqReader wfr =
			new WordFreqReader(TMPFILE, CopyPasteConfig.ET_LIST);

		CopyPastePolicy cpp = new CopyPastePolicy();

		cpp.setWordFreqReader(wfr);
		assert cpp.getWordFreqReader() == wfr;

		new File(TMPFILE).delete();
	}

	private static void test05() throws IOException {
		BadSigWriter bsw = new BadSigWriter(TMPFILE);
		bsw.write();

		BadSigReader bsr = new BadSigReader(TMPFILE);

		CopyPastePolicy cpp = new CopyPastePolicy();

		cpp.setBadSigReader(bsr);
		assert cpp.getBadSigReader() == bsr;

		new File(TMPFILE).delete();
	}

	public static void main(String[] args) throws IOException {
		test01();
		test02();
		test03();
		test04();
		test05();
	}
}
