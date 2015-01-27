import fclib.*;
import java.util.*;

public class bm1 {
	private static final int N = 1000000;

	private static String TMP = "serial.out";

	public static void main(String[] args) throws Exception {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		for (int i = 1; i <= N; i++)
			list1.add(new Integer(i));

		UtilFuncs.objectToFile(list1, TMP);

		ArrayList<Integer> list2 =
			(ArrayList<Integer>)UtilFuncs.fileToObject(TMP);

		assert list1.equals(list2);
	}
}
