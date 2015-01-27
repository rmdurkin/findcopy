package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for disk files used as documents

// this program displays a list of hit counts and owner
// counts and owners, and writes a bad signature database

public class MainBadSig {
	public static void main(String[] args) throws IOException {

		// get the list of documents

		if (args.length != 1) {
			System.err.println("missing docsdir");
			System.exit(1);
		}

		ArrayList<String> list = new DirWalk(args[0]).getPaths();

		// set up a FindCopy object

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WL_OWN);		

		// process the list of documents

		for (int i = 0; i < list.size(); i++)
			fc.addDocument(new DocAttrFile(list.get(i)));

		// print out the table

//???		fc.printTable();

		// make a list of bad signatures

		FileWriter fw = new FileWriter(Config.BAD_SIG2);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);

		pw.printf("%s\n", UtilFuncs.getSettings());

		fc.walkTable(pw);

		pw.close();
	}
}
