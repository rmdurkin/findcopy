package fclib;

// global settings for FindCopy

public class Config {

	// no instances of this class can be created

	private Config() {}

	// standard database object

	public static final DbmsInt DB = new DbmsRand();

	// directory where temporary files are stored

	public static final String TMP_DIR = ".";

	// standard PrepText object

	public static final PrepText PT = new PrepText1();

	// standard window size

	public static final int WIN_SIZE_STD = 5;

	public static final int[] WIN_SIZE = new int[]{WIN_SIZE_STD};

	// program version

	public static final String PROG_VERSION = "1.00";

	// sanity check on the above settings

	static {
		assert DB != null;

		assert TMP_DIR != null && TMP_DIR.length() >= 1;

		assert PT != null;

		assert WIN_SIZE_STD >= 1;
		assert WIN_SIZE_STD <= 100;

		assert WIN_SIZE.length >= 1;

		assert PROG_VERSION != null && PROG_VERSION.length() >= 1;
	}
}
