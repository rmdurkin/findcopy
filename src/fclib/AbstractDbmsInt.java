package fclib;

import java.util.*;

// abstract class that partially implements DbmsInt

public abstract class AbstractDbmsInt implements DbmsInt {

	// get a work list of owner IDs whose documents
	// need to be analyzed for copy/paste instances

	public abstract ArrayList<String> getWorkOwnerIds();

	// get a large list of owner IDs for purposes
	// of analyzing a random sample of documents

	public abstract ArrayList<String> getAllOwnerIds();

	// get a list of document IDs for a specific owner

	public abstract ArrayList<String> getDocumentIds(String ownerid);

	// get the owner for a specific document

	public abstract String getOwner(String docid);

	// get the text for a specific document

	public abstract ArrayList<String> getLines(String docid);

	// get a timestamp for a specific document

	public abstract Date getTimeStamp(String docid);

	// get all the documents for an owner

	public abstract ArrayList<RawDoc> getAllForOwner(String ownerid);

	// get all the documents for an owner in DocAttr form

	public ArrayList<DocAttr> getAllDocAttrForOwner(String ownerid) {
		if (ownerid == null || ownerid.length() == 0)
			throw new IllegalArgumentException();

		ArrayList<RawDoc> rdlist = getAllForOwner(ownerid);

		int sz = rdlist.size();

		ArrayList<DocAttr> out = new ArrayList<DocAttr>(sz);

		for (int i = 0; i < sz; i++)
			out.add(new DocAttrRaw(ownerid, rdlist.get(i)));

		return out;
	}
}
