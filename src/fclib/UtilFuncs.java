package fclib;

import java.io.*;
import java.util.*;

// utility functions

public class UtilFuncs {

	// no instances of this class can be created

	private UtilFuncs() {}

	// get a list of pathnames from the command line or standard input

	public static ArrayList<String> getFiles(String[] args) {
		if (args == null)
			throw new IllegalArgumentException();

		ArrayList<String> filelist;

		// get filenames from args[]

		if (args.length >= 1) {
			filelist = new DirWalk(args).getPaths();
		}

		// or from standard input

		else {
			try {
				filelist = new FileLineReader().getList();
			}
			catch (IOException e) {
				throw new FindCopyException("getFiles");
			}
		}

		return filelist;
	}

	// convert a byte array to a hex string

	public static String byteToHex(byte[] buf) {
		if (buf == null || buf.length == 0)
			throw new IllegalArgumentException();

		StringBuilder sb = new StringBuilder(buf.length * 2);

		for (int i = 0; i < buf.length; i++) {
			sb.append(Character.forDigit((buf[i] >>> 4) & 0xf, 16));
			sb.append(Character.forDigit((buf[i] >>> 0) & 0xf, 16));
		}

		return sb.toString();
	}

	// convert a hex string to a byte array

	public static byte[] hexToByte(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		int len = s.length();
		if (len < 1 || len % 2 != 0)
			throw new IllegalArgumentException();

		byte[] out = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			int d0 = Character.digit(s.charAt(i + 0), 16);
			int d1 = Character.digit(s.charAt(i + 1), 16);
			if (d0 == -1 || d1 == -1)
				throw new FindCopyException("hexToByte");
			out[i / 2] = (byte)((d0 << 4) | d1);
		}

		return out;
	}

	// verify that two WinLocs refer to the same text

	public static boolean verifyWindow(WinLoc loc1, WinLoc loc2) {
		if (loc1 == null || loc2 == null)
			throw new IllegalArgumentException();

		// check number of lines in window

		if (loc1.getNumln() != loc2.getNumln())
			return false;

		// get the lines for each WinLoc

		ArrayList<String> lines1 = loc1.getDocAttr().getLines();
		ArrayList<String> lines2 = loc2.getDocAttr().getLines();

		// get the base lines

		int base1 = loc1.getStartln();
		int base2 = loc2.getStartln();

		// iterate across each window

		int cnt = loc1.getNumln();

		for (int i = 0; i < cnt; i++) {

			// compute the current line in each window

			int curr1 = base1 + i;
			int curr2 = base2 + i;

			// if current line too high, windows don't match

			if (curr1 >= lines1.size() || curr2 >= lines2.size())
				return false;

			// compare the actual lines

			String s1 = Config.PT.prepLine(lines1.get(curr1));
			String s2 = Config.PT.prepLine(lines2.get(curr2));
			if (!s1.equals(s2))
				return false;
		}

		return true;
	}

	// split a string into separate lines

	public static ArrayList<String> stringToLines(String s, char sep) {
		if (s == null)
			throw new IllegalArgumentException();

		ArrayList<String> out = new ArrayList<String>();

		int len = s.length();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);

			if (c == sep) {
				out.add(sb.toString());
				sb = new StringBuilder();
			}

			else if (i + 1 == len) {
				sb.append(c);
				out.add(sb.toString());
			}

			else {
				sb.append(c);
			}
		}

		out.trimToSize();

		return out;
	}

	// split a string into whitespace-delimited tokens

	public static ArrayList<String> splitLine(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		StringTokenizer st = new StringTokenizer(s);
		ArrayList<String> out = new ArrayList<String>();

		while (st.hasMoreTokens())
			out.add(st.nextToken());

		return out;
	}

	// read a text file into a single string,
	// with each line delimited by Constants.EOL_CHAR

	public static String fileToString(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		FileReader fr = new FileReader(fn);
		BufferedReader br = new BufferedReader(fr);

		StringBuilder sb = new StringBuilder();

		String ln;

		while ((ln = br.readLine()) != null) {
			sb.append(ln);
			sb.append(Constants.EOL_CHAR);
		}

		br.close();

		return sb.toString();
	}

	// convert EOL_CHARs in a String to sequences
	// suitable for doing I/O on a given platform

	public static String eolToLineSep(String s) {
		if (s == null)
			throw new IllegalArgumentException();

		int len = s.length();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c == Constants.EOL_CHAR)
				sb.append(Constants.IO_SEP);
			else
				sb.append(c);
		}

		return sb.toString();
	}

	// get the settings pertinent to a signature database

	public static String getSettings() {
		StringBuilder sb = new StringBuilder();
		final String SEP = " ";

		// hash algorithm

		sb.append("HASH_ALG=");
		sb.append(Constants.HASH_ALG);
		sb.append(SEP);

		// window sizes

		sb.append("WIN_SIZE=");
		for (int i = 0; i < Config.WIN_SIZE.length; i++) {
			if (i >= 1)
				sb.append(',');
			sb.append(Config.WIN_SIZE[i]);
		}
		sb.append(SEP);

		// EOL setting

		sb.append("EOL=");
		for (int i = 0; i < Constants.EOL.length(); i++) {
			if (i >= 1)
				sb.append(',');
			sb.append((int)Constants.EOL.charAt(i));
		}
		sb.append(SEP);

		// prep method setting

		sb.append("PREP_NAME=");
		sb.append(Config.PT.getPrepName());
		sb.append(SEP);

		// program version

		sb.append("PROG_VERSION=");
		sb.append(Config.PROG_VERSION);

		// return

		return sb.toString();
	}

	// get the number of days between a timestamp and the current time

	public static double getElapsedTime(long ts) {
		if (ts < 0)
			throw new IllegalArgumentException();

		long diff = System.currentTimeMillis() - ts;

		return (double)diff / Constants.MILLISEC_PER_DAY;
	}

	public static double getElapsedTime(Date d) {
		if (d == null)
			throw new IllegalArgumentException();

		return getElapsedTime(d.getTime());
	}

	// get the number of days since a file was last modified

	public static double getFileAgeDays(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		File f = new File(fn);

		long mod = f.lastModified();

		double d = getElapsedTime(mod);

		if (mod == 0 || d < 0)
			throw new IOException();

		return d;
	}

	// open a PrintWriter file object

	public static PrintWriter getPrintWriter(String fn) throws IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		FileWriter fw = new FileWriter(fn);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);

		return pw;
	}

	// create a temporary file

	public static String createTempFile() throws IOException {
		File dir = new File(Config.TMP_DIR);
		File out = File.createTempFile("tmp", null, dir);
		return out.getPath();
	}

	// serialization header

	private static final byte[] SERIAL_HEADER = new byte[]{
		91,
		125,
		-105,
		60,
		-22,
		-35,
		-74,
		46,
		-15,
		-118,
		46,
		113,
		-87,
		-68,
		-105,
		25
	};

	// header length

	private static final int SH_LEN = SERIAL_HEADER.length;

	// check whether header is okay

	private static boolean is_valid_header(BufferedInputStream bis) {
		assert bis != null;

		boolean isvalid = true;

		try {
			byte[] header = new byte[SH_LEN];
			bis.read(header, 0, SH_LEN);
			for (int i = 0; i < SH_LEN; i++) {
				if (header[i] != SERIAL_HEADER[i]) {
					isvalid = false;
					break;
				}
			}
		}
		catch (IOException e) {
			isvalid = false;
		}

		return isvalid;
	}

	// serialize an object to a file

	public static void objectToFile(Object obj, String fn)
	throws IOException {
		if (obj == null || fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		// open the output file

		FileOutputStream fos = new FileOutputStream(fn);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		// write the header

		bos.write(SERIAL_HEADER, 0, SH_LEN);

		// write the object

		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);

		// close the output

		oos.close();
	}

	// deserialize a file to an object

	public static Object fileToObject(String fn)
	throws ClassNotFoundException, IOException {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		// open the input file

		FileInputStream fis = new FileInputStream(fn);
		BufferedInputStream bis = new BufferedInputStream(fis);

		// check the header

		if (!is_valid_header(bis))
			throw new FindCopyException("fileToObject");

		// read the object

		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();

		// close the input file

		ois.close();

		return obj;
	}

	// serialize an object to a byte array

	public static byte[] objectToArray(Object obj) throws IOException {
		if (obj == null)
			throw new IllegalArgumentException();

		// open the output stream

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);

		// write the header

		bos.write(SERIAL_HEADER, 0, SH_LEN);

		// write the object

		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);

		// close the output stream

		oos.close();

		return baos.toByteArray();
	}

	// deserialize a byte array to an object

	public static Object arrayToObject(byte[] buf)
	throws ClassNotFoundException, IOException {
		if (buf == null)
			throw new IllegalArgumentException();

		// open the input stream

		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		BufferedInputStream bis = new BufferedInputStream(bais);

		// check the header

		if (!is_valid_header(bis))
			throw new FindCopyException("arrayToObject");

		// read the object

		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();

		// close the input stream

		ois.close();

		return obj;
	}

	// check whether a file has a valid serialization stream in it

	public static boolean isSerialFile(String fn) {
		if (fn == null || fn.length() == 0)
			throw new IllegalArgumentException();

		try {
			FileInputStream fis = new FileInputStream(fn);
			BufferedInputStream bis = new BufferedInputStream(fis);
			boolean isvalid = is_valid_header(bis);
			bis.close();
			return isvalid;
		}
		catch (IOException e) {
			return false;
		}
	}

	// check whether an array has a valid serialization stream in it

	public static boolean isSerialArray(byte[] b) {
		if (b == null)
			throw new IllegalArgumentException();

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			BufferedInputStream bis = new BufferedInputStream(bais);
			boolean isvalid = is_valid_header(bis);
			bis.close();
			return isvalid;
		}
		catch (IOException e) {
			return false;
		}
	}
}
