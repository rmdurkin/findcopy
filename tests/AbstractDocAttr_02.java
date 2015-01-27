import fclib.*;
import java.util.*;

public class AbstractDocAttr_02 {

	private static void test01() {
		AbstractDocAttr da1 =
			new DocAttrRaw("1", "2", "", new Date(3));
		AbstractDocAttr da2 =
			new DocAttrRaw("1", "22", "", new Date(3));
		AbstractDocAttr da3 =
			new DocAttrRaw("11", "2", "", new Date(3));
		AbstractDocAttr da4 =
			new DocAttrRaw("1", "2", "zzz", new Date(4));

		assert da1.equals(da1);
		assert !da1.equals(da2);
		assert !da1.equals(da3);
		assert !da1.equals(da4);

		assert da1.compareTo(da1) == 0;

		assert da1.compareTo(da2) < 0;
		assert da2.compareTo(da1) > 0;

		assert da1.compareTo(da3) == 0;
		assert da3.compareTo(da1) == 0;

		assert da1.compareTo(da4) < 0;
		assert da4.compareTo(da1) > 0;
	}

	public static void main(String[] args) {
		test01();
	}
}
