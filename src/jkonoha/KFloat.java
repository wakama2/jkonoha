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
	
	public static double opPLUS(double self) {
		return +self;
	}
	
	public static double opMINUS(double self) {
		return -self;
	}
	
	public static double opADD(double self, double d) {
		return self + d;
	}
	
	public static double opSUB(double self, double d) {
		return self - d;
	}
	
	public static double opMUL(double self, double d) {
		return self * d;
	}
	
	public static double opDIV(double self, double d) {
		return self / d;
	}
	
	public static double opADD(double self, int d) {
		return self + d;
	}
	
	public static double opSUB(double self, int d) {
		return self - d;
	}
	
	public static double opMUL(double self, int d) {
		return self * d;
	}
	
	public static double opDIV(double self, int d) {
		return self / d;
	}	
	
	public static boolean opEQ(double self, double d) {
		return self == d;
	}
	
	public static boolean opNEQ(double self, double d) {
		return self != d;
	}
	
	public static boolean opLT(double self, double d) {
		return self < d;
	}
	
	public static boolean opLTE(double self, double d) {
		return self <= d;
	}
	
	public static boolean opGT(double self, double d) {
		return self > d;
	}
	
	public static boolean opGTE(double self, double d) {
		return self >= d;
	}
	
	public static String toString(double self) {
		return Double.toString(self);
	}
	
	@Override public String toString() {
		return toString(value);
	}

}
