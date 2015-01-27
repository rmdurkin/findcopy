
class A {
	private A() {}

	public static A factory() {
		return new A();
	}
}

class B {
	void f() {
		A a = A.factory();
	}
}
