package jkonoha.ast;

public class KFLAG {
	private static int KFLAG_H (int N){
		return Integer.SIZE * 8 - N;
	}
	
	public static int H0 = 1 << KFLAG_H(1);
	public static int H1 = 1 << KFLAG_H(2);
	public static int H2 = 1 << KFLAG_H(3);
	public static int H3 = 1 << KFLAG_H(4);
	public static int H4 = 1 << KFLAG_H(5);
	public static int H5 = 1 << KFLAG_H(6);
	public static int H6 = 1 << KFLAG_H(7);
	public static int H7 = 1 << KFLAG_H(8);
}
