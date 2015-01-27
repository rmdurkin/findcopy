import java.lang.reflect.*;
import java.util.*;

public class PersistReader {

	// list of strings to be read from

	private ArrayList<String> list;

	// current position in the list

	int currpos;

	// constructor

	public PersistReader(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		list = new ArrayList<String>();

		currpos = 0;

		StringBuilder sb = new StringBuilder();

		boolean empty = true;

		int len = s.length();

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);

			if (c == PersistWriter.SEP_CHAR) {
				list.add(sb.toString());
				sb = new StringBuilder();
				empty = true;
			}
			else {
				sb.append(c);
				empty = false;
			}
		}

		if (!empty)
			throw new FindCopyException("PersistReader");
	}

	// integer

	public int readInt() {
		return Integer.parseInt(list.get(currpos++));
	}

	public int readInt(int min) {
		int ival = readInt();

		if (ival < min)
			throw new FindCopyException("readInt");

		return ival;
	}

	// long

	public long readLong() {
		return Long.parseLong(list.get(currpos++));
	}

	public long readLong(long min) {
		long lval = readLong();

		if (lval < min)
			throw new FindCopyException("readLong");

		return lval;
	}

	// double

	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	public double readDouble(double min) {
		double dval = readDouble();

		if (dval < min)
			throw new FindCopyException("readDouble");

		return dval;
	}

	// boolean

	public boolean readBoolean() {
		int ival = readInt();

		if (ival == 0)
			return false;
		else if (ival == 1)
			return true;
		else
			throw new FindCopyException("readBoolean");
	}

	// string

	public String readString() {
		return list.get(currpos++);
	}

	// Date

	public Date readDate() {
		return new Date(readLong(0));
	}

	// list of strings

	public ArrayList<String> readStringArray() {
		int sz = readInt(0);

		ArrayList<String> out = new ArrayList<String>();

		for (int i = 0; i < sz; i++)
			out.add(readString());

		return out;
	}

	// list of PersistObjects

	public ArrayList<PersistObject> readArray(
	Class<? extends PersistReader> type) {
		if (type == null)
			throw new IllegalArgumentException();

		Constructor<? extends PersistReader> ctor;

		try {
			ctor = (Constructor<? extends PersistReader>)
				type.getConstructor(PersistReader.class);
		}
		catch (Exception e) {
			throw new FindCopyException("readArray");
		}

		int sz = readInt(0);

		ArrayList<PersistObject> out = new ArrayList<PersistObject>();

		for (int i = 0; i < sz; i++) {
			try {
				PersistObject po =
					(PersistObject)ctor.newInstance(this);
				out.add(po);
			}
			catch (Exception e) {
				throw new FindCopyException("readArray");
			}
		}

		return null;
	}
}
