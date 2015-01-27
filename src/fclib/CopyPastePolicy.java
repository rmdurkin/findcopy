package fclib;

// policy class for copy/paste analysis

public class CopyPastePolicy {

	// BadSigReader

	private BadSigReader bsr;

	// SyntaxFilter

	private SyntaxFilter sfi;

	// LineFreqReader

	private LineFreqReader lfr;

	// WordFreqReader

	private WordFreqReader wfr;

	// ExclusionList

	private ExclusionList el;

	// recent days setting

	private double min_recent;

	private double max_recent;

	// constructor

	public CopyPastePolicy() {
		bsr = null;

		sfi = null;

		lfr = null;

		wfr = null;

		min_recent = 0;
		max_recent = CopyPasteConfig.DEFAULT_RECENT_DAYS;
	}

	// set/get bad signature file

	public void setBadSigReader(BadSigReader bsr) {
		this.bsr = bsr;
	}

	public BadSigReader getBadSigReader() {
		return bsr;
	}

	// set/get SyntaxFilter

	public void setSyntaxFilter(SyntaxFilter sfi) {
		this.sfi = sfi;
	}

	public SyntaxFilter getSyntaxFilter() {
		return sfi;
	}

	// set/get LineFreqReader

	public void setLineFreqReader(LineFreqReader lfr) {
		this.lfr = lfr;
	}

	public LineFreqReader getLineFreqReader() {
		return lfr;
	}

	// set/get WordFreqReader

	public void setWordFreqReader(WordFreqReader wfr) {
		this.wfr = wfr;
	}

	public WordFreqReader getWordFreqReader() {
		return wfr;
	}

	// set/get ExclusionList

	public void setExclusionList(ExclusionList el) {
		this.el = el;
	}

	public ExclusionList getExclusionList() {
		return el;
	}

	// set/get recent days

	public void setRecentDays(double min_recent, double max_recent) {
		if (min_recent < CopyPasteConfig.MIN_RECENT_DAYS ||
		max_recent > CopyPasteConfig.MAX_RECENT_DAYS ||
		min_recent >= max_recent)
			throw new IllegalArgumentException();

		this.min_recent = min_recent;
		this.max_recent = max_recent;
	}

	public double getMinRecentDays() {
		return min_recent;
	}

	public double getMaxRecentDays() {
		return max_recent;
	}
}
