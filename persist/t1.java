interface A {
	void f();
}

class B implements A {
	public void f() {}
}

public class t1 {
	void f() throws Exception {
		A a = B.class.newInstance();
		a.f();
	}
}
