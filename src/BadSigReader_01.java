import fclib.*;
import java.io.*;
import java.util.*;

public class BadSigReader_01 {

	private static final int N = 1000;

	private static final String TMPFILE = "tmpfile";

	private static void test01() throws IOException {
		Rand rn = new Rand();

		for (int i = 1; i <= N; i++) {
			BadSigWriter bsw = new BadSigWriter(TMPFILE);
			ArrayList<String> list = new ArrayList<String>();

			int n = rn.getRange(0, 100);
			for (int j = 1; j <= n; j++) {
				String s = "testing" + rn.getRange(1, 10000);
				CryptoHash h =
					new CryptoHash(Constants.HASH_ALG);
				h.addLine(s);
				bsw.add(h.getWinSig());
				list.add(s);
			}
			bsw.write();

			BadSigReader bsr = new BadSigReader(TMPFILE);

			for (String s : list) {
				CryptoHash h =
					new CryptoHash(Constants.HASH_ALG);
				h.addLine(s);
				assert bsr.isBadSig(h.getWinSig());
			}
		}

		new File(TMPFILE).delete();
	}

	public static void main(String[] args) throws IOException {
		test01();
	}
}
