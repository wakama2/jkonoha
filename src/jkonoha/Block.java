package jkonoha;

import java.util.*;

public class Block extends KObject {
	public KonohaSpace ks;
	public Stmt parentNULL;
	public final List<Stmt> blocks = new ArrayList<Stmt>();
	public Expr esp;  // BlockScopeVariable() to record maximun used stack
	
	public Block(CTX ctx, KonohaSpace ks) {
		this.ks = ks != null ? ks : ctx.modsugar.rootks;
	}
	
	public boolean tyCheckAll(CTX ctx, Object gamma) {
		boolean result = true;
		for(int i=0; i<blocks.size(); i++) {
			Stmt stmt = blocks.get(i);
			Syntax syn = stmt.syntax;
			//dumpSyntax
			if(syn == null) continue;
			if(syn.kw.equals(KW.Err)) {
				result = false;
				break;
			}
			if(!syn.stmtTyCheck(ctx, stmt, gamma)) {				
				result = false;
				break;
			}
		}
		return result;
	}
}
