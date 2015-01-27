package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// this program displays a list of hit counts and owner
// counts and owners, and writes a bad signature database

public class MainBadSigDbms {
	public static void main(String[] args) throws IOException {

		// set up a FindCopy object

		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WL_OWN);		

		// get a list of owners from the database

		ArrayList<String> owners = Config.DB.getAllOwnerIds();
		assert owners.size() >= 1;

		// process the owners

		for (String owner : owners) {

			// get the documents for an owner

			ArrayList<DocAttr> docs =
				Config.DB.getAllDocAttrForOwner(owner);
			assert docs.size() >= 1;

			// process the documents

			for (DocAttr da : docs)
				fc.addDocument(da);
		}

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
