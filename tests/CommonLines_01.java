import fclib.*;
import java.util.*;

public class CommonLines_01 {

	private static Rand rn = new Rand();

	private static void test01() {
		ArrayList<String> list1 = new ArrayList<String>();
		CommonLines cl1 = new CommonLines(list1, list1);
		ArrayList<CommonLinesEntry> clelist1 = cl1.getSequences(1);
		assert clelist1.size() == 0;

		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("a");
		CommonLines cl2 = new CommonLines(list1, list2);
		ArrayList<CommonLinesEntry> clelist2 = cl2.getSequences(1);
		assert clelist2.size() == 0;
	}

	private static void test02() {
		for (int i = 1; i <= 10; i++) {
			ArrayList<String> list = new ArrayList<String>();
			for (int j = 1; j <= i; j++)
				list.add("a");

			CommonLines cl = new CommonLines(list, list);

			ArrayList<CommonLinesEntry> clelist;

			clelist = cl.getSequences(1);
			assert clelist.size() == 1;
			CommonLinesEntry cle = clelist.get(0);
			assert cle.getLen() == i;
			assert cle.getPos1() == 0;
			assert cle.getPos2() == 0;

			clelist = cl.getSequences(i);
			assert clelist.size() == 1;
			cle = clelist.get(0);
			assert cle.getLen() == i;
			assert cle.getPos1() == 0;
			assert cle.getPos2() == 0;

			clelist = cl.getSequences(i + 1);
			assert clelist.size() == 0;
		}
	}

	private static void test03() {
		for (int i = 1; i <= 20; i++) {
			ArrayList<String> list1 = new ArrayList<String>();
			list1.add("a");

			ArrayList<String> list2 = new ArrayList<String>();
			for (int j = 1; j <= i; j++)
				list2.add("a");

			CommonLines cl = new CommonLines(list1, list2);
			ArrayList<CommonLinesEntry> clelist =
				cl.getSequences(1);

			assert clelist.size() == i;

			for (int j = 0; j < i; j++) {
				CommonLinesEntry cle = clelist.get(j);
				assert cle.getLen() == 1;
				assert cle.getPos1() == 0;
				assert cle.getPos2() == j;
			}
		}
	}

	private static void test04() {
		for (int i = 1; i <= 20; i++) {
			ArrayList<String> list1 = new ArrayList<String>();
			ArrayList<String> list2 = new ArrayList<String>();

			for (int j = 1; j <= i; j++) {
				list1.add("a");
				list1.add("x");

				list2.add("a");
				list2.add("y");
			}

			CommonLines cl = new CommonLines(list1, list2);
			ArrayList<CommonLinesEntry> clelist =
				cl.getSequences(1);

			assert clelist.size() == i * i;

			int cnt = 0;

			for (int j = 0; j < i ; j++) {
				for (int k = 0; k < i; k++) {
					CommonLinesEntry cle = clelist.get(cnt);
					cnt++;
					assert cle.getLen() == 1;
					assert cle.getPos1() == j * 2;
					assert cle.getPos2() == k * 2;
				}
			}

			assert cnt == i * i;
		}
	}

	private static void test05() {
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();

		list1.add("a");
		list1.add("a");
		list1.add("a");

		list2.add("a");
		list2.add("a");

		CommonLines cl = new CommonLines(list1, list2);

		ArrayList<CommonLinesEntry> clelist = cl.getSequences(2, true);

		assert clelist.size() == 2;

		CommonLinesEntry cle = clelist.get(0);
		assert cle.getLen() == 2;
		assert cle.getPos1() == 0;
		assert cle.getPos2() == 0;

		cle = clelist.get(1);
		assert cle.getLen() == 2;
		assert cle.getPos1() == 1;
		assert cle.getPos2() == 0;
	}

	private static void test06() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("a");
		list.add("b");
		list.add("c");

		CommonLines cl = new CommonLines(list, list);

		ArrayList<CommonLinesEntry> clelist = cl.getSequences(1, false);

		assert clelist.size() == 6;

		CommonLinesEntry cle;

		cle = clelist.get(0);
		assert cle.getLen() == 3 &&
			cle.getPos1() == 0 && cle.getPos2() == 0;

		cle = clelist.get(1);
		assert cle.getLen() == 2 &&
			cle.getPos1() == 0 && cle.getPos2() == 0;

		cle = clelist.get(2);
		assert cle.getLen() == 2 &&
			cle.getPos1() == 1 && cle.getPos2() == 1;

		cle = clelist.get(3);
		assert cle.getLen() == 1 &&
			cle.getPos1() == 0 && cle.getPos2() == 0;

		cle = clelist.get(4);
		assert cle.getLen() == 1 &&
			cle.getPos1() == 1 && cle.getPos2() == 1;

		cle = clelist.get(5);
		assert cle.getLen() == 1 &&
			cle.getPos1() == 2 && cle.getPos2() == 2;
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
		test04();
		test05();
		test06();
	}
}
