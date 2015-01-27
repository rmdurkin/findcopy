import fclib.*;
import java.io.*;
import java.util.*;

public class FileLineWriter_01 {

	private static final int N = 1000;

	private static final String TMPFILE = "tmpfile";

	private static void test01() throws IOException {
		Rand rn = new Rand();

		for (int i = 1; i <= N; i++) {
			ArrayList<String> list1 = new ArrayList<String>();
			FileLineWriter flw = new FileLineWriter(TMPFILE, list1);
			int n = rn.getRange(0, 100);
			for (int j = 1; j <= n; j++)
				list1.add("testing" + rn.getRange(1, 1000));
			flw.write();

			FileLineReader flr = new FileLineReader(TMPFILE);
			ArrayList<String> list2 = flr.getList();
			assert flr.size() == flw.size();

			int j = 0;
			for (String s : list2) {
				assert s.equals(flr.get(j));
				assert s.equals(flw.get(j));
				j++;
			}
		}

		new File(TMPFILE).delete();
	}

	public static void main(String[] args) throws IOException {
		test01();
	}
}
