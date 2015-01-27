import java.util.*;

interface A {}

public class t5<E> extends ArrayList<E> implements A {
	public t5() {
		super();
	}
	public t5(int x) {
		super(x);
	}
	public t5(Collection<? extends E> x) {
		super(x);
	}
}

class zzz {
	t5<Short> x = new t5<Short>();
	A y = new t5<Long>();
	void f() {
		x.add(null);
		x.size();
		for (Short s : x)
			;
	}
}
