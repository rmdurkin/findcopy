import fclib.*;

public class CopyPasteScore_01 {

	private static class Rec {
		String s;
		boolean b;
		Rec(String s, boolean b) {
			this.s = s;
			this.b = b;
		}
	};

	private static final Rec tab[] = new Rec[]{
		new Rec(null, false),
		new Rec("", false),
		new Rec(" ", false),
		new Rec("x", false),
		new Rec("xy", false),
		new Rec("1", false),
		new Rec("11", false),
		new Rec("A", false),
		new Rec("AA", false),
		new Rec("A1", false),
		new Rec("1A1", false),
		new Rec("A1A", false),
		new Rec("1A", true),
		new Rec("4F", true),
	};

	private static void test01() {
		for (int i = 0; i < tab.length; i++) {
			Rec r = tab[i];
			assert CopyPasteScore.isValidScore(r.s) == r.b;
		}
	}

	private static void test02() {
		CopyPasteScore cps = new CopyPasteScore();
		assert !cps.isSet();
		assert cps.toString().equals("00");

		cps = new CopyPasteScore("1A");
		assert cps.isSet();
		assert cps.toString().equals("1A");

		cps.setScore("4F");
		assert cps.isSet();
		assert cps.toString().equals("4F");
		assert cps.getRisk() == '4';
		assert cps.getDup() == 'F';
	}

	public static void main(String[] args) {
		test01();
		test02();
	}
}
