package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents;
// this program displays a list of hit counts and signatures

public class MainHitSig {
	public static void main(String[] args) throws IOException {

		// create a FindCopy object

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WL_HIT);		

		// get the list of filenames

		ArrayList<String> filelist = UtilFuncs.getFiles(args);

		// process the list of filenames

		for (int i = 0; i < filelist.size(); i++)
			fc.addDocument(new DocAttrFile(filelist.get(i)));

		// print out the table

		fc.printTable();
	}
}
