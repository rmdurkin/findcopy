import fclib.*;
import java.util.*;
import java.io.*;

public class lf2 {

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

		// read in the weights

		LineFreqReader lfr = new LineFreqReader(Config.LINE_FREQ);
		WordFreqReader wfr =
			new WordFreqReader(Config.WORD_FREQ, Config.ET_LIST);

		// tabulate line frequencies

		for (int i = 0; i <= cnt; i++) {
			int lo = tab[i][0];
			int hi = tab[i][0] + tab[i][1] - 1;

			double wt = lfr.getWeight(list, lo + 1, tab[i][1] - 2);
			double avg = lfr.getScaledWeight(wt);

			double wt2 = wfr.getWeight(list, lo + 1, tab[i][1] - 2);
			double avg2 = wfr.getScaledWeight(wt2);

			System.out.printf("%s     avg = %.1f %.1f %.2f %.2f\n",
				list.get(lo), wt, wt2, avg, avg2);
			for (int j = lo + 1; j <= hi - 1; j++)
				System.out.println(list.get(j));
			System.out.println(list.get(hi));
		}
	}
}
