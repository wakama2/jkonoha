package jkonoha;

public class KSystem extends KObject {
	
	public static void _assert(boolean b) {
		if(!b) {
			throw new AssertionError();
		}
	}
	
	public static void p(KObject o) {
		System.out.println(o);
	}
	
	public static void gc() {
		System.gc();
	}

}
