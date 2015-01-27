package fclib;

import java.util.*;

// implementation of PrepText that trims leading and
// trailing whitespace, and converts to lower case

public class PrepText1 extends AbstractPrepText {

	// preprocess a line of text

	public String prepLine(String s) {
		if (s == null)
			throw new IllegalArgumentException("s is null");

		int len = s.length();

		StringBuilder sb = new StringBuilder(len);

		// skip leading and trailing whitespace

// assumes 7-bit ASCII for whitespace and upper/lower case ???
		int lo = 0;
		while (lo < len && s.charAt(lo) <= ' ')
			lo++;

		int hi = len - 1;
		while (hi > lo && s.charAt(hi) <= ' ')
			hi--;

		// convert remaining characters to lower case

		for (int i = lo; i <= hi; i++) {
			char c = s.charAt(i);
			if (c >= 'A' && c <= 'Z')
				c += 040;
			sb.append(c);
		}

		return sb.toString();
	}

	// get the ID describing the preprocessing scheme

	public String getPrepName() {
		return "PrepText1";
	}
}
