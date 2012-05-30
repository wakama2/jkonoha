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
	
	public void makeTree(CTX ctx, KonohaSpace ks, int tt, List<Token> tls, int s, int e, int closech, List<Token> tlsdst, Object tkERRRef) {
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
		KObject trial = new KObject();
		if (tkERR != null) {
			stmt.syntax = new Syntax();//TODO rf. /src/ast.h (SYN_ function in Block_addStmtLine). 
			stmt.build = TSTMT.ERR;
			trial.setObject(KW.Err, tkERR);
		}
		else {
			int estart = Ctxsugar.errors.size();//TODO	creat Ctxsugar Class
			s = Stmt_addAnnotation(ctx, stmt, tls, s, e);//TODO
			if (!Stmt_parseSyntaxRule(ctx, stmt, tls, s, e)) {//TODO
				Stmt_toERR(stmt, estart);
			}
		}
		assert (stmt.syntax != null);
	}
	
	public int Stmt_addAnnotation(CTX ctx, Stmt stmt, List<Object>  tls, int s, int e) {
		int i;
		for (i = s; i < e; i++) {
			Token tk;
			if (tk.tt != TK_METANAME) break;
			if (i+1 < e) {
				String buf;
				//snprintf(buf, sizeof(buf), "@%s", S_text(tk->text));
				int kw;
				
			}
		}
		return 1;
	}
	public boolean Stmt_parseSyntaxRule(CTX ctx, Stmt stmt, List<Object> tls, int s, int e) {
		return true;
	}
	public void Stmt_toERR (Stmt stmt, int estart) {
		//TODO
	}
}
