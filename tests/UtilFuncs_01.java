import fclib.*;
import java.io.*;
import java.util.*;

public class UtilFuncs_01 {

	private static final String TMPFILE = "tmpfile";

	private static final double CUTOFF = 1e-3;

/* ??? obsolete
	private static String[][] tab = new String[][]{
		{"",		""},
		{"\n",		""},
		{"\t",		""},
		{" ",		""},
		{"abc",		"abc"},
		{" A B  C ",	"a b  c"},
		{"\t\nZzZ\t\n",	"zzz"},
		{"ab\nc",	"ab\nc"},
	};

	private static void test01() {
		String s = UtilFuncs.getPrepName();
		assert s != null && s.length() >= 1;

		for (int i = 0; i < tab.length; i++) {
			String s1 = tab[i][0];
			String s2 = UtilFuncs.prepLine1(s1);
			assert s2.equals(tab[i][1]);
		}
	}

	private static void test02() {
		ArrayList<String> list = new ArrayList<String>();

		list = UtilFuncs.prepLine1(list);
		assert list.size() == 0;

		list = UtilFuncs.prepLine1(list, 0, 0);
		assert list.size() == 0;

		list.add("ABC");
		list.add("DEF");
		list = UtilFuncs.prepLine1(list, 0, 1);
		assert list.size() == 1;
		assert list.get(0).equals("abc");

		list.add("DEF");
		list = UtilFuncs.prepLine1(list);
		assert list.size() == 2;
		assert list.get(0).equals("abc");
		assert list.get(1).equals("def");

		list.add("  GHI\t\n  ");
		list = UtilFuncs.prepLine1(list, 0, 3);
		assert list.size() == 3;
		assert list.get(0).equals("abc");
		assert list.get(1).equals("def");
		assert list.get(2).equals("ghi");

		DocAttr da = new DocAttrRaw("123", "456", "A \n\tb\nC",
			new Date(789));
		WinLoc loc = new WinLoc(da, 0, 2);
		list = UtilFuncs.prepLine1(loc);
		assert list.size() == 2;
		assert list.get(0).equals("a");
		assert list.get(1).equals("b");
	}
*/

	private static void test03() {
		ArrayList<String> out;

		out = UtilFuncs.stringToLines("", Constants.EOL_CHAR);
		assert out.size() == 0;

		out = UtilFuncs.stringToLines(" ", Constants.EOL_CHAR);
		assert out.size() == 1;
		assert out.get(0).equals(" ");

		out = UtilFuncs.stringToLines("" + Constants.EOL_CHAR,
			Constants.EOL_CHAR);
		assert out.size() == 1;
		assert out.get(0).equals("");

		out = UtilFuncs.stringToLines(" " + Constants.EOL_CHAR,
			Constants.EOL_CHAR);
		assert out.size() == 1;
		assert out.get(0).equals(" ");

		out = UtilFuncs.stringToLines("" + Constants.EOL_CHAR + "",
			Constants.EOL_CHAR);
		assert out.size() == 1;
		assert out.get(0).equals("");

		out = UtilFuncs.stringToLines(" " + Constants.EOL_CHAR + "",
			Constants.EOL_CHAR);
		assert out.size() == 1;
		assert out.get(0).equals(" ");

		out = UtilFuncs.stringToLines("" + Constants.EOL_CHAR + " ",
			Constants.EOL_CHAR);
		assert out.size() == 2;
		assert out.get(0).equals("");
		assert out.get(1).equals(" ");

		out = UtilFuncs.stringToLines(" " + Constants.EOL_CHAR + " ",
			Constants.EOL_CHAR);
		assert out.size() == 2;
		assert out.get(0).equals(" ");
		assert out.get(1).equals(" ");

		out = UtilFuncs.stringToLines(" " + Constants.EOL_CHAR + " " +
			Constants.EOL_CHAR, Constants.EOL_CHAR);
		assert out.size() == 2;
		assert out.get(0).equals(" ");
		assert out.get(1).equals(" ");
	}

	private static final int N = 100000;

	private static void test04() {
		Rand rn = new Rand();

		for (int i = 1; i <= N; i++) {
			int n = rn.getRange(1, 100);
			byte[] b1 = new byte[n];
			for (int j = 0; j < n; j++)
				b1[j] = (byte)rn.getRange(0, 255);

			String s = UtilFuncs.byteToHex(b1);
			String t = new ByteString(b1).toString();
			byte[] b2 = UtilFuncs.hexToByte(s);

			assert s.equals(t);
			assert b1.length >= 1 && b1.length == b2.length;

			for (int j = 0; j < b1.length; j++)
				assert b1[j] == b2[j];
		}
	}

	private static void test05() {
		long ts = System.currentTimeMillis();
		double d = UtilFuncs.getElapsedTime(ts);
		assert d >= 0.0 && d <= CUTOFF;

		long ts2 = System.currentTimeMillis() -
			Constants.MILLISEC_PER_DAY;
		d = UtilFuncs.getElapsedTime(ts2);
		assert d >= 1.0 && d <= 1.0 + CUTOFF;

		Rand rn = new Rand();

		for (int i = 1; i <= 1000; i++) {
			long ts3 = System.currentTimeMillis() -
				rn.getRange(0, 1000000);
			double d1 = UtilFuncs.getElapsedTime(ts3);
			double d2 = UtilFuncs.getElapsedTime(new Date(ts3));
			double d3 = d2 / d1;
			assert d3 >= 1.0 && d3 <= 1.0 + CUTOFF;
		}
	}

	private static void test06() {
		DocAttr da1 = new DocAttrRaw("1", "2", "3\n4\n5\n",
			new Date(6));
		DocAttr da2 = new DocAttrRaw("1", "2", "5\n 4 \n3\n",
			new Date(6));

		WinLoc loc1 = new WinLoc(da1, 0, 1);
		assert UtilFuncs.verifyWindow(loc1, loc1);

		WinLoc loc2 = new WinLoc(da1, 0, 2);
		assert !UtilFuncs.verifyWindow(loc1, loc2);

		loc2 = new WinLoc(da1, 1, 1);
		assert !UtilFuncs.verifyWindow(loc1, loc2);

		loc1 = new WinLoc(da1, 1, 1);
		loc2 = new WinLoc(da2, 1, 1);
		assert UtilFuncs.verifyWindow(loc1, loc2);
	}

	private static void test07() throws IOException {
		PrintWriter pw = UtilFuncs.getPrintWriter(TMPFILE);
		pw.printf("testing\n");
		pw.close();

		for (int i = 0; i <= 100; i++) {
			long mod = System.currentTimeMillis() -
				i * (long)Constants.MILLISEC_PER_DAY;
			new File(TMPFILE).setLastModified(mod);
			double d = UtilFuncs.getFileAgeDays(TMPFILE);
			assert d >= (double)i && d < (double)i + CUTOFF;
		}

		new File(TMPFILE).delete();
	}

	private static void test08() {
		assert UtilFuncs.eolToLineSep("").equals("");

		String s = "" + Constants.EOL_CHAR;
		assert UtilFuncs.eolToLineSep(s).equals(Constants.IO_SEP);

		s = Constants.EOL_CHAR + "zzz" + Constants.EOL_CHAR;
		assert UtilFuncs.eolToLineSep(s).equals(
			Constants.IO_SEP + "zzz" + Constants.IO_SEP);
	}

	private static void test09() {
		ArrayList<String> list;

		list = UtilFuncs.splitLine("");
		assert list.size() == 0;

		list = UtilFuncs.splitLine(" ");
		assert list.size() == 0;

		list = UtilFuncs.splitLine("\n\t \f   ");
		assert list.size() == 0;

		list = UtilFuncs.splitLine("a");
		assert list.size() == 1;
		assert list.get(0).equals("a");

		list = UtilFuncs.splitLine("   a \t");
		assert list.size() == 1;
		assert list.get(0).equals("a");

		list = UtilFuncs.splitLine("\ta \fb CCC\r");
		assert list.size() == 3;
		assert list.get(0).equals("a");
		assert list.get(1).equals("b");
		assert list.get(2).equals("CCC");
	}

	public static void main(String[] args) throws IOException {
/* obsolete
		test01();
		test02();
*/
		test03();
		test04();
		test05();
		test06();
		test07();
		test08();
		test09();
	}
}
