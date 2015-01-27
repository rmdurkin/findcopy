import fclib.*;
import java.util.*;

// compute statistics across all owners and documents

public class MainStats {
	public static void main(String[] args) {

		long numowners = 0;
		long numdocs = 0;
		long numlines = 0;
		long numchars = 0;

		// get a list of owners from the database

		ArrayList<String> owners = Config.DB.getAllOwnerIds();
		assert owners.size() >= 1;

		// process the owners

		for (String owner : owners) {

			numowners++;

			// get the documents for an owner

			ArrayList<DocAttr> docs =
				Config.DB.getAllDocAttrForOwner(owner);

			// process the documents

			for (DocAttr da : docs) {
				numdocs++;

				ArrayList<String> lines = da.getLines();

				numlines += lines.size();

				for (String s : lines)
					numchars += s.length();
			}
		}

		System.out.println("number of owners = " + numowners);
		System.out.println("number of documents = " + numdocs);
		System.out.println("number of lines = " + numlines);
		System.out.println("number of characters " + numchars);

		System.out.println();

		System.out.println("documents/owner = " + numdocs / numowners);
		System.out.println("lines/document = " + numlines / numdocs);
		System.out.println("chars/document = " + numchars / numdocs);
	}
}
