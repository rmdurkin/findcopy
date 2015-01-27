import java.io.*;
import java.util.*;

// this program displays a list of hit counts and owner
// counts and owners, and writes a bad signature database

public class t2 {
	public static void main(String[] args) throws IOException {
		String fn = args[0];

		FindCopy fc = new FindCopy(Config.WINSIZE,
			Config.VW, Config.VSWLOWN);		

		DocAttr da = new DocAttrFile(fn);

		for (int i = 1; i <= 2000; i++)
			fc.addDocument(da);
	}
}
