package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

// this program uses FilterCopy to filter raw copy/paste instances,
// and operates on random documents

public class MainFilterRand {
	private static final Rand rn = new Rand();

	public static void main(String[] args) throws IOException {
		FindCopy fc = new FindCopy(new int[]{rn.getRange(1, 10)},
			null, Config.VS_WLL);		

		boolean b = (rn.getRange(1, 2) == 1);

		// process the list of filenames

		int n = rn.getRange(0, 100);

		for (int i = 1; i <= n; i++) {
			String dn = "doc" + i;
			DocAttr da = (b ? new DocAttrRand(dn) :
				new DocAttrShort(dn));
			fc.addDocument(da);
		}

		// filter the raw hits and display them

		FilterCopy fic = new FilterCopy(fc);

		fic.verifyCopies();

		for (int i = 0; i < fic.size(); i++)
			System.out.println(fic.get(i));
	}
}
