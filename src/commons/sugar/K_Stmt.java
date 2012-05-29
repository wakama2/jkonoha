package commons.sugar;

import commons.konoha2.kclass.K_Object;


public class K_Stmt extends K_Object {
	int uline;
	KSyntax syn;
	K_Block parentNULL;
	int build;

	public void setSyn(KSyntax syn) { //inline
		//		if(syn == NULL && stmt->syn != NULL) {
		//		DBG_P("DONE: STMT='%s'", T_kw(syn->kw));
		//	    }
		this.syn = syn;
	}

	public void typed(int build) { //inline
		this.build = build;
	}
}
