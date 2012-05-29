package commons.sugar;

import sugar.K_Token;
import commons.konoha2.kclass.*;

public class K_Expr extends K_Object {
	int ty; int build;
	K_Token tk;     // Term
	// union-------------
	K_Object data;
	K_Array<K_Object> cons;  // Cons
	K_Expr single;
	K_Block block;
	// ---------------------
	// union-------------
	KSyntax syn;
	int ivalue;
	double fvalue;
	int ndata;
	int index;
	int cid;
	//		uintptr_t	   mn;
	// ---------------------
	
	public void typeid(int build, int ty) { // inline
		this.build = build;
		this.ty = ty;
	}
}
