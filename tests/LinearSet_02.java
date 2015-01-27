import fclib.*;
import java.util.*;

public class LinearSet_02 {

	private static final int N = 1000;

	private static void test01() {
		final int MAX = 1000000;

		int seed = Rand.getSeed(16);

		for (int i = 1; i <= N; i++) {
			Rand rn = new Rand(seed + i);
			LinearSet<String> ls = new LinearSet<String>();
			int numiter = rn.getRange(1, 5000);
			for (int j = 1; j <= numiter; j++) {
				int n = rn.getRange(1, MAX);
				String s = "" + n;
				ls.put(s);
			}

			rn = new Rand(seed + i);
			numiter = rn.getRange(1, 5000);
			for (int j = 1; j <= numiter; j++) {
				int n = rn.getRange(1, MAX);
				String s = "" + n;
				assert ls.containsKey(s);
			}
		}
	}

	public static void main(String[] args) {
		test01();
	}
}
