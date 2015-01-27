package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

public class MainFile {
	private static final int[] WINLIST = new int[]{5, 10, 20};

	public static void main(String[] args) throws IOException {
		FindCopy fc = new FindCopy(WINLIST, Config.VW, Config.VS_WLL);		
		ArrayList<String> filelist = UtilFuncs.getFiles(args);

		// process the list of filenames

		for (int i = 0; i < filelist.size(); i++)
			fc.addDocument(new DocAttrFile(filelist.get(i)));

		// print out the table

		fc.printTable();

		// verify the table

//		fc.verifyTable();
	}
}
