import fclib.*;
import java.io.*;
import fclib.*;
import java.util.*;

public class DirWalk_01 {

	private static void createdir() throws IOException {
		deletedir();

		File f1 = new File("f1");
		f1.createNewFile();

		File d1 = new File("d1");
		d1.mkdir();

		File f2 = new File(d1, "f2");
		f2.createNewFile();

		File f3 = new File(d1, "f3");
		f3.createNewFile();

		File d2 = new File(d1, "d2");
		d2.mkdir();

		File f4 = new File(d2, "f4");
		f4.createNewFile();
	}

	private static void deletedir() {
		File f1 = new File("f1");
		f1.delete();

		File d1 = new File("d1");

		File f2 = new File(d1, "f2");
		f2.delete();

		File f3 = new File(d1, "f3");
		f3.delete();

		File d2 = new File(d1, "d2");

		File f4 = new File(d2, "f4");
		f4.delete();

		d2.delete();

		d1.delete();
	}

	private static void test01() throws IOException {
		createdir();

		ArrayList<String> list;
		String s;

		list = new DirWalk("zzz").getPaths();
		assert list.size() == 0;

		list = new DirWalk("f1").getPaths();
		assert list.size() == 1;
		assert list.get(0).equals("f1");

		list = new DirWalk("d1").getPaths();
		assert list.size() == 3;
		s = new File(new File("d1", "d2"), "f4").toString();
		assert list.get(0).equals(s);
		assert list.get(1).equals(new File("d1", "f2").toString());
		assert list.get(2).equals(new File("d1", "f3").toString());

		list = new DirWalk(new String[]{"f1", "d1"}).getPaths();
		assert list.size() == 4;
		s = new File(new File("d1", "d2"), "f4").toString();
		assert list.get(0).equals(s);
		assert list.get(1).equals(new File("d1", "f2").toString());
		assert list.get(2).equals(new File("d1", "f3").toString());
		assert list.get(3).equals("f1");

		list = UtilFuncs.getFiles(new String[]{"f1", "d1"});
		assert list.size() == 4;
		s = new File(new File("d1", "d2"), "f4").toString();
		assert list.get(0).equals(s);
		assert list.get(1).equals(new File("d1", "f2").toString());
		assert list.get(2).equals(new File("d1", "f3").toString());
		assert list.get(3).equals("f1");

		deletedir();
	}

	public static void main(String[] args) throws IOException {
		test01();
	}
}
