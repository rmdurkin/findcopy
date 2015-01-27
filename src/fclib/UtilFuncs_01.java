package fclib;
import fclib.*;
import java.util.*;

public class UtilFuncs_01 {

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

	private static void test03() {
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

	private static void test04() {
		final double CUTOFF = 1e-4;
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

	public static void main(String[] args) {
		test01();
		test02();
		test03();
		test04();
	}
}
