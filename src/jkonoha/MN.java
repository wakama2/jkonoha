package jkonoha;

import jkonoha.ast.KFLAG;

public class MN {
	public static int ISBOOL = KFLAG.H0;//Joseph
	public static int GETTER = KFLAG.H1;
	public static int SETTER = KFLAG.H2;
	public static int Annotation = (KFLAG.H1|KFLAG.H2);

	public static int TOCID = (KFLAG.H0|KFLAG.H1);
	public static int ASCID = (KFLAG.H0|KFLAG.H1|KFLAG.H2);

	static boolean isISBOOL(int mn) {
		return (SYM.HEAD(mn) == ISBOOL);
	}
	static int toISBOOL(int mn) {
		return ((SYM.UNMASK(mn)) | ISBOOL);
	}
	static boolean MN_isGETTER(int mn) {
		return (SYM.HEAD(mn) == GETTER);
	}
	
	static int toGETTER(int mn) {
		return ((SYM.UNMASK(mn)) | GETTER);
	}
	
	static boolean isSETTER(int mn) {
		return (SYM.HEAD(mn) == SETTER);
	}
	
	static int toSETTER(int mn) {
		return ((SYM.UNMASK(mn)) | SETTER);
	}

	static int to(int cid) {
		return (cid | TOCID);
	}
	
	static boolean isTOCID(int mn) {
		return ((SYM.UNMASK(mn)) == TOCID);
	}
	
	static int as(int cid) {
		return (cid | ASCID);
	}
	
	static boolean isASCID(int mn) {
		return ((SYM.UNMASK(mn)) == ASCID);
	}
}
