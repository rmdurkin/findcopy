import fclib.*;
import java.util.*;

public class PrepText_01 {

	private static String[][] tab = new String[][]{
		{"",			""},
		{"\r",			""},
		{"\n",			""},
		{"\t",			""},
		{" ",			""},
		{"\014A\na A",		"a\na a"},
		{"abc",			"abc"},
		{" A B  C ",		"a b  c"},
		{"\t \nZzZ\t\n",	"zzz"},
		{"ab\n \nc",		"ab\n \nc"},
	};

	private static void test01() {
		String s = Config.PT.getPrepName();
		assert s != null && s.length() >= 1;

		for (int i = 0; i < tab.length; i++) {
			String s1 = tab[i][0];
			String s2 = Config.PT.prepLine(s1);
			assert s2.equals(tab[i][1]);
		}
	}

	private static void test02() {
		ArrayList<String> list = new ArrayList<String>();

		list = Config.PT.prepList(list);
		assert list.size() == 0;

		list = Config.PT.prepList(list, 0, 0);
		assert list.size() == 0;

		list.add("ABC");
		list.add("DEF");
		list = Config.PT.prepList(list, 0, 1);
		assert list.size() == 1;
		assert list.get(0).equals("abc");

		list.add("DEF");
		list = Config.PT.prepList(list);
		assert list.size() == 2;
		assert list.get(0).equals("abc");
		assert list.get(1).equals("def");

		list.add("  GHI\t\n  ");
		list = Config.PT.prepList(list, 0, 3);
		assert list.size() == 3;
		assert list.get(0).equals("abc");
		assert list.get(1).equals("def");
		assert list.get(2).equals("ghi");

		DocAttr da = new DocAttrRaw("123", "456", "A \n\tb\nC",
			new Date(789));
		WinLoc loc = new WinLoc(da, 0, 2);
		list = Config.PT.prepWinLoc(loc);
		assert list.size() == 2;
		assert list.get(0).equals("a");
		assert list.get(1).equals("b");

		loc = new WinLoc(da, 1, 1);
		list = Config.PT.prepWinLoc(loc);
		assert list.size() == 1;
		assert list.get(0).equals("b");
	}

	public static void main(String[] args) {
		test01();
		test02();
	}
}
