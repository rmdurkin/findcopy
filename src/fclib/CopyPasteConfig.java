package fclib;

// global settings for copy/paste analysis

public class CopyPasteConfig {

	// no instances of this class can be created

	private CopyPasteConfig() {}

	// minimum number of letters/digits for text window to be good

	public static final int MIN_LETTERS_DIGITS = 5;

	// minimum/maximum age in days of "recent" documents

	public static final double MIN_RECENT_DAYS = 0.0;

	public static final double MAX_RECENT_DAYS = 10000.0;

	// default value for "recent" documents

	public static final double DEFAULT_RECENT_DAYS = 30.0;

	// standard ExtractTokens objects for parsing into words/numbers

	public static final ExtractTokens[] ET_LIST = new ExtractTokens[]{
		new ExtractWords(),
		new ExtractNumbers()
	};

	// maximum age in days of bad signature file before regarded as stale

	public static final double MAX_BAD_SIG_DAYS = 7.0;

	// name of bad signature files

	public static final String BAD_SIG1 = "badsig1.txt";

	public static final String BAD_SIG2 = "badsig2.txt";

	// name of line frequency file

	public static final String LINE_FREQ = "line_freq.txt";

	// name of word frequency file

	public static final String WORD_FREQ = "word_freq.txt";

	// name of exclusion list file

	public static final String EXCLUSION_LIST = "exclusion_list.txt";

	// names of global template files

	public static final String GT_FREQ = "gt_freq.txt";

	public static final String GT_FULL = "gt_full.txt";

	public static final String GT_SIGS = "gt_sigs.txt";

	public static final String GT_STAT = "gt_stat.txt";

	// names of copy/paste output files

	public static final String CP_SUM = "cp_sum.txt";

	public static final String CP_FULL = "cp_full.txt";

	static {
		assert MIN_LETTERS_DIGITS >= 1;

		assert ET_LIST != null && ET_LIST.length >= 1;

		assert MIN_RECENT_DAYS >= 0;
		assert MIN_RECENT_DAYS <= MAX_RECENT_DAYS;
		assert DEFAULT_RECENT_DAYS >= MIN_RECENT_DAYS;
		assert DEFAULT_RECENT_DAYS <= MAX_RECENT_DAYS;

		assert MAX_BAD_SIG_DAYS >= 1.0;
		assert MAX_BAD_SIG_DAYS <= 90.0;
	}
}
