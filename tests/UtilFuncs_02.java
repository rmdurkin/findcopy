import fclib.*;
import java.io.*;
import java.util.*;

public class UtilFuncs_02 {

	private static final int N = 100000;

	private static final String TMPFILE = "tmpfile";

	private static void test01() throws Exception {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		for (int i = 1; i <= N; i++)
			list1.add(new Integer(i));
		UtilFuncs.objectToFile(list1, TMPFILE);

		assert UtilFuncs.isSerialFile(TMPFILE);

		ArrayList<Integer> list2 =
			(ArrayList<Integer>)UtilFuncs.fileToObject(TMPFILE);
		assert list1.equals(list2);

		new File(TMPFILE).delete();
	}

	private static void test02() throws Exception {
		ArrayList<String> list1 = new ArrayList<String>();
		for (int i = 1; i <= N; i++)
			list1.add("testing" + i);
		byte[] buf = UtilFuncs.objectToArray(list1);

		assert UtilFuncs.isSerialArray(buf);

		ArrayList<String> list2 =
			(ArrayList<String>)UtilFuncs.arrayToObject(buf);
		assert list1.equals(list2);
	}

	private static void test03() throws Exception {
		String fn = UtilFuncs.createTempFile();
		assert !UtilFuncs.isSerialFile(fn);
		FileLineWriter flw = new FileLineWriter(fn);
		flw.add("testing");
		flw.write();
		assert !UtilFuncs.isSerialFile(fn);

		FileLineReader flr = new FileLineReader(fn);
		assert flr.size() == 1 && flr.get(0).equals("testing");

		new File(fn).delete();
	}

	public static void main(String[] args) throws Exception {
		test01();
		test02();
		test03();
	}
}
