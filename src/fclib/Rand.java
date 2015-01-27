package fclib;

import java.util.*;

// random number wrapper class

public class Rand extends java.util.Random {

	// get a random number seed with a specified number of bits

	public static int getSeed(int nb) {
		if (nb < 1 || nb > 32)
			throw new IllegalArgumentException();

		return (int)(System.currentTimeMillis() & ((1L << nb) - 1L));
	}

	// get a random number seed

	public static int getSeed() {
		return getSeed(31);
	}

	// default constructor

	public Rand() {
		super();
	}

	// constructor from a seed

	public Rand(long s) {
		super(s);
	}

	// generate a random value in an inclusive range

	public int getRange(int lo, int hi) {
		if (lo > hi)
			throw new IllegalArgumentException();

		int r = hi - lo + 1;

		int ret = lo + (int)(nextDouble() * r);

		assert ret >= lo && ret <= hi;

		return ret;
	}

	// return true a certain percentage of the time

	public boolean getPerc(int perc) {
		if (perc <= 0)
			return false;

		if (perc >= 100)
			return true;

		return getRange(1, 100) <= perc;
	}

	// returrn true a certain percentage of the time

	public boolean getPerc(double perc) {
		if (perc <= 0.0)
			return false;

		if (perc >= 1.0)
			return true;

		return nextDouble() <= perc;
	}
}
