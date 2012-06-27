package jkonoha;

import jkonoha.ast.KFLAG;

public class SYM {
	public static int HEAD (int sym) {
		return (sym  & (KFLAG.H0|KFLAG.H1|KFLAG.H2));
	}

	public static int UNMASK (int sym) {
		return (sym & (~(KFLAG.H0|KFLAG.H1|KFLAG.H2)));
	}
	
	/*kinline*/ public static void SYM_s(CTX ctx, int sym)
	{
		int index = (sym & (~(KFLAG.H0|KFLAG.H1|KFLAG.H2)));
		//assert(index < ctx.share.unameList.size());
//		return ctx.share.unameList.get(index);
	}
	public static int NONAME = -1;
	public static int NEWID = -2;
}
