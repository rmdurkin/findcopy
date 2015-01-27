import fclib.*;
import java.util.*;
import java.io.*;

public class wwf {

	// list of input lines

	private static ArrayList<String> list;

	// details of each input record

	private static int[][] tab = new int[100000][2];

	// count of input records

	private static int cnt;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("missing input file");
			System.exit(1);
		}

		// read in lines from file

		FileLineReader flr = new FileLineReader(args[0]);
		list = flr.getList();

		// break the input into records

		cnt = -1;

		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);

			if (s.indexOf("<<< ") == 0) {
				tab[++cnt][0] = i;
				tab[cnt][1]++;
			}
			else {
				tab[cnt][1]++;
			}
		}

		// tabulate word frequencies

		ExtractTokens et1 = new ExtractWords();
		ExtractTokens et2 = new ExtractNumbers();

		WordFreq wf = new WordFreq(et1, et2);

		for (int i = 0; i <= cnt; i++) {
			int lo = tab[i][0];
			int hi = tab[i][0] + tab[i][1] - 1;
			ArrayList<String> out = new ArrayList<String>();
			for (int j = lo + 1; j <= hi - 1; j++)
				out.add(list.get(j));

			wf.addDocument(out);
		}

		wf.write("x1");
	}
}
