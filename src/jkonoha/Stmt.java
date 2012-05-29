package jkonoha;

import jkonoha.*;
import jkonoha.compiler.kobject.KToken;

public class Stmt {
	public int uline;
	public Syntax syntax;
	public Block parentNULL;
	
	public final int build;  // TSTMT.XXXX
	public Map<String, Object> proto;
	
	public Stmt(int build) {
		this.build = build;
	}
	
	// src/sugar/struct.h:930
	public Block getBlock(CTX ctx, int kw, Block def) {
		Object bk = this.getObject(kw);
		if(bk != null) {
			if(bk instanceof Block) {
				return (Block)bk;
			}
			if(bk instanceof KToken) {
				//TODO
			}
		}
		return def;
	}
	
	public String getText(CTX ctx, int kw, String def) {
		return (String)getObject(kw);
	}
	
	public Expr getExpr(CTX ctx, int kw, Expr def) {
		return (Expr)getObject(kw);
	}
}
