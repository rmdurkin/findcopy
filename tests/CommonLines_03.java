import fclib.*;
import java.util.*;

public class CommonLines_03 {

	private static Rand rn = new Rand();

	private static ArrayList<String> getList() {
		ArrayList<String> out = new ArrayList<String>();

		int n = rn.getRange(0, 25); // ???

		for (int i = 0; i < n; i++)
			out.add(rn.getPerc(75) ? "a" : "b");

		return out;
	}

	private static final int N = 10000;

	private static void doit(int min, ArrayList<String> s,
	ArrayList<String> t) {
		int m = s.size();
		int n = t.size();

		CommonLines cl = new CommonLines(s, t);
		ArrayList<CommonLinesEntry> clelist =
			cl.getSequences(min, rn.getPerc(50));
		cl.verifySequences();

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				int k = 0;
				while (i + k < m && j + k < n &&
				s.get(i + k).equals(t.get(j + k)))
					k++;

				if (k < min)
					continue;

				boolean found = false;

				for (CommonLinesEntry cle : clelist) {
					int len = cle.getLen();
					int pos1 = cle.getPos1();
					int pos2 = cle.getPos2();
					if (i >= pos1 && i + k <= pos1 + len &&
					j >= pos2 && j + k <= pos2 + len) {
						found = true;
						break;
					}
				}

				assert found;
			}
		}
	}
	
	private static void test01() {
		for (int i = 1; i <= N; i++) {
			ArrayList<String> list1 = getList();
			ArrayList<String> list2 = getList();
			int min = rn.getRange(1, 5);
			doit(min, list1, list2);
		}
	}

	public static void main(String[] args) {
		test01();
	}
}
