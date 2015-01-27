import fclib.*;
import java.util.*;
import java.io.*;

public class ww {

	// list of input lines

	private static ArrayList<String> list;

	// details of each input record

	private static int[][] tab = new int[100000][2];

	// count of input records

	private static int cnt;

	// word frequencies and weights

	private static class WordFreq {
		static int totcount;
		int count;
		double weight;
	}

	// hash table to map words to WordFreq records

	private static LinearHash<String,WordFreq> table =
		new LinearHash<String,WordFreq>();

	private static int total_count;
	private static double total_weight;

	// process a word

	private static void doword(String s, boolean lookup) {
		WordFreq wf = table.get(s);

		// first pass

		if (!lookup) {
			if (wf == null) {
				wf = new WordFreq();
				table.put(s, wf);
			}

			wf.count++;
			wf.totcount++;
		}

		// second pass

		else {
			assert wf != null;
			total_weight += wf.weight;
			total_count++;
		}
	}

	// check whether a character is valid word character
	private static boolean isch(char c) {
		return Character.isLetter(c);
	}

	// process a line of text

	private static void doline(String s, boolean lookup) {
		int len = s.length();
		int i = 0;

		for (;;) {
			while (i < len && !isch(s.charAt(i)))
				i++;

			if (i == len)
				return;

			StringBuilder sb = new StringBuilder();
			while (i < len && isch(s.charAt(i)))
				sb.append(s.charAt(i++));

			doword(sb.toString(), lookup);
		}
	}

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

		for (int i = 0; i <= cnt; i++) {
			int lo = tab[i][0];
			int hi = tab[i][0] + tab[i][1] - 1;
			for (int j = lo + 1; j <= hi - 1; j++)
				doline(list.get(j), false);
		}

		// assign word weights

		for (String s : table) {
			WordFreq wf = table.get(s);
			double d = (double)wf.totcount / wf.count;
			wf.weight = Math.pow(d, 0.40);
//???System.out.printf("%4.1f   %s\n", wf.weight, s);
		}

		// go back and process each document again

		for (int i = 0; i <= cnt; i++) {
			int lo = tab[i][0];
			int hi = tab[i][0] + tab[i][1] - 1;

			total_count = 0;
			total_weight = 0;

			for (int j = lo + 1; j <= hi - 1; j++)
				doline(list.get(j), true);

			double avg = (total_count == 0 ? 0 :
				total_weight / total_count);

if (total_count >= 1)
			System.out.printf("%s   %.1f\n", list.get(lo), avg);
		}
	}
}
