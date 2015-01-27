package fclib;

// split input strings into numbers

public class ExtractNumbers implements ExtractTokens {

	// current string

	private String curr;

	// length of current string

	private int len;

	// current index into string

	private int i;

	// helper methods

	private char ca() {
		return curr.charAt(i);
	}

	private boolean isdig() {
		return Character.isDigit(ca());
	}

	private boolean isdig1() {
		return Character.isDigit(curr.charAt(i + 1));
	}

	private boolean isper() {
		return ca() == '.';
	}

	// constructor

	public ExtractNumbers() {
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

	// get the next number from the string

	public String nextToken() {
		for (;;) {
			char save_sign = 0;
			char c = (i < len ? ca() : 0);
			if (c == '+' || c == '-') {
				save_sign = c;
				i++;
			}

			if (i == len)
				return null;

			if (isdig()) {
				StringBuilder sb = new StringBuilder();
				if (save_sign != 0)
					sb.append(save_sign);

				boolean seen_period = false;

				while (i < len && (isdig() ||
				(isper() && !seen_period && i + 1 < len &&
				isdig1()))) {
					sb.append(ca());
					if (ca() == '.')
						seen_period = true;
					i++;
				}

				return sb.toString();
			}

			else if (ca() == '.' && i + 1 < len && isdig1()) {
				StringBuilder sb = new StringBuilder();
				if (save_sign != 0)
					sb.append(save_sign);

				sb.append(ca());
				i++;

				while (i < len && isdig()) {
					sb.append(ca());
					i++;
				}

				return sb.toString();
			}

			else {
				i++;
			}
		}
	}
}
