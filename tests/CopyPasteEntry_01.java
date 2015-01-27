import fclib.*;

import java.util.*;

public class CopyPasteEntry_01 {

	private static void test01() {
		CopyPasteScore cps = new CopyPasteScore();

		CryptoHash h = new CryptoHash(Constants.HASH_ALG);
		h.addLine("zzz");
		WinSig ws = h.getWinSig();

		DocAttr da1 = new DocAttrRaw("1", "2", "zzz", new Date(0));
		DocAttr da2 = new DocAttrRaw("1", "3", "zzz",
			new Date(Constants.MILLISEC_PER_DAY));

		WinLoc loc1 = new WinLoc(da1, 0, 1);
		WinLoc loc2 = new WinLoc(da2, 0, 1);

		ArrayList<WinLoc> list1 = new ArrayList<WinLoc>();
		list1.add(loc1);

		CopyPasteEntry cpe1 = new CopyPasteEntry(
			ws, list1, false, false, false, 1, 2, cps, false);
		assert cpe1.getOwner().equals("1");
		assert cpe1.getId(0).equals("2");
		assert cpe1.getWinLoc(0).equals(loc1);
		assert cpe1.getWinSig() == ws;
		assert cpe1.getNumLoc() == 1;
		assert cpe1.getWinLocs() == list1;
		assert cpe1.getLineInfo() == 1;
		assert cpe1.getWordInfo() == 2;
		assert cpe1.getInfo() == 1.5;
		assert cpe1.isBadSig() == false;
		assert cpe1.isBadSyn() == false;
		assert cpe1.isBadExcl() == false;
		assert cpe1.isBadAny() == false;
		assert cpe1.getElapsedDays() == 0;
		ArrayList<String> out1 = cpe1.getText();
		assert out1.size() == 1 && out1.get(0).equals("zzz");
		assert !cpe1.getScore().isSet();
		cpe1.getScore().setScore("1A");
		assert cpe1.getScore().isSet();
		assert cpe1.getScore().toString().equals("1A");
		assert cpe1.getScore().getRisk() == '1';
		assert cpe1.getScore().getDup() == 'A';
		assert !cpe1.getAddExcl();
		cpe1.setAddExcl(true);
		assert cpe1.getAddExcl();
		cpe1.setAddExcl(false);
		assert !cpe1.getAddExcl();

		list1.add(loc2);

		CopyPasteEntry cpe2 = new CopyPasteEntry(
			ws, list1, true, true, true, 1.5, 2, cps, false);

		assert cpe2.getOwner().equals("1");
		assert cpe2.getId(0).equals("2");
		assert cpe2.getId(1).equals("3");
		assert cpe2.getWinLoc(0).equals(loc1);
		assert cpe2.getWinLoc(1).equals(loc2);
		assert cpe2.getWinSig() == ws;
		assert cpe2.getNumLoc() == 2;
		assert cpe2.getWinLocs() == list1;
		assert cpe2.getLineInfo() == 1.5;
		assert cpe2.getWordInfo() == 2;
		assert cpe2.getInfo() == 1.75;
		assert cpe2.isBadSig() == true;
		assert cpe2.isBadSyn() == true;
		assert cpe2.isBadExcl() == true;
		assert cpe2.isBadAny() == true;
		assert cpe2.getElapsedDays() == 1;
		ArrayList<String> out2 = cpe2.getText();
		assert out2.size() == 1 && out2.get(0).equals("zzz");
		assert cpe2.getScore().isSet();

		assert cpe1.compareTo(cpe1) == 0;
		assert cpe1.compareTo(cpe2) == -1;
		assert cpe2.compareTo(cpe1) == 1;
		assert cpe2.compareTo(cpe2) == 0;

		CopyPasteEntry cpe3 = new CopyPasteEntry(
			ws, list1, true, false, true, 1.5, 2, cps, false);
		assert cpe3.isBadAny() == true;

		CopyPasteEntry cpe4 = new CopyPasteEntry(
			ws, list1, false, false, true, 1, 2, cps, false);
		assert cpe4.isBadAny() == true;

		CopyPasteEntry cpe5 =
			new CopyPasteEntry(ws, list1, true, true, true,
			Constants.BAD_WEIGHT, Constants.BAD_WEIGHT, cps, false);
		assert cpe5.getInfo() == Constants.BAD_WEIGHT;

		assert cpe2.compareTo(cpe5) == -1;
		assert cpe5.compareTo(cpe2) == 1;

		CopyPasteEntry cpe6 = new CopyPasteEntry(
			ws, list1, true, true, true, 1.51, 2, cps, false);

		assert cpe2.compareTo(cpe6) == 1;
		assert cpe6.compareTo(cpe2) == -1;

		ArrayList<WinLoc> list2 = new ArrayList<WinLoc>();
		ArrayList<WinLoc> list3 = new ArrayList<WinLoc>();
		list2.add(loc1);
		list3.add(loc1);
		list3.add(loc2);

		CopyPasteEntry cpe7 = new CopyPasteEntry(
			ws, list2, true, true, true, 1.5, 2, cps, false);
		CopyPasteEntry cpe8 = new CopyPasteEntry(
			ws, list3, true, true, true, 1.5, 2, cps, false);

		assert cpe8.compareTo(cpe7) == -1;
		assert cpe7.compareTo(cpe8) == 1;
	}

	public static void main(String[] args) {
		test01();
	}
}
