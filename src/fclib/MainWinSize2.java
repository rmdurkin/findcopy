package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents;
// this program displays statistics for various window sizes

public class MainWinSize2 {

	// try different window sizes, and
	// count the number of duplicates

	private static void doit(ArrayList<String> list, int winlen) {
		assert list != null && winlen >= 1;

		FindCopy fc = new FindCopy(new int[]{winlen},
			null, Config.VS_WLL);

		for (int i = 0; i < list.size(); i++)
			fc.addDocument(new DocAttrFile(list.get(i)));

		int sz = fc.size();

		int numdup = 0;

		for (WinSig sig : fc) {
			WinLocList wll = (WinLocList)fc.get(sig);
			numdup += (wll.size() - 1);
		}

		double d = (double)(numdup + sz) / sz;

		System.out.printf("%3d   %8d   %5.2f\n", winlen, numdup, d);
	}

	public static void main(String[] args) throws IOException {
		ArrayList<String> filelist = UtilFuncs.getFiles(args);

		// process the list of filenames

		for (int i = 1; i <= 20; i++)
			doit(filelist, i);
	}
}
