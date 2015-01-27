package fclib;

// exception class used for various internal errors

public class FindCopyException extends RuntimeException {
	public FindCopyException() {
		super();
	}

	public FindCopyException(String s) {
		super(s);
	}
}
