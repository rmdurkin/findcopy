package fclib;

// DbmsInt configuration file

public class DbmsConfig {

	// no instances of this class can be created

	private DbmsConfig() {}

	// minimum/maximum timestamps

	// maximum range of timestamps in days, relative to current time

	static final int MAX_TS_DAYS = 100;

	// minimum/maximum number of owners

	static final int MINOWNERS = 10;
	static final int MAXOWNERS = 100;

	// minimum/maximum documents for an owner

	static final int MINDOCS = 25;
	static final int MAXDOCS = 250;

	// minimum/maximum number of global templates

	static final int MINTEMPLATES = 10;
	static final int MAXTEMPLATES = 50;

	// minimum/maximum lines for a global template

	static final int MINTEMPLATELINES = 10;
	static final int MAXTEMPLATELINES = 25;

	// probability that a global template is used

	static final double TEMPLATEPERC = 0.02;

	// minimum/maximum number of owner templates

	static final int MINOWNERTEMPLATES = 5;
	static final int MAXOWNERTEMPLATES = 25;

	// minimum/maximum lines for an owner template

	static final int MINOWNERTEMPLATELINES = 10;
	static final int MAXOWNERTEMPLATELINES = 25;

	// probabililty that an owner template is used

	static final double OWNERTEMPLATEPERC = 0.01;

	// mininum/maximum number of random lines

	static final int MINRANDOMLINES = 5;
	static final int MAXRANDOMLINES = 25;

	// number of distinct random lines

	static final int NUMRANDLINES = 25;
}
