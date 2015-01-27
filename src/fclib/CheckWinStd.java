package fclib;

import java.util.*;

// validate a window of lines

public class CheckWinStd implements ValidWin {
	public boolean isValidWin(ArrayList<String> list,
	int startln, int numln) {
		assert list != null && startln >= 0 && numln >= 1;

		return numln >= Config.MIN_WIN_CALLBACK;
	}
}
