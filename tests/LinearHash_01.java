import fclib.*;
import java.util.*;

public class LinearHash_01 {

	private static final int N = 500;

	private static void test01() {
		LinearHash<Integer, Integer> lh =
			new LinearHash<Integer, Integer>();

		assert lh.size() == 0;

		assert !lh.containsKey(new Integer(0));
	}

	private static void test02() {
		for (int i = 1; i <= N; i++) {
			LinearHash<Integer, Integer> lh =
				new LinearHash<Integer, Integer>();

			for (int j = 1; j <= i; j++) {
				Integer key = new Integer(j);
				Integer val = new Integer(j * j);
				lh.put(key, val);
				assert lh.containsKey(key);
				assert lh.get(key) == val;
			}

			for (int j = 1; j <= i; j++) {
				Integer key = new Integer(j);
				Integer val = new Integer(j * j);
				assert lh.containsKey(key);
				assert lh.get(key).equals(val);
			}
		}
	}

	private static void test03() {
		for (int i = 1; i <= N; i++) {
			LinearHash<Integer, Integer> lh =
				new LinearHash<Integer, Integer>();

			for (int j = 1; j <= i; j++) {
				Integer key = new Integer(j);
				Integer val = new Integer(j * j);
				lh.put(key, val);
				assert lh.containsKey(key);
				assert lh.get(key) == val;

				int cnt = 0;
				BitSet bs = new BitSet();

				for (Integer x : lh) {
					cnt++;
					int iv = x.intValue();
					assert !bs.get(iv);
					bs.set(iv);
					int y = lh.get(x).intValue();
					assert y == iv * iv;
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
