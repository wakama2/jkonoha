package jkonoha;

public class KFloat extends KNumber {
	
	private final double value;
	
	public KFloat(double value) {
		this.value = value;
	}
	
	public static KFloat box(float n) {
		return new KFloat(n);
	}
	
	public static KFloat box(double n) {
		return new KFloat(n);
	}
	
	public double unbox() {
		return value;
	}
	
	public static String toString(double self) {
		return Double.toString(self);
	}
	
	@Override public String toString() {
		return toString(value);
	}

}
