import fclib.*;
import java.util.*;

public class LinearHash_02 {

	private static final int N = 1000;

	private static void test01() {
		final int MAX = 1000000;

		int seed = Rand.getSeed(16);

		for (int i = 1; i <= N; i++) {
			Rand rn = new Rand(seed + i);
			LinearHash<String, String> ls =
				new LinearHash<String, String>();
			int numiter = rn.getRange(1, 5000);
			for (int j = 1; j <= numiter; j++) {
				int n = rn.getRange(1, MAX);
				String s = "" + n;
				String t = "" + n + 1;
				ls.put(s, t);
			}

			rn = new Rand(seed + i);
			numiter = rn.getRange(1, 5000);
			for (int j = 1; j <= numiter; j++) {
				int n = rn.getRange(1, MAX);
				String s = "" + n;
				String t = "" + n + 1;
				assert ls.containsKey(s);
				assert ls.get(s).equals(t);
			}
		}
	}

	public static void main(String[] args) {
		test01();
	}
}
