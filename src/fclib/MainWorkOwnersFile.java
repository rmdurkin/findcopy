package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// ???

public class MainWorkOwnersFile {

	private static long numCopies(FindCopy fc) {
		assert fc != null;

		long cnt = 0;

		for (WinSig sig : fc) {
			WinLocList wll = (WinLocList)fc.get(sig);
			int n = wll.size();
			cnt += n * (n - 1) / 2;
		}

		return cnt;
	}

	public static void main(String[] args) {

		// get the list of documents

		ArrayList<String> list = new DirWalk(args[0]).getPaths();

		// load a bad signature file

		try {
			BadSig.loadFile(Config.BAD_SIG2);
		}
		catch (IOException e) {
			throw new FindCopyException("MainWorkOwnersFile");
		}

		// set up FindCopy objects

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL);		

		FindCopy fcbad = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL_BAD);		

		// add the documents

		for (String fn : list) {
			fc.addDocument(new DocAttrFile(fn));
			fcbad.addDocument(new DocAttrFile(fn));
		}

		// set up FilterCopy objects

		FilterCopy fic = new FilterCopy(fc);
		FilterCopy ficbad = new FilterCopy(fcbad);

		// display statistics

		long size = numCopies(fc);
		long sizebad = numCopies(fcbad);

		int ficsize = fic.size();
		int ficsizebad = ficbad.size();

		assert sizebad <= size;
		assert ficsizebad <= ficsize;
		assert ficsize <= size && ficsizebad <= sizebad;

		System.out.print(size + " -> " + sizebad + "   ");
		System.out.print(ficsize + " -> " + ficsizebad + "   ");
		System.out.println();

for (int i = 0; i < ficbad.size(); i++) {
System.out.println(ficbad.get(i));
}
	}
}
