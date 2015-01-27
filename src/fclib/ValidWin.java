package fclib;

import java.util.*;

// interface for callback objects used to validate windows of text

// returns true for valid windows

public interface ValidWin {
	boolean isValidWin(ArrayList<String> list, int startln, int numln);
}
