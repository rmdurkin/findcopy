package fclib;

import java.util.*;

// do syntax filtering on windows of text

public class SyntaxFilter {

	// constructor

	public SyntaxFilter() {}

	// check whether a window of text has adequate letters/digits

	public boolean isBadSyn(WinLoc loc) {
		if (loc == null)
			throw new IllegalArgumentException();

		ArrayList<String> list = Config.PT.prepWinLoc(loc);

		int numgood = 0;

		for (String s : list) {
			for (int i = 0; i < s.length(); i++) {
				if (Character.isLetterOrDigit(s.charAt(i)))
					numgood++;
			}
		}

		return numgood < CopyPasteConfig.MIN_LETTERS_DIGITS;
	}
}
