import fclib.*;
import java.util.*;
import java.io.*;

public class lf {

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

		// tabulate line frequencies

		LineFreq lf = new LineFreq();

		for (int i = 0; i <= cnt; i++) {
			int lo = tab[i][0];
			int hi = tab[i][0] + tab[i][1] - 1;
			ArrayList<String> out = new ArrayList<String>();
			for (int j = lo + 1; j <= hi - 1; j++)
				out.add(list.get(j));

			lf.addDocument(out);
		}

		lf.write(Config.LINE_FREQ);
	}
}
