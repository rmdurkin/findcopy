package fclib;

import java.util.*;

// abstract class that partially implements PrepText

public abstract class AbstractPrepText implements PrepText {

	// preprocess a line of text

	public abstract String prepLine(String s);

	// get the ID describing the preprocessing scheme

	public abstract String getPrepName();

	// preprocess a list of strings

	public ArrayList<String> prepList(ArrayList<String> list,
	int startln, int numln) {
		if (list == null)
			throw new IllegalArgumentException();

		if (startln < 0 || numln < 0)
			throw new IllegalArgumentException();

		if (startln + numln - 1 >= list.size())
			throw new IllegalArgumentException();

		ArrayList<String> out = new ArrayList<String>(numln);

		for (int i = startln; i < startln + numln; i++)
			out.add(prepLine(list.get(i)));

		return out;
	}

	public ArrayList<String> prepList(ArrayList<String> list) {
		if (list == null)
			throw new IllegalArgumentException();

		return prepList(list, 0, list.size());
	}

	// preprocess a WinLoc

	public ArrayList<String> prepWinLoc(WinLoc loc) {
		if (loc == null)
			throw new IllegalArgumentException();

		return prepList(loc.getDocAttr().getLines(),
			loc.getStartln(), loc.getNumln());
	}
}
