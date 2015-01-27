import fclib.*;
import java.util.*;
import java.io.*;

public class printcp {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("missing input file / count");
			System.exit(1);
		}

		FileLineReader flr = new FileLineReader(args[0]);
		ArrayList<String> list = flr.getList();

		int outcount = Integer.parseInt(args[1]);
		assert outcount >= 1;

		int[][] tab = new int[1000000][2];
		int cnt = -1;

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

		Rand rn = new Rand();

		for (int i = 1; i <= outcount; i++) {
			int n = rn.getRange(0, cnt);
			for (int j = 0; j < tab[n][1]; j++)
				System.out.println(list.get(tab[n][0] + j));
		}
	}
}
