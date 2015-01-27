package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

// this program uses FilterCopy to filter raw copy/paste instances

public class MainFilterOld {
	public static void main(String[] args) throws IOException {
		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL);		

		ArrayList<String> filelist = UtilFuncs.getFiles(args);

		// process the list of filenames

		for (int i = 0; i < filelist.size(); i++)
			fc.addDocument(new DocAttrFile(filelist.get(i)));

		// filter the raw hits and display them

		FilterCopy fic = new FilterCopy(fc);

		fic.verifyCopies(); // ???

		for (int i = 0; i < fic.size(); i++)
			System.out.println(fic.get(i));
	}
}
