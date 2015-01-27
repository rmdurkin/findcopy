import java.util.*;

public class PersistWriter {

	// separator character

	static final char SEP_CHAR = '\u0001';

	// list of strings to be formatted for output

	private ArrayList<String> out;

	// constructor

	public PersistWriter() {
		out = new ArrayList<String>();
	}

	// convert to a structured string

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (String s : out) {
			sb.append(s);
			sb.append(SEP_CHAR);
		}

		return sb.toString();
	}

	// integer

	public void writeInt(int ival) {
		out.add(Integer.toString(ival));
	}

	// long

	public void writeLong(long lval) {
		out.add(Long.toString(lval));
	}

	// double

	public void writeDouble(double dval) {
		writeLong(Double.doubleToLongBits(dval));
	}

	// boolean

	public void writeBoolean(boolean bval) {
		writeInt(bval ? 1 : 0);
	}

	// string

	public void writeString(String sval) {
		if (sval == null)
			throw new IllegalArgumentException();

		out.add(sval);
	}

	// Date

	public void writeDate(Date dval) {
		if (dval == null)
			throw new IllegalArgumentException();

		writeLong(dval.getTime());
	}

	// list of strings

	public void writeStringArray(ArrayList<String> list) {
		if (list == null)
			throw new IllegalArgumentException();

		writeInt(list.size());

		for (String s : list)
			out.add(s);
	}

	// list of PersistObjects

	public void writeArray(ArrayList<? extends PersistObject> list) {
		if (list == null)
			throw new IllegalArgumentException();

		writeInt(list.size());

		for (PersistObject po : list)
			po.writePersist(this);
	}
}
