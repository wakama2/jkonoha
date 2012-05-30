package jkonoha;

import java.util.*;

public class Parser extends KObject{

	// important
	public Block newBlock(CTX ctx, KonohaSpace ks, Stmt parent, List<Object> tls, int s, int e, int delim) {
		//TODO
		Block bk = new Block();
		int i = s, indent = 0, atop = tls.size();
		while (i < e) {
			Token tkERR;
			assert (atop == tls.size());
			i = selectStmtLine (ctx, ks, indent, tls, i, e, delim, tls, tkERR);//TODO How to tkERR?
			int asize = tls.size();
			if (asize > atop) {
				Block_addStmtLine (ctx, bk, tls, atop, asize, tkERR);
				tls.remove(atop);
			}
		}
		return null;
	}
	
	public void makeTree(CTX ctx, KonohaSpace ks, int tt, List<Token> tls, int s, int e, int closech, List<Object> tlsdst, Object tkERRRef) {
		//TODO 
				
	}
	
	public int selectStmtLine(CTX ctx, KonohaSpace ks, int indent, List<Token> tls, int s, int e, int delim, List<Object> tlsdst, Token tkERRRef) {
		//TODO
		return 1;
	}
	
	public void Block_addStmtLine (CTX ctx, Block bk, List<Object> tls, int s, int e, Token tkERR) {
		Stmt stmt = new Stmt(s/*tls.toks[s].uline*/);//TODO
		
		ArrayList<Object> tmp = (ArrayList<Object>)tls;
		tmp.add(bk);
		if (tkERR != null) {
			stmt.syntax = new Syntax();//TODO rf. /src/ast.h (SYN_ function in Block_addStmtLine). 
			stmt.build = TSTMT.ERR;
			setObject(KW.Err, tkERR);//TODO key is String or Int?
		}
		else {
			int estart = CtxSugar.errors.size();
			s = stmt.addAnnotation(ctx, stmt, tls, s, e);
			if (!stmt.parseSyntaxRule(ctx, stmt, tls, s, e)) {
				stmt.toERR(stmt, estart);
			}
		}
		assert (stmt.syntax != null);
	}

}
