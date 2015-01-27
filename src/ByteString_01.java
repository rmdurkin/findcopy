import fclib.*;

public class ByteString_01 {

	private static Rand rn = new Rand();

	private static final int N = 10000;

	private static void test01() {
		for (int i = 1; i <= N; i++) {
			int n = rn.getRange(0, 100);
			byte[] b1 = new byte[n];
			for (int j = 0; j < n; j++)
				b1[j] = (byte)rn.getRange(0, 255);

			ByteString bs = new ByteString(b1);

			assert bs.length() == n;

			byte[] b2 = bs.getBytes();

			assert bs.equals(new ByteString(b2));

			for (int j = 0; j < b1.length; j++)
				assert b1[j] == b2[j];
		}
	}

	private static void test02() {
		byte[] b = new byte[]{0};
		ByteString b1 = new ByteString(b);
		assert b1.length() == 1;
		assert !b1.equals(null);
		assert !b1.equals(new Integer(0));
	}

	private static void test03() {
		byte[] b1 = new byte[]{};
		ByteString bs1 = new ByteString(b1);
		assert bs1.length() == 0;
		assert bs1.hashCode() == 0;

		byte[] b2 = new byte[]{0};
		ByteString bs2 = new ByteString(b2);
		assert bs2.length() == 1;
		assert bs2.hashCode() == 0;

		byte[] b3 = new byte[]{0, 1};
		ByteString bs3 = new ByteString(b3);
		assert bs3.length() == 2;
		assert bs3.hashCode() == 1;

		byte[] b4 = new byte[]{1, 2};
		ByteString bs4 = new ByteString(b4);
		assert bs4.hashCode() == 33;
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
	}
}
