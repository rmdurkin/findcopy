import fclib.*;

public class Rand_01 {
	private static final int N = 10000;

	private static void test01() {
		for (int i = 1; i <= N; i++)
			assert Rand.getSeed() <= 0x7fffffff;

		for (int i = 1; i <= N; i++) {
			assert Rand.getSeed(1) <= 1;
			assert Rand.getSeed(10) <= 1023;
		}
	}

	private static void test02() {
		Rand rn = new Rand();

		int locnt = 0;
		int hicnt = 0;

		for (int i = 1; i <= N; i++) {
			int n = rn.getRange(1, 10);
			assert n >= 1 && n <= 10;
			if (n == 1)
				locnt++;
			if (n == 10)
				hicnt++;
		}

		assert locnt >= 1 && hicnt >= 1;
	}

	private static void test03() {
		Rand rn = new Rand();

		int f = 0;
		int t = 0;

		for (int i = 1; i <= N; i++) {
			if (rn.getPerc(50))
				t++;
			else
				f++;
		}

		int cutoff = (int)(N * 0.40 + 0.5);

		assert t >= cutoff;
		assert f >= cutoff;
	}

	private static void test04() {
		Rand rn = new Rand();

		int f = 0;
		int t = 0;

		for (int i = 1; i <= N; i++) {
			if (rn.getPerc(0.5))
				t++;
			else
				f++;
		}

		int cutoff = (int)(N * 0.40 + 0.5);

		assert t >= cutoff;
		assert f >= cutoff;
	}

	private static void test05() {
		for (int i = 1; i <= N; i++) {
			int n1 = new Rand(i).getRange(1, 10);
			int n2 = new Rand(i).getRange(1, 10);
			assert n1 == n2;
		}
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
		test04();
		test05();
	}
}
