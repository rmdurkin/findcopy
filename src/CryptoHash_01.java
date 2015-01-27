import fclib.*;
import java.security.*;

public class CryptoHash_01 {

	private static void test01() {
		CryptoHash ch1a = new CryptoHash(Constants.HASH_ALG);
		CryptoHash ch1b = new CryptoHash(Constants.HASH_ALG);
		ch1a.addString("a");
		ch1a.addString(Constants.EOL);
		ch1b.addLine("a");
		assert ch1a.getWinSig().equals(ch1b.getWinSig());

		CryptoHash ch2a = new CryptoHash(Constants.HASH_ALG);
		CryptoHash ch2b = new CryptoHash(Constants.HASH_ALG);
		ch2a.addString("abc");
		ch2b.addString("a");
		ch2b.addString("b");
		ch2b.addString("c");
		assert ch2a.getWinSig().equals(ch2b.getWinSig());
	}

	private static final int N = 1000;

	private static void test02() {
		Rand rn = new Rand();

		for (int i = 1; i <= N; i++) {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance(Constants.HASH_ALG);
			}
			catch (NoSuchAlgorithmException e) {
				throw new FindCopyException();
			}

			CryptoHash h = new CryptoHash(Constants.HASH_ALG);

			int n = rn.getRange(1, 2500);

			for (int j = 1; j <= n; j++) {
				int b = rn.getRange(0, 255);
				md.update((byte)0);
				md.update((byte)b);
				h.addString(Character.toString((char)b));
			}

			WinSig ws1 = new ByteString(md.digest());
			WinSig ws2 = h.getWinSig();
			assert ws1.equals(ws2);
		}
	}

	public static void main(String[] args) {
		test01();
		test02();
	}
}
