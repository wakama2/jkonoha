package jkonoha;

public class KInt extends KNumber {
	
	private final int value;
	
	public KInt(int value) {
		this.value = value;
	}
	
	public static KInt box(int n) {
		return new KInt(n);
	}
	
	public static KInt box(long n) {
		return new KInt((int)n);
	}
	
	public int unbox() {
		return value;
	}
	
	public static int opPLUS(int self) {
		return self;
	}
	
	public static int opMINUS(int self) {
		return -self;
	}
	
	public static int opADD(int self, int n) {
		return self + n;
	}
	
	public static int opSUB(int self, int n) {
		return self - n;
	}
	
	public static int opMUL(int self, int n) {
		return self * n;
	}
	
	public static int opDIV(int self, int n) {
		return self / n;
	}
	
	public static int opMOD(int self, int n) {
		return self % n;
	}
	
	public static boolean opEQ(int self, int n) {
		return self == n;
	}
	
	public static boolean opNEQ(int self, int n) {
		return self != n;
	}
	
	public static boolean opLT(int self, int n) {
		return self < n;
	}
	
	public static boolean opLTE(int self, int n) {
		return self <= n;
	}
	
	public static boolean opGT(int self, int n) {
		return self > n;
	}
	
	public static boolean opGTE(int self, int n) {
		return self >= n;
	}
	
	public static int opLSHIFT(int self, int n) {
		return self << n;
	}
	
	public static int opRSHIFT(int self, int n) {
		return self >> n;
	}
	
	public static String opADD(int self, String s) {
		return self + s;
	}
	
	public static String toString(int self) {
		return Integer.toString(self);
	}
	
	@Override public String toString() {
		return toString(value);
	}
	
}
