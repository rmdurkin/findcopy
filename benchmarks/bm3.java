import fclib.*;
import java.util.*;

public class bm3 {
	private static final int N = 500000;

	private static String TMP = "serial.out";

	public static void main(String[] args) throws Exception {
		ArrayList<Long> list1 = new ArrayList<Long>();
		for (int i = 1; i <= N; i++)
			list1.add(new Long(i));

		UtilFuncs.objectToFile(list1, TMP);

		ArrayList<Long> list2 =
			(ArrayList<Long>)UtilFuncs.fileToObject(TMP);

		assert list1.equals(list2);
	}
}
