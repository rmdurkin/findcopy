package fclib;

// global constants and fixed settings

public class Constants {

	// no instances of this class can be created

	private Constants() {}

	// seconds in an hour

	public static final int SEC_PER_HOUR = 60 * 60;

	// milliseconds in an hour

	public static final int MILLISEC_PER_HOUR = SEC_PER_HOUR * 1000;

	// seconds in a day

	public static final int SEC_PER_DAY = SEC_PER_HOUR * 24;

	// milliseconds in a day

	public static final int MILLISEC_PER_DAY = SEC_PER_DAY * 1000;

	// algorithm used to hash windows

	public static final String HASH_ALG = "SHA-256";

	// length of hash output

	public static final int HASH_LEN_BITS = 256;

	public static final int HASH_LEN_BYTES = HASH_LEN_BITS / 8;

	// end-of-line terminator

	public static final char EOL_CHAR = '\n';

	public static final String EOL = Character.toString(EOL_CHAR);

	// string used to separate lines for I/O purposes

	public static final String IO_SEP = System.getProperty("line.separator");

	// value used when line/word weights are not present

	public static final double BAD_WEIGHT = -1.0;

	static {
		assert HASH_LEN_BITS >= 128;

		assert HASH_LEN_BITS % 8 == 0;

		assert HASH_LEN_BYTES * 8 == HASH_LEN_BITS;

		assert EOL.length() == 1;

		assert IO_SEP.length() >= 1;
	}
}
