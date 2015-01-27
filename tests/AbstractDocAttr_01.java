import fclib.*;
import java.util.*;

public class AbstractDocAttr_01 {

	private static final double CUTOFF = 1e-3;

	private static void test01() {
		AbstractDocAttr da = new DocAttrRaw("123", "456", "789\n101112",
			new Date(131415));

		assert da.getOwner().equals("123");

		assert da.getId().equals("456");

		ArrayList<String> lines = da.getLines();
		assert lines.size() == 2;
		assert lines.get(0).equals("789");
		assert lines.get(1).equals("101112");

		assert da.getTimeStamp().equals(new Date(131415));
	}

	private static void test02() {
		RawDoc rd = new RawDoc("456", "789\n101112",
			new Date(131415));

		AbstractDocAttr da = new DocAttrRaw("123", rd);

		assert da.getOwner().equals("123");

		assert da.getId().equals("456");

		ArrayList<String> lines = da.getLines();
		assert lines.size() == 2;
		assert lines.get(0).equals("789");
		assert lines.get(1).equals("101112");

		assert da.getTimeStamp().equals(new Date(131415));
	}

	private static void test03() {
		AbstractDocAttr da = new DocAttrRaw("123", "456", "789\n101112",
			new Date(131415));

		double d1 = da.getAgeDays();
		double d2 = UtilFuncs.getElapsedTime(131415);
		assert d2 >= d1 && d2 <= d1 + CUTOFF;
	}

	private static void test04() {
		AbstractDocAttr da = new DocAttrRaw("123", "456", "789\n101112",
			new Date(131415));

		String s = da.toString();

		assert s != null && s.length() >= 1;
	}

	public static void main(String[] args) {
		test01();
		test02();
		test03();
		test04();
	}
}
