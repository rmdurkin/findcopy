
interface A {
	public void B();
}

class B implements A {
	public void B() {}
	public B() {}
}

class C {
	B b = new B();
	void f() {
		b.B();
	}
}
