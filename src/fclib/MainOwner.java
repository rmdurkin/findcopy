package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

// this program displays a list of hit counts and owner
// counts and owners, and writes a bad signature file

public class MainOwner {
	public static void main(String[] args) throws IOException {

		// load a bad signature file if any

		try {
			BadSig.loadFile(Config.BAD_SIG1);
		}
		catch (IOException e) {
		}

		// set up a FindCopy object

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WL_OWN);		

		ArrayList<String> filelist = UtilFuncs.getFiles(args);

		// process the list of filenames

		for (int i = 0; i < filelist.size(); i++)
			fc.addDocument(new DocAttrFile(filelist.get(i)));

		// print out the table

		fc.printTable();

		// make a list of bad signatures

		FileWriter fw = new FileWriter(Config.BAD_SIG2);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);

		fc.walkTable(pw);

		pw.close();
	}
}
