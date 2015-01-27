import fclib.*;
import java.util.*;

public class LinearSet_01 {

	private static final int N = 500;

	private static void test01() {
		LinearSet<Integer> ls = new LinearSet<Integer>();

		assert ls.size() == 0;

		assert !ls.containsKey(new Integer(0));
	}

	private static void test02() {
		for (int i = 1; i <= N; i++) {
			LinearSet<Integer> ls = new LinearSet<Integer>();

			for (int j = 1; j <= i; j++) {
				Integer key = new Integer(j);
				ls.put(key);
				assert ls.containsKey(key);
			}

			for (int j = 1; j <= i; j++) {
				Integer key = new Integer(j);
				assert ls.containsKey(key);
			}
		}
	}

	private static void test03() {
		for (int i = 1; i <= N; i++) {
			LinearSet<Integer> ls = new LinearSet<Integer>();

			for (int j = 1; j <= i; j++) {
				Integer key = new Integer(j);
				ls.put(key);
				assert ls.containsKey(key);

				int cnt = 0;
				BitSet bs = new BitSet();

				for (Integer x : ls) {
					cnt++;
					int iv = x.intValue();
					assert !bs.get(iv);
					bs.set(iv);
				}

				assert cnt == j;
			}
		}
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
	}
}
