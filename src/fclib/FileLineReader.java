package fclib;

import java.io.*;
import java.util.*;

// read the lines from a text file into an ArrayLlist

public class FileLineReader {
	private ArrayList<String> list;

	// do the actual line read

	private void readlines(BufferedReader br) throws IOException {
		assert br != null;

		list = new ArrayList<String>(1000);

		String ln;

		while ((ln = br.readLine()) != null)
			list.add(ln);

		list.trimToSize();
	}

	// constructor from standard input

	public FileLineReader() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);

		BufferedReader br = new BufferedReader(isr);

		readlines(br);

		br.close();
	}

	// constructor from a disk file

	public FileLineReader(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		FileReader fr = new FileReader(fn);

		BufferedReader br = new BufferedReader(fr);

		readlines(br);

		br.close();
	}

	// get size of the list

	public int size() {
		return list.size();
	}

	// get a string from the list

	public String get(int i) {
		return list.get(i);
	}

	// get the whole list

	public ArrayList<String> getList() {
		return list;
	}
}
