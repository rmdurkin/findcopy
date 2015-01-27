package fclib;

import java.util.*;

// interface for text preprocessing methods

public interface PrepText {

	// preprocess a line of text

	String prepLine(String s);

	// get the ID describing the preprocessing scheme

	String getPrepName();

	// preprocess a list of strings

	ArrayList<String> prepList(ArrayList<String> list,
	int startln, int numln);

	ArrayList<String> prepList(ArrayList<String> list);

	// preprocess a WinLoc

	ArrayList<String> prepWinLoc(WinLoc loc);
}
