
public class ch1 {
	private static final Rand rn = new Rand();

	private static final int N = 1000000;

	private static String getLine() {
		StringBuilder sb = new StringBuilder();

		int n = rn.getRange(1, 100);

		for (int i = 1; i <= n; i++)
			sb.append(rn.getPerc(50) ? 'a' : 'b');

		return sb.toString();
	}

	public static void main(String[] args) {
		for (int i = 1; i <= N; i++) {
			CryptoHash h = new CryptoHash(Config.HASHALG);

			int n = rn.getRange(1, 250);

			for (int j = 1; j <= n; j++)
				h.addString(getLine());

			h.getWinSig();

			if (i % 1000 == 0)
				System.out.println(i);
		}
	}
}
