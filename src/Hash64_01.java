import fclib.*;

public class Hash64_01 {

	private static void test01() {
		Hash64 h1a = new Hash64();
		Hash64 h1b = new Hash64();
		h1a.addString("a");
		h1a.addString(Constants.EOL);
		h1b.addLine("a");
		assert h1a.getHash() == h1b.getHash();

		Hash64 h2a = new Hash64();
		Hash64 h2b = new Hash64();
		h2a.addString("abc");
		h2b.addString("a");
		h2b.addString("b");
		h2b.addString("c");
		assert h2a.getHash() == h2b.getHash();
	}

	private static void test02() {
		Hash64 h1 = new Hash64();
		h1.addString("a");
		assert h1.getHash() == 'a';

		Hash64 h2 = new Hash64();
		h2.addString("ab");
		assert h2.getHash() == ('a' * 31 + 'b');

		Hash64 h3 = new Hash64();
		h3.addString("abc");
		assert h3.getHash() == (('a' * 31 + 'b') * 31 + 'c');
	}

	public static void main(String[] args) {
		test01();
		test02();
	}
}
