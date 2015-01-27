import fclib.*;
import java.util.*;

public class SyntaxFilter_01 {

	private static WinLoc getloc(String s) {
		DocAttr da = new DocAttrRaw("123", "456", s, new Date(0));

		return new WinLoc(da, 0, 1);
	}

	private static void test01() {
		SyntaxFilter sf = new SyntaxFilter();

		assert sf.isBadSyn(getloc(" "));
		assert sf.isBadSyn(getloc("\t"));
		assert sf.isBadSyn(getloc("\n"));
		assert sf.isBadSyn(getloc("\n\n\n\n\n\n\n\n\n\n"));
		assert sf.isBadSyn(getloc("................................"));
	}

	private static void test02() {
		SyntaxFilter sf = new SyntaxFilter();

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < CopyPasteConfig.MIN_LETTERS_DIGITS; i++)
			sb.append('a');
		assert sf.isBadSyn(getloc(sb.toString()));

		sb = new StringBuilder();
		for (int i = 1; i <= CopyPasteConfig.MIN_LETTERS_DIGITS; i++)
			sb.append('a');
		assert !sf.isBadSyn(getloc(sb.toString()));

	}

	public static void main(String[] args) {
		test01();
		test02();
	}
}
