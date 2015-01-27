import fclib.*;

import java.io.*;
import java.util.*;

public class ExclusionList_01 {

	private static final String TMPFILE = "tmpfile";

	private static void test01() throws IOException {
		ExclusionList el = new ExclusionList(TMPFILE);
		DocAttrRaw da = new DocAttrRaw("1", "2", "1\n2\n3\n",
			new Date(4));

		WinLoc loc = new WinLoc(da, 0, 1);
		assert !el.findEntry(loc);
		el.addEntry(loc);
		assert el.findEntry(loc);

		loc = new WinLoc(da, 0, 2);
		assert !el.findEntry(loc);
		el.addEntry(loc);
		assert el.findEntry(loc);

		loc = new WinLoc(da, 0, 3);
		assert !el.findEntry(loc);
		el.addEntry(loc);
		assert el.findEntry(loc);

		el = new ExclusionList(TMPFILE);
		loc = new WinLoc(da, 0, 1);
		assert el.findEntry(loc);
		loc = new WinLoc(da, 0, 2);
		assert el.findEntry(loc);
		loc = new WinLoc(da, 0, 3);
		assert el.findEntry(loc);

		new File(TMPFILE).delete();
	}

	private static final int N = 100;

	private static void test02() throws IOException {
		for (int i = 1; i <= N; i++) {
			ExclusionList el = new ExclusionList(TMPFILE);

			for (int j = 1; j <= i; j++) {
				String s = j + "\n" + (j * j) + "\n";
				DocAttr da = new DocAttrRaw("1", "2", s,
					new Date(3));
				WinLoc loc = new WinLoc(da, 0, 2);
				assert !el.findEntry(loc);
				el.addEntry(loc);
				assert el.findEntry(loc);
			}

			el = new ExclusionList(TMPFILE);

			for (int j = 1; j <= i; j++) {
				String s = j + "\n" + (j * j) + "\n";
				DocAttr da = new DocAttrRaw("1", "2", s,
					new Date(3));
				WinLoc loc = new WinLoc(da, 0, 2);
				assert el.findEntry(loc);
				el.addEntry(loc);
				assert el.findEntry(loc);
			}

			new File(TMPFILE).delete();
		}
	}

	public static void main(String[] args) throws IOException {
		test01();
		test02();
	}
}
