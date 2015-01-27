import fclib.*;
import java.util.*;
import java.io.*;

public class SplitCopyPaste {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("missing input file / output directory");
			System.exit(1);
		}

		FileLineReader flr = new FileLineReader(args[0]);
		ArrayList<String> list = flr.getList();

		int fnum = 0;

		FileLineWriter flw = null;

		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);

			if (s.indexOf("<<< ") == 0) {
				if (flw != null)
					flw.write();

				String fn = String.format("%s/%06d.txt",
					args[1], ++fnum);
				flw = new FileLineWriter(fn);
//???				flw.add(s);
			}

			else {
				flw.add(s);

				if (i + 1 == list.size()) {
					flw.write();
				}
			}
		}
	}
}
