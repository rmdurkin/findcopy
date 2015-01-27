import fclib.*;
import java.util.*;

public class RawDoc_01 {

	private static void test01() {
		RawDoc rd = new RawDoc("123", "456", new Date(789));

		assert rd.getId().equals("123");
		assert rd.getText().equals("456");
		assert rd.getTimeStamp().equals(new Date(789));
	}

	public static void main(String[] args) {
		test01();
	}
}
