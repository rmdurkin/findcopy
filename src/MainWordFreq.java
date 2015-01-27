import fclib.*;
import java.io.*;
import java.util.*;

// create a database of word weights based
// on analyzing a large number of documents

public class MainWordFreq {
	public static void main(String[] args) throws IOException {

		// get a list of all owners

		ArrayList<String> owners = Config.DB.getAllOwnerIds();

		// set up the WordFreq object

		WordFreq wf = new WordFreq(CopyPasteConfig.WORD_FREQ,
			CopyPasteConfig.ET_LIST);

		// iterate across the owners

		for (String owner : owners) {

			// get all documents for an owner

			ArrayList<DocAttr> docs =
				Config.DB.getAllDocAttrForOwner(owner);

			// iterate across the documents

			for (DocAttr doc : docs) {

				// get the lines from the document

				ArrayList<String> list = doc.getLines();

				// preprocess the lines

				list = Config.PT.prepList(list);

				// add to WordFreq object

				wf.addDocument(list);
			}
		}

		// write out the WordFreq data

		wf.write();
	}
}
