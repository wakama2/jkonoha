package jkonoha;

import java.util.*;

public class Block extends KObject {
	public final KonohaSpace ks;
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
	
	public void addStmtLine(CTX ctx, List<Token> tls, int s, int e, Token tkERR) {
		Stmt stmt = new Stmt(tls.get(s).uline);
		this.blocks.add(stmt);
		stmt.parentNULL = this;
		if (tkERR != null) {
			stmt.syntax = stmt.parentNULL.ks.syntax(ctx, KW.Err);
			stmt.build = TSTMT.ERR;
			stmt.setObject(KW.Err, tkERR);
		}
		else {
			int estart = ctx.ctxsugar.errors.size();
			s = stmt.addAnnotation(ctx, tls, s, e);
			if (!stmt.parseSyntaxRule(ctx, tls, s, e)) {
				stmt.toERR(estart);
			}
		}
		assert (stmt.syntax != null);
	}	
	
}
