import java.util.*;

interface A {
	void f();
}

class B implements A {
	public void f() {}
	public void g() {}
}

class C {
	void f(ArrayList<? extends A> a) {
		a.get(0).f();
//		a.get(0).g();
	}

	void g() {
		ArrayList<A> x = new ArrayList<A>();
		f(x);
		x.get(0).f();

		ArrayList<B> y = new ArrayList<B>();
		f(y);
		y.get(0).f();
		y.get(0).g();
	}
}
