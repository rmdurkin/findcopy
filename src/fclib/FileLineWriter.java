package fclib;

import java.io.*;
import java.util.*;

// accmulate and then write the lines from a list to a text file

public class FileLineWriter {
	private String fn;

	private ArrayList<String> list;

	// constructor

	public FileLineWriter(String fn, ArrayList<String> list) {
		if (fn == null || fn.length() == 0 || list == null)
			throw new IllegalArgumentException();

		this.fn = fn;

		this.list = list;
	}

	public FileLineWriter(String fn) {
		this(fn, new ArrayList<String>());
	}

	// add a string to the list

	public void add(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		list.add(s);
	}

	// get the list size

	public int size() {
		return list.size();
	}

	// get the i-th string from the list

	public String get(int i) {
		if (i < 0 || i >= size())
			throw new IllegalArgumentException();

		return list.get(i);
	}

	// write the list of strings

	public void write() throws IOException {
		FileWriter fw = new FileWriter(fn);
		BufferedWriter bw = new BufferedWriter(fw);

		for (String s : list) {
			bw.write(s, 0, s.length());
			bw.newLine();
		}

		bw.close();
	}
}
