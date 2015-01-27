package fclib;

// represents a score for a copy/paste instance

public class CopyPasteScore implements java.io.Serializable {
	private static final char UNSET_TAG = '0';

	// risk category

	private char risk;

	// duplication

	private char dup;

	// see if a score is valid

	public static boolean isValidScore(String s) {
		if (s == null || s.length() != 2)
			return false;

		if (s.charAt(0) < '1' || s.charAt(0) > '6')
			return false;

		if (s.charAt(1) < 'A' || s.charAt(1) > 'U')
			return false;

		return true;
	}

	// default constructor

	public CopyPasteScore() {
		risk = UNSET_TAG;

		dup = UNSET_TAG;
	}

	// constructor from a string

	public CopyPasteScore(String s) {
		setScore(s);
	}

	// set the score

	public void setScore(String s) {
		if (!isValidScore(s))
			throw new IllegalArgumentException();

		risk = s.charAt(0);

		dup = s.charAt(1);
	}

	// check if the score has been set

	public boolean isSet() {
		return risk != UNSET_TAG && dup != UNSET_TAG;
	}

	// get the risk score

	public char getRisk() {
		return risk;
	}

	// get the duplication score

	public char getDup() {
		return dup;
	}

	// convert to a string

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(risk);

		sb.append(dup);

		return sb.toString();
	}
}
