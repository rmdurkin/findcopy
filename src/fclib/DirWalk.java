package fclib;

import java.io.*;
import java.util.*;

// walk through a directory and make a list of pathnames

public class DirWalk {
	private ArrayList<String> out = new ArrayList<String>();

	// process one File object and recursively expand if a directory

	private void walk(File f) {
		if (f.isDirectory()) {
			String[] list = f.list();
			if (list != null) {
				for (int i = 0; i < list.length; i++)
					walk(new File(f, list[i]));
			}
		}
		else if (f.exists()) {
			out.add(f.getPath());
		}
	}

	// process one string name

	private void go(String fn) {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		walk(new File(fn));
	}

	// constructor from a single filename

	public DirWalk(String fn) {
		go(fn);

		out.trimToSize();

		Collections.sort(out);
	}

	// constructor from a list of filenames

	public DirWalk(String[] args) {
		for (int i = 0; i < args.length; i++)
			go(args[i]);

		out.trimToSize();

		Collections.sort(out);
	}

	// return a list of the pathnames

	public ArrayList<String> getPaths() {
		return out;
	}
}
