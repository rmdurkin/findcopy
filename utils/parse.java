import fclib.*;
import java.util.*;
import java.io.*;

public class parse {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("missing input file");
			System.exit(1);
		}

		FileLineReader flr = new FileLineReader(args[0]);
		ArrayList<String> list = flr.getList();

		ExtractTokens et1 = new ExtractWords();
		ExtractTokens et2 = new ExtractNumbers();

		for (String s : list) {
			et1.setInput(s);
			et2.setInput(s);

			String t;

			while ((t = et1.nextToken()) != null)
				System.out.println(t);

			while ((t = et2.nextToken()) != null)
				System.out.println(t);
		}
	}
}
