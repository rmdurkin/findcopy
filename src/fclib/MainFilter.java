package fclib;
import fclib.*;
import java.io.*;
import java.util.*;

// driver program for documents taken from a database

// this program uses FilterCopy to filter raw copy/paste instances

public class MainFilter {
	public static void main(String[] args) throws IOException {
		FindCopy fc = new FindCopy(Config.WIN_SIZE,
			Config.VW, Config.VS_WLL);		

		// process the list of owners

		ArrayList<String> owners = Config.DB.getWorkOwnerIds();
		assert owners.size() >= 1;

		for (int i = 0; i < owners.size(); i++) {
			String owner = owners.get(i);

			ArrayList<DocAttr> docs =
				Config.DB.getAllDocAttrForOwner(owner);
			assert docs.size() >= 1;

			for (int j = 0; j < docs.size(); j++)
				fc.addDocument(docs.get(j));
		}

		// filter the raw hits and display them

		FilterCopy fic = new FilterCopy(fc);

		fic.verifyCopies(); // ???

		boolean oneline = true; // true for one-line summary

		for (int i = 0; i < fic.size(); i++) {
			if (oneline) {
				System.out.println(fic.get(i));
			}
			else {
				WinLoc loc1 = fic.get(i).getFirst();
				WinLoc loc2 = fic.get(i).getSecond();
				System.out.println(loc1);
				loc1.printLines();
				System.out.println("-----");
				System.out.println(loc2);
				loc2.printLines();
				System.out.println("=========================");
			}
		}
	}
}
