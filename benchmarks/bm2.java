import fclib.*;
import java.util.*;

public class bm2 {
	private static final int N = 1000000;

	public static void main(String[] args) throws Exception {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		for (int i = 1; i <= N; i++)
			list1.add(new Integer(i));

		byte[] buf = UtilFuncs.objectToArray(list1);

		ArrayList<Integer> list2 =
			(ArrayList<Integer>)UtilFuncs.arrayToObject(buf);

		assert list1.equals(list2);
	}
}
