import fclib.*;
import java.util.*;

public class WinLoc_01 {

	private static void test01() {
		DocAttr da = new DocAttrRaw("1", "2", "a\nb\nc\n", new Date(3));

		WinLoc loc = new WinLoc(da, 1, 2);

		assert loc.getDocAttr() == da;
		assert loc.getStartln() == 1;
		assert loc.getNumln() == 2;
	}

	private static void test02() {
		DocAttr da = new DocAttrRaw("1", "2",
			"a\nb\nc\nd\ne\n", new Date(3));

		WinLoc loc1 = new WinLoc(da, 1, 1);
		WinLoc loc2 = new WinLoc(da, 2, 1);
		WinLoc loc3 = new WinLoc(da, 1, 2);
		WinLoc loc4 = new WinLoc(da, 2, 2);

		assert loc1.equals(loc1);
		assert !loc1.equals(loc2);
		assert !loc1.equals(loc3);
		assert !loc1.equals(loc4);

		assert loc1.compareTo(loc1) == 0;

		assert loc1.compareTo(loc2) < 0;
		assert loc2.compareTo(loc1) > 0;

		assert loc1.compareTo(loc3) < 0;
		assert loc3.compareTo(loc1) > 0;

		assert loc1.compareTo(loc4) < 0;
		assert loc4.compareTo(loc1) > 0;

		assert loc2.compareTo(loc3) > 0;
		assert loc3.compareTo(loc2) < 0;

		assert loc2.compareTo(loc4) < 0;
		assert loc4.compareTo(loc2) > 0;

		assert loc3.compareTo(loc4) < 0;
		assert loc4.compareTo(loc3) > 0;
	}

	private static void test03() {
		DocAttr da1 = new DocAttrRaw("1", "2", "a\nb", new Date(3));
		DocAttr da2 = new DocAttrRaw("1", "3", "a\nb", new Date(3));
		DocAttr da3 = new DocAttrRaw("1", "2", "a\nb", new Date(4));

		WinLoc loc1 = new WinLoc(da1, 1, 1);
		WinLoc loc2 = new WinLoc(da2, 1, 1);
		WinLoc loc3 = new WinLoc(da3, 1, 1);

		assert loc1.equals(loc1);
		assert !loc1.equals(loc2);
		assert !loc1.equals(loc3);
		assert !loc2.equals(loc3);

		assert loc1.compareTo(loc1) == 0;

		assert loc1.compareTo(loc2) < 0;
		assert loc2.compareTo(loc1) > 0;

		assert loc2.compareTo(loc3) < 0;
		assert loc3.compareTo(loc2) > 0;
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
	}
}
