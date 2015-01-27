import fclib.*;
import java.util.*;

public class CommonLines_04 {

	private static Rand rn = new Rand();

	private static final int MAXLINES = 25;

	private static final int N = 25000;

	private static ArrayList<String> getList() {
		ArrayList<String> out = new ArrayList<String>();

		int n = rn.getRange(0, MAXLINES);

		for (int i = 0; i < n; i++)
			out.add(rn.getPerc(75) ? "a" : "b");

		return out;
	}

	private static boolean cover(CommonLinesEntry cpe1,
	CommonLinesEntry cpe2) {
		assert cpe1 != null && cpe2 != null;

		int mini = cpe2.getPos1();
		int maxi = mini + cpe2.getLen() - 1;

		int minj = cpe2.getPos2();
		int maxj = minj + cpe2.getLen() - 1;

		int i = cpe1.getPos1();
		if (i < mini || i + cpe1.getLen() - 1 > maxi)
			return false;

		int j = cpe1.getPos2();
		if (j < minj || j + cpe1.getLen() - 1 > maxj)
			return false;

		return true;
	}

	private static void check(CommonLinesEntry cle,
	ArrayList<CommonLinesEntry> out) {
		assert cle != null && out != null;

		for (int i = 0; i < out.size(); i++) {
			if (cover(cle, out.get(i)))
				return;
		}

		out.add(cle);
	}

	private static void getalt(ArrayList<String> list1,
	ArrayList<String> list2, int curr,
	ArrayList<CommonLinesEntry> out) {
		assert list1 != null && list2 != null;
		assert curr >= 1 && out != null; 

		for (int i = 0; i < list1.size() - curr + 1; i++) {
			for (int j = 0; j < list2.size() - curr + 1; j++) {
				boolean found = true;
				for (int k = 0; k < curr; k++) {
					String s1 = list1.get(i + k);
					String s2 = list2.get(j + k);
					if (!s1.equals(s2)) {
						found = false;
						break;
					}
				}

				if (!found)
					continue;

				CommonLinesEntry cle =
					new CommonLinesEntry(curr, i, j);

				check(cle, out);
			}
		}
	}

	private static ArrayList<CommonLinesEntry> getalt(
	ArrayList<String> list1, ArrayList<String> list2, int min) {
		assert list1 != null && list2 != null && min >= 1;

		ArrayList<CommonLinesEntry> out =
			new ArrayList<CommonLinesEntry>();

		int maxln = Math.max(list1.size(), list2.size());

		for (int i = maxln; i >= min; i--)
			getalt(list1, list2, i, out);

		return out;
	}

	private static void doit(ArrayList<String> list1,
	ArrayList<String> list2, int min) {
		assert list1 != null && list2 != null && min >= 1;

		CommonLines cl = new CommonLines(list1, list2);
		ArrayList<CommonLinesEntry> cle = cl.getSequences(min);

		ArrayList<CommonLinesEntry> alt = getalt(list1, list2, min);

		assert cle.size() == alt.size();

		for (int i = 0; i < cle.size(); i++)
			assert cle.get(i).equals(alt.get(i));
	}

	private static void go() {
		ArrayList<String> list1 = getList();
		ArrayList<String> list2 = getList();
		int min = rn.getRange(1, MAXLINES);

		doit(list1, list2, min);
	}

	private static void test01() {
		for (int i = 1; i <= N; i++)
			go();
	}

	public static void main(String[] args) {
		test01();
	}
}
