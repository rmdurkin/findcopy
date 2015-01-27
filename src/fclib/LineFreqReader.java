package fclib;

import java.io.*;
import java.util.*;

// read in a file prepared by the LineFreq class

public class LineFreqReader {

	// number of line occurrences

	private int numocc;

	// number of documents

	private int numdoc;

	// average weight of a line

	private double avgwt;

	// hash table to map lines to weights

	private LinearHash<String, Double> table =
		new LinearHash<String, Double>();

	// constructor

	public LineFreqReader(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		// read in the file

		FileLineReader flr = new FileLineReader(fn);
		assert flr.size() >= 4;

		// check the settings line

		assert flr.get(0).equals(UtilFuncs.getSettings());

		// read the header

		numocc = Integer.parseInt(flr.get(1));
		numdoc = Integer.parseInt(flr.get(2));
		avgwt = Double.parseDouble(flr.get(3));
		assert numocc >= 0 && numdoc >= 0 && avgwt >= 0;

		// read the lines and corresponding weights

		for (int i = 4; i < flr.size(); i += 2) {
			String key = flr.get(i);
			assert key != null && key.length() != 0;
			double wt = Double.parseDouble(flr.get(i + 1));
			assert wt > 0;
			table.put(key, new Double(wt));
		}
	}

	// find a line's weight, and return average weight if not foudn

	public double getWeight(String line) {
		if (line == null || line.length() == 0)
			throw new IllegalArgumentException();

		Double d = table.get(line);

		return (d == null ? avgwt : d.doubleValue());
	}

	// find the average weight for a sequence of lines

	public double getWeight(ArrayList<String> list,
	int startln, int numln) {
		if (list == null)
			throw new IllegalArgumentException();

		if (startln < 0 || numln < 0)
			throw new IllegalArgumentException();

		if (startln + numln - 1 >= list.size())
			throw new IllegalArgumentException();

		double sum = 0;
		int cnt = 0;

		for (int i = startln; i < startln + numln; i++) {
			String s = list.get(i);
			if (s != null && s.length() != 0) {
				sum += getWeight(list.get(i));
				cnt++;
			}
		}

		return (cnt == 0 ? 0 : sum / cnt);
	}

	// get the average weight of a whole list of lines

	public double getWeight(ArrayList<String> list) {
		if (list == null)
			throw new IllegalArgumentException();

		return getWeight(list, 0, list.size());
	}

	// return a weight divided by the average weight

	public double getScaledWeight(double w) {
		assert w >= 0;
		return (avgwt == 0 ? 0 : w / avgwt);
	}

	// get the scaled weight for the whole list

	public double getScaledWeight(ArrayList<String> list) {
		if (list == null)
			throw new IllegalArgumentException();

		return getScaledWeight(getWeight(list));
	}
}
