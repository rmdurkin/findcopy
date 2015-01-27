import java.util.*;

class A implements PersistObject {

	private int f1 = 123;

	private long f2 = 456;

	private double f3 = 789.101112;

	private boolean f4 = true;

	private boolean f5 = false;

	private Date f6 = new Date(123456);

	private String f7 = "abc";

	public A() {}

	public A(PersistReader pr) {
		f1 = pr.readInt();
		f2 = pr.readLong();
		f3 = pr.readDouble();
		f4 = pr.readBoolean();
		f5 = pr.readBoolean();
		f6 = pr.readDate();
		f7 = pr.readString();
	}

	public void writePersist(PersistWriter pw) {
		pw.writeInt(f1);
		pw.writeLong(f2);
		pw.writeDouble(f3);
		pw.writeBoolean(f4);
		pw.writeBoolean(f5);
		pw.writeDate(f6);
		pw.writeString(f7);
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof A))
			return false;

		A other = (A)obj;

		if (this == other)
			return true;

		if (f1 != other.f1)
			return false;

		if (f2 != other.f2)
			return false;

		if (f3 != other.f3)
			return false;

		if (f4 != other.f4)
			return false;

		if (f5 != other.f5)
			return false;

		if (!f6.equals(other.f6))
			return false;

		if (!f7.equals(other.f7))
			return false;

		return true;
	}
}

public class test1 {
	public static void main(String[] args) {
		A a1 = new A();
		PersistWriter pw = new PersistWriter();
		a1.writePersist(pw);
		String persist = pw.toString();

//System.out.println(persist);

		PersistReader pr = new PersistReader(persist);
		A a2 = new A(pr);
		assert a2.equals(a1);
	}
}
