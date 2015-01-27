import fclib.*;
import java.io.*;
import java.util.*;

public class MainReview {

	// window size

	private static final int WIN_SIZE = 20;

	// Console

	private static Console con = System.console();

	// current Comparator

	private static Comparator<CopyPasteEntry> currcmp = new s1();
	private static String currcmpname = "s1";

	// true if data has been modified

	private static boolean modflag = false;

	// x1/x2 flags

	private static boolean x1_flag = false;
	private static boolean x2_flag = false;

	// command-line arguments

	private static ArrayList<String> cmdargs;

	// information on currently loaded file

	private static String file_path;
	private static Date file_date;
	private static long file_length;
	private static ArrayList<CopyPasteEntry> cpelist;

	// information on current view

	private static int curr = -1;
	private static ArrayList<CopyPasteEntry> cpeview;

	// display a message

	private static void msg(String s) {
		assert s != null && s.length() >= 1;

		System.out.println("*** " + s + " ***");
	}

	// display an error

	private static void err(String s) {
		assert s != null && s.length() >= 1;

		msg("error: " + s);
	}

	// check whether a file is loaded

	private static boolean havefile() {
		return file_path != null;
	}

	// check whether there is a current view

	private static boolean haveview() {
		return curr != -1;
	}

	// update the view

	private static void updateview() {
		assert havefile();

		cpeview = new ArrayList<CopyPasteEntry>();

		for (CopyPasteEntry cpe : cpelist) {
			if (x1_flag && cpe.isBadAny())
				continue;

			if (x2_flag && cpe.getScore().isSet())
				continue;

			cpeview.add(cpe);
		}

		curr = (cpeview.size() >= 1 ? 1 : -1);

		Collections.sort(cpeview, currcmp);

		msg("view has " + cpeview.size() + " records");

	}

	// get the prompt

	private static String getprompt() {
		if (haveview())
			return String.format("%d/%d> ", curr, cpeview.size());
		else
			return "> ";
	}

	// add record to exclude file

	private static void do_exclude() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		CopyPasteEntry cpe = cpeview.get(curr - 1);

		boolean b = cpe.getAddExcl();
		b = !b;
		cpe.setAddExcl(b);

		msg("current value is: " + b);

		modflag = true;
	}

	// display file information

	private static void do_file() {
		if (!havefile()) {
			err("no current file");
			return;
		}

		System.out.println("path: " + file_path);
		System.out.println("length: " + file_length);
		System.out.println("last modified: " + file_date);
		System.out.println("total number of records: " +
			cpelist.size());
		System.out.println("number of records in view: " +
			(haveview() ? cpeview.size() : 0));
		System.out.println("sort name: " + currcmpname);
		System.out.println("x1 flag: " + x1_flag);
		System.out.println("x2 flag: " + x2_flag);
	}

	// display help information

	private static void do_help() {
		System.out.println("[global actions]");
		System.out.println("f   show current file / view information");
		System.out.println("h   display this message");
		System.out.println("l   list records");
		System.out.println("N   go to record N");
		System.out.println("n   next record");
		System.out.println("o   open a serialized file");
		System.out.println("p   previous record");
		System.out.println("q   quit");
		System.out.println("s   save to a serialized file");
		System.out.println("s1  sort order bad|info|numloc|elapsed");
		System.out.println("s2  sort order info|bad|numloc|elapsed");
		System.out.println("s3  sort order numloc|bad|info|elapsed");
		System.out.println("s4  sort order elapsed|bad|info|numloc");
		System.out.println("u   update the view");
		System.out.println("x1  toggle exclude bad records flag");
		System.out.println("x2  toggle exclude scored records flag");

		System.out.println();

		System.out.println("[actions on record N]");
		System.out.println("e   toggle add to exclude file flag");
		System.out.println("i   information for a record");
		System.out.println("r   record a score for a record");
		System.out.println("t   copy/paste text for a record");
	}

	// info for a record

	private static void do_info() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		String s = cpeview.get(curr - 1).getFormatted(false);
		s = UtilFuncs.eolToLineSep(s);

		System.out.print(s);
	}

	// list records

	private static void do_list() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		for (int i = curr; i < curr + WIN_SIZE &&
		i <= cpeview.size(); i++)
			System.out.println(i + ":" +
				cpeview.get(i - 1).getSummary());
	}

	// go to record N

	private static void do_n() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		int n;

		try {
			n = Integer.parseInt(cmdargs.get(0));
		}
		catch (NumberFormatException e) {
			err("invalid number");
			return;
		}

		if (n < 1 || n > cpeview.size()) {
			err("number out of range");
			return;
		}

		curr = n;
	}

	// go to next record

	private static void do_next() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		if (curr + 1 > cpeview.size()) {
			err("already at end of view");
			return;
		}

		curr++;
	}

	// open the contents of a file

	private static void do_open() {
		if (modflag) {
			err("unsaved data");
			return;
		}

		if (cmdargs.size() != 2) {
			err("missing pathname");
			return;
		}

		String fn = cmdargs.get(1);

		if (!UtilFuncs.isSerialFile(fn)) {
			err("invalid input file");
			return;
		}

		ArrayList<CopyPasteEntry> obj;

		try {
			obj = (ArrayList<CopyPasteEntry>)
				UtilFuncs.fileToObject(fn);
		}
		catch (Exception e) {
			System.out.println(e);
			err("could not open serialized input file");
			return;
		}

		File f = new File(fn);

		file_path = fn;
		file_length = f.length();
		file_date = new Date(f.lastModified());
		cpelist = obj;

		updateview();
	}

	// go to previous record

	private static void do_prev() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		if (curr == 1) {
			err("already at beginning of view");
			return;
		}

		curr--;
	}

	// quit

	private static void do_quit() {
		if (modflag) {
			err("unsaved data");
			return;
		}

		System.exit(0);
	}

	// record a score for a record

	private static void do_record() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		if (cmdargs.size() != 2) {
			err("missing score");
			return;
		}

		String s = cmdargs.get(1);

		if (!CopyPasteScore.isValidScore(s)) {
			err("invalid score");
			return;
		}

		cpeview.get(curr - 1).getScore().setScore(s);

		modflag = true;
	}

	// save to a file

	private static void do_save() {
		if (!havefile()) {
			err("no current file");
			return;
		}

		try {
			UtilFuncs.objectToFile(cpelist, file_path);
		}
		catch (Exception e) {
			err("could not save to file");
			return;
		}

		File f = new File(file_path);

		file_length = f.length();
		file_date = new Date(f.lastModified());

		msg("saved");

		modflag = false;
	}

	// set s1 sort order

	private static void do_s1() {
		currcmp = new s1();
		currcmpname = "s1";
	}

	// set s2 sort order

	private static void do_s2() {
		currcmp = new s2();
		currcmpname = "s2";
	}

	// set s3 sort order

	private static void do_s3() {
		currcmp = new s3();
		currcmpname = "s3";
	}

	// set s4 sort order

	private static void do_s4() {
		currcmp = new s4();
		currcmpname = "s4";
	}

	// display the text of a copy/paste instance

	private static void do_text() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		ArrayList<String> list = cpeview.get(curr - 1).getText();
		int totsize = list.size();

		int numout = 0;
		for (String s : list) {
			System.out.println(s);
			numout++;
			if (numout % WIN_SIZE == 0 && numout < totsize)
				con.readLine("press Enter to continue: ");
		}
	}

	// update the view

	private static void do_update() {
		if (!haveview()) {
			err("no current view");
			return;
		}

		updateview();
	}

	// toggle x1 flag

	private static void do_x1() {
		x1_flag = !x1_flag;

		msg("current value is: " + x1_flag);
	}

	// toggle x2 flag

	private static void do_x2() {
		x2_flag = !x2_flag;

		msg("current value is: " + x2_flag);
	}

	// command dispatch

	private static void dispatch() {
		if (cmdargs.size() == 0)
			return;

		String s = cmdargs.get(0);

		if (s.equals("e"))
			do_exclude();
		else if (s.equals("f"))
			do_file();
		else if (s.equals("h"))
			do_help();
		else if (s.equals("i"))
			do_info();
		else if (s.equals("l"))
			do_list();
		else if (Character.isDigit(s.charAt(0)))
			do_n();
		else if (s.equals("n"))
			do_next();
		else if (s.equals("o"))
			do_open();
		else if (s.equals("p"))
			do_prev();
		else if (s.equals("q"))
			do_quit();
		else if (s.equals("r"))
			do_record();
		else if (s.equals("s"))
			do_save();
		else if (s.equals("s1"))
			do_s1();
		else if (s.equals("s2"))
			do_s2();
		else if (s.equals("s3"))
			do_s3();
		else if (s.equals("s4"))
			do_s4();
		else if (s.equals("t"))
			do_text();
		else if (s.equals("u"))
			do_update();
		else if (s.equals("x1"))
			do_x1();
		else if (s.equals("x2"))
			do_x2();
		else
			err("invalid command");
	}

	public static void main(String[] args) {

		// main loop

		if (con == null) {
			System.err.println("*** no console ***");
			System.exit(1);
		}

		for (;;) {
			String input = con.readLine(getprompt());

			cmdargs = UtilFuncs.splitLine(input);

			dispatch();
		}
	}
}

class s1 implements Comparator<CopyPasteEntry> {

	// compare two CopyPasteEntry records

	public int compare(CopyPasteEntry cpe1, CopyPasteEntry cpe2) {
		if (cpe1 == null || cpe2 == null)
			throw new IllegalArgumentException();

		// records are identical

		if (cpe1 == cpe2)
			return 0;

		// bad signatures and other exclusions

		if (cpe1.isBadAny() != cpe2.isBadAny())
			return cpe2.isBadAny() ? -1 : 1;

		// information content

		double info = cpe1.getInfo() - cpe2.getInfo();
		if (info != 0.0)
			return info > 0.0 ? -1 : 1;

		// number of locations

		int locs = cpe1.getNumLoc() - cpe2.getNumLoc();
		if (locs != 0)
			return locs > 0 ? -1 : 1;

		// elapsed days between oldest and newest

		double elapsed = cpe1.getElapsedDays() - cpe2.getElapsedDays();
		if (elapsed != 0.0)
			return elapsed > 0.0 ? -1 : 1;

		return 0;
	}

	// not used

	public boolean equals(Object obj) {
		return false;
	}
}

class s2 implements Comparator<CopyPasteEntry> {

	// compare two CopyPasteEntry records

	public int compare(CopyPasteEntry cpe1, CopyPasteEntry cpe2) {
		if (cpe1 == null || cpe2 == null)
			throw new IllegalArgumentException();

		// records are identical

		if (cpe1 == cpe2)
			return 0;

		// information content

		double info = cpe1.getInfo() - cpe2.getInfo();
		if (info != 0.0)
			return info > 0.0 ? -1 : 1;

		// bad signatures and other exclusions

		if (cpe1.isBadAny() != cpe2.isBadAny())
			return cpe2.isBadAny() ? -1 : 1;

		// number of locations

		int locs = cpe1.getNumLoc() - cpe2.getNumLoc();
		if (locs != 0)
			return locs > 0 ? -1 : 1;

		// elapsed days between oldest and newest

		double elapsed = cpe1.getElapsedDays() - cpe2.getElapsedDays();
		if (elapsed != 0.0)
			return elapsed > 0.0 ? -1 : 1;

		return 0;
	}

	// not used

	public boolean equals(Object obj) {
		return false;
	}
}

class s3 implements Comparator<CopyPasteEntry> {

	// compare two CopyPasteEntry records

	public int compare(CopyPasteEntry cpe1, CopyPasteEntry cpe2) {
		if (cpe1 == null || cpe2 == null)
			throw new IllegalArgumentException();

		// records are identical

		if (cpe1 == cpe2)
			return 0;

		// number of locations

		int locs = cpe1.getNumLoc() - cpe2.getNumLoc();
		if (locs != 0)
			return locs > 0 ? -1 : 1;

		// bad signatures and other exclusions

		if (cpe1.isBadAny() != cpe2.isBadAny())
			return cpe2.isBadAny() ? -1 : 1;

		// information content

		double info = cpe1.getInfo() - cpe2.getInfo();
		if (info != 0.0)
			return info > 0.0 ? -1 : 1;

		// elapsed days between oldest and newest

		double elapsed = cpe1.getElapsedDays() - cpe2.getElapsedDays();
		if (elapsed != 0.0)
			return elapsed > 0.0 ? -1 : 1;

		return 0;
	}

	// not used

	public boolean equals(Object obj) {
		return false;
	}
}

class s4 implements Comparator<CopyPasteEntry> {

	// compare two CopyPasteEntry records

	public int compare(CopyPasteEntry cpe1, CopyPasteEntry cpe2) {
		if (cpe1 == null || cpe2 == null)
			throw new IllegalArgumentException();

		// records are identical

		if (cpe1 == cpe2)
			return 0;

		// elapsed days between oldest and newest

		double elapsed = cpe1.getElapsedDays() - cpe2.getElapsedDays();
		if (elapsed != 0.0)
			return elapsed > 0.0 ? -1 : 1;

		// bad signatures and other exclusions

		if (cpe1.isBadAny() != cpe2.isBadAny())
			return cpe2.isBadAny() ? -1 : 1;

		// information content

		double info = cpe1.getInfo() - cpe2.getInfo();
		if (info != 0.0)
			return info > 0.0 ? -1 : 1;

		// number of locations

		int locs = cpe1.getNumLoc() - cpe2.getNumLoc();
		if (locs != 0)
			return locs > 0 ? -1 : 1;

		return 0;
	}

	// not used

	public boolean equals(Object obj) {
		return false;
	}
}
