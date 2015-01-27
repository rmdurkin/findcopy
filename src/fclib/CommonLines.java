package fclib;

import java.util.*;

// compute contiguous sequences of lines between two lists of strings

public class CommonLines {

	// line lists

	private ArrayList<String> s;
	private ArrayList<String> t;

	// lengths of the lists

	private int m;
	private int n;

	// table used to compute lengths

	private int[][] table;

    // get hash codes for line lists

    private static int[] gethash(ArrayList<String> list) {
            assert list != null;

            int sz = list.size();

            int[] out = new int[sz];

            for (int i = 0; i < sz; i++) {
                    String s = list.get(i);
                    int code = s.length();
                    if (code == 1) {
                            code = code * 31 + s.charAt(0);
                    }
                    else if (code >= 2) {
                            code = code * 31 + s.charAt(0);
                            code = code * 31 + s.charAt(1);
                    }
                    out[i] = code;
            }

            return out;
    }

    // calculate the entries in the table

    private void calculate() {

            int[] shash = gethash(s);
            int[] thash = gethash(t);

            for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                            if (shash[i] == thash[j] &&
                            s.get(i).equals(t.get(j))) {
                                    if (i == 0 || j == 0)
                                            table[i][j] = 1;
                                    else
                                            table[i][j] =
                                                    table[i - 1][j - 1] + 1;
                            }
                            else {
                                    table[i][j] = 0;
                            }
                    }
            }
    }
        
	// constructor

	public CommonLines(ArrayList<String> list1, ArrayList<String> list2) {
		if (list1 == null || list2 == null)
			throw new IllegalArgumentException();

		s = list1;
		t = list2;

		m = s.size();
		n = t.size();

		table = new int[m][n];

		calculate();
	}

	// get a sorted list of contiguous sequences

	public ArrayList<CommonLinesEntry>
	getSequences(int minlen, boolean fold) {
		if (minlen < 1)
			throw new IllegalArgumentException();

		ArrayList<CommonLinesEntry> outlist =
			new ArrayList<CommonLinesEntry>();

		// find all the entries with adequate length

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				int len = table[i][j];
				while (len >= minlen) {
					int pos1 = i - len + 1;
					int pos2 = j - len + 1;
					outlist.add(new CommonLinesEntry(
						len, pos1, pos2));
					len--;
				}
			}
		}

		// sort the list

		Collections.sort(outlist);

		// fold overlapping entries

		if (!fold)
			return outlist;

		ArrayList<CommonLinesEntry> newlist =
			new ArrayList<CommonLinesEntry>();

		int sz = outlist.size();

		for (int i = 0; i < sz; i++) {
			CommonLinesEntry e = outlist.get(i);

			// if an entry already used up, skip it

			if (e.getUsed())
				continue;

			// add entry to the output list

			newlist.add(e);

			// mark as used all subsidiary entries
			// that are totally covered by this entry

			int min1 = e.getPos1();
			int max1 = min1 + e.getLen() - 1;
			assert min1 >= 0 && max1 >= min1 && max1 < m;

			int min2 = e.getPos2();
			int max2 = min2 + e.getLen() - 1;
			assert min2 >= 0 && max2 >= min2 && max2 < n;

			for (int j = i + 1; j < sz; j++) {
				CommonLinesEntry ee = outlist.get(j);

				if (ee.getUsed())
					continue;

				int p1min = ee.getPos1();
				int p1max = p1min + ee.getLen() - 1;
				if (p1min < min1 || p1max > max1)
					continue;

				int p2min = ee.getPos2();
				int p2max = p2min + ee.getLen() - 1;
				if (p2min < min2 || p2max > max2)
					continue;

				ee.setUsed();
			}
		}

		return newlist;
	}

	// get a sorted list of contiguous sequences

	public ArrayList<CommonLinesEntry> getSequences(int minlen) {
		return getSequences(minlen, true);
	}

	// test the sequences returned by getSequences()

	public void verifySequences() {
		boolean[] b = new boolean[]{false, true};

		for (int i = 0; i < b.length; i++) {
			ArrayList<CommonLinesEntry> seq = getSequences(1, b[i]);

			int sz = seq.size();

			for (int j = 0; j < sz; j++) {
				CommonLinesEntry e = seq.get(j);
				int len = e.getLen();
				int pos1 = e.getPos1();
				int pos2 = e.getPos2();

				assert len >= 1;
				assert pos1 >= 0 && pos2 >= 0;
				assert pos1 + len - 1 < m;
				assert pos2 + len - 1 < n;

				for (int k = 0; k < len; k++) {
					String s1 = s.get(pos1 + k);
					String s2 = t.get(pos2 + k);
					assert s1.equals(s2);
				}

				if (!b[i])
					continue;
//???if (pos1 > 0 && pos2 > 0)
//	assert !s.get(pos1 - 1).equals(t.get(pos2 - 1));

				if (pos1 + len < m && pos2 + len < n) {
					String s1 = s.get(pos1 + len);
					String s2 = t.get(pos2 + len);
					assert !s1.equals(s2);
				}

				for (int k = j + 1; k < sz; k++) {
					CommonLinesEntry ee = seq.get(k);
					assert pos1 != ee.getPos1() ||
						pos2 != ee.getPos2();
				}
			}
		}
	}
}
