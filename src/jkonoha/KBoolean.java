package jkonoha;


public class KBoolean extends KObject {
	
	private final boolean value;
	
	public KBoolean(boolean value) {
		this.value = value;
	}
	
	public static KBoolean box(boolean b) {
		return new KBoolean(b);
	}
	
	public boolean unbox() {
		return value;
	}
	
	public static boolean opNOT(boolean self) {
		return !self;
	}
	
	public static String toString(boolean self) {
		return Boolean.toString(self);
	}
	
	@Override public String toString() {
		return toString(value);
	}
	
}
