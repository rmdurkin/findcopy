import fclib.*;
import java.util.*;

public class CommonLines_02 {

	private static Rand rn = new Rand();

	private static ArrayList<String> getList() {
		ArrayList<String> out = new ArrayList<String>();

		int n = rn.getRange(0, 25);

		for (int i = 0; i < n; i++)
			out.add(rn.getPerc(75) ? "a" : "b");

		return out;
	}

	private static final int N = 10000;

	private static void test01() {
		for (int i = 1; i <= N; i++) {
			ArrayList<String> list1 = getList();
			ArrayList<String> list2 = getList();

			CommonLines cl1 = new CommonLines(list1, list2);
			CommonLines cl2 = new CommonLines(list2, list1);

			int n = rn.getRange(1, 10);
			boolean b = rn.getPerc(50);

			ArrayList<CommonLinesEntry> clelist1 =
				cl1.getSequences(n, b);
			ArrayList<CommonLinesEntry> clelist2 =
				cl2.getSequences(n, b);

			cl1.verifySequences();
			cl2.verifySequences();

			assert clelist1.size() == clelist2.size();

			for (int j = 0; j < clelist1.size(); j++) {
				CommonLinesEntry cle1 = clelist1.get(j);
				CommonLinesEntry cle2 = clelist2.get(j);
				assert cle1.getLen() == cle2.getLen();
			}

			for (int j = 0; j < clelist1.size() - 1; j++) {
				CommonLinesEntry cle1 = clelist1.get(j);
				CommonLinesEntry cle2 = clelist1.get(j + 1);
				assert !cle1.equals(cle2);
				assert cle1.getLen() >= cle2.getLen();
			}

			for (int j = 0; j < clelist2.size() - 1; j++) {
				CommonLinesEntry cle1 = clelist2.get(j);
				CommonLinesEntry cle2 = clelist2.get(j + 1);
				assert !cle1.equals(cle2);
				assert cle1.getLen() >= cle2.getLen();
			}
		}
	}

	public static void main(String[] args) {
		test01();
	}
}
