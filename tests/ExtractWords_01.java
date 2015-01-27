import fclib.*;

public class ExtractWords_01 {

	private static void test01() {
		ExtractTokens et = new ExtractWords();

		assert et.nextToken() == null;
		assert et.nextToken() == null;

		et.setInput("");
		assert et.nextToken() == null;
		assert et.nextToken() == null;
	}

	private static void test02() {
		ExtractTokens et = new ExtractWords();

		et.setInput("a");
		assert et.nextToken().equals("a");
		assert et.nextToken() == null;

		et.setInput(" a37b\n");
		assert et.nextToken().equals("a");
		assert et.nextToken().equals("b");
		assert et.nextToken() == null;

		et.setInput("'");
		assert et.nextToken() == null;

		et.setInput("'a");
		assert et.nextToken().equals("a");
		assert et.nextToken() == null;

		et.setInput("a'");
		assert et.nextToken().equals("a");
		assert et.nextToken() == null;

		et.setInput("'a'a'");
		assert et.nextToken().equals("a'a");
		assert et.nextToken() == null;
	}

	private static final int N = 10000;
	private static final String CHARS = "a' ";
	private static final int CHARSLEN = CHARS.length();

	private static void test03() {
		ExtractTokens et = new ExtractWords();
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
