package fclib;

// split input strings into words

public class ExtractWords implements ExtractTokens {

	// current string

	private String curr;

	// length of current string

	private int len;

	// current index into string

	private int i;

	// test to see if current character can start a word

	private boolean is_start() {
		return Character.isLetter(curr.charAt(i));
	}

	// test to see if current character can be in the middle of a word

	private boolean is_follow() {
		char c = curr.charAt(i);

		if (Character.isLetter(c))
			return true;

		if (c == '\'' && i + 1 < len &&
		Character.isLetter(curr.charAt(i + 1)))
			return true;

		return false;
	}

	// constructor

	public ExtractWords() {
		curr = null;

		len = 0;

		i = 0;
}

	// set the input string

	public void setInput(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		curr = s;

		len = s.length();

		i = 0;
	}

	// get the next word from the string

	public String nextToken() {
		while (i < len && !is_start())
			i++;

		if (i == len)
			return null;

		StringBuilder sb = new StringBuilder();

		while (i < len && is_follow())
			sb.append(curr.charAt(i++));

		return sb.toString();
	}
}
