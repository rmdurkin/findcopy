package fclib;

import java.util.*;

// interface to a database

public interface DbmsInt {

	// get a work list of owner IDs whose documents
	// need to be analyzed for copy/paste instances

	ArrayList<String> getWorkOwnerIds();

	// get a large list of owner IDs for purposes
	// of analyzing a random sample of documents

	ArrayList<String> getAllOwnerIds();

	// get all the documents for an owner

	ArrayList<RawDoc> getAllForOwner(String ownerid);

	// get all the documents for an owner in DocAttr form

	ArrayList<DocAttr> getAllDocAttrForOwner(String ownerid);

// rest of these are obsolete ???

	// get a list of document IDs for a specific owner

	ArrayList<String> getDocumentIds(String ownerid);

	// get the owner for a specific document

	String getOwner(String docid);

	// get the text for a specific document

	ArrayList<String> getLines(String docid);

	// get a timestamp for a specific document

	Date getTimeStamp(String docid);
}
