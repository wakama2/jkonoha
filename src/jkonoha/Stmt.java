package jkonoha;

public class Stmt extends KObject {
	public int uline;
	public Syntax syntax;
	public Block parentNULL;
	public int build;  // TSTMT.XXXX
	
	public Stmt(int build) {
		this.build = build;
	}
	
	// src/sugar/struct.h:930
	public Block getBlock(CTX ctx, String kw, Block def) {
		Object bk = this.getObject(kw);
		if(bk != null) {
			if(bk instanceof Block) {
				return (Block)bk;
			}
			if(bk instanceof Token) {
				//TODO
			}
		}
		return def;
	}
	
	public String getText(CTX ctx, String kw, String def) {
		return (String)getObject(kw);
	}
	
	public Expr getExpr(CTX ctx, String kw, Expr def) {
		return (Expr)getObject(kw);
	}
}
