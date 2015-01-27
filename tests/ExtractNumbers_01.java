import fclib.*;

public class ExtractNumbers_01 {

	private static void test01() {
		ExtractTokens et = new ExtractNumbers();

		assert et.nextToken() == null;
		assert et.nextToken() == null;

		et.setInput("");
		assert et.nextToken() == null;
		assert et.nextToken() == null;
	}

	private static void test02() {
		ExtractTokens et = new ExtractNumbers();

		et.setInput("0");
		assert et.nextToken().equals("0");
		assert et.nextToken() == null;

		et.setInput("1e2\n");
		assert et.nextToken().equals("1");
		assert et.nextToken().equals("2");
		assert et.nextToken() == null;

		et.setInput(".");
		assert et.nextToken() == null;

		et.setInput("+");
		assert et.nextToken() == null;

		et.setInput("-");
		assert et.nextToken() == null;

		et.setInput("+.");
		assert et.nextToken() == null;

		et.setInput("-.");
		assert et.nextToken() == null;

		et.setInput("-.0");
		assert et.nextToken().equals("-.0");
		assert et.nextToken() == null;

		et.setInput("-.0+.1");
		assert et.nextToken().equals("-.0");
		assert et.nextToken().equals("+.1");
		assert et.nextToken() == null;

		et.setInput("1..2");
		assert et.nextToken().equals("1");
		assert et.nextToken().equals(".2");
		assert et.nextToken() == null;

		et.setInput("-1.2+.3.4");
		assert et.nextToken().equals("-1.2");
		assert et.nextToken().equals("+.3");
		assert et.nextToken().equals(".4");
		assert et.nextToken() == null;
	}

	private static final int N = 100000;
	private static final String CHARS = "+-. e0123456789";
	private static final int CHARSLEN = CHARS.length();

	private static void test03() {
		ExtractTokens et = new ExtractNumbers();
		Rand rn = new Rand();

		for (int i = 1; i <= N; i++) {
			int n = rn.getRange(0, 25);
			StringBuilder sb = new StringBuilder();
			for (int j = 1; j <= n; j++) {
				int r = rn.getRange(0, CHARSLEN - 1);
				sb.append(CHARS.charAt(r));
			}

			et.setInput(sb.toString());

			while (et.nextToken() != null)
				;

			assert et.nextToken() == null;
			assert et.nextToken() == null;
		}
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
	}
}
