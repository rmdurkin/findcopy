import java.io.*;
import java.util.*;

public class filetolines {
	public static void main(String[] args) throws IOException {
//		String s = UtilFuncs.fileToString(args[0]);
String s = "a\nb\nc";
		ArrayList<String> list = UtilFuncs.stringToLines(s, '\n');

		System.out.println(list.size());

		for (int i = 0; i < list.size(); i++)
			System.out.println(i + "   " + list.get(i));
	}
}
