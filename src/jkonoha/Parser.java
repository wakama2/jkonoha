package jkonoha;

import java.util.*;

public class Parser {

	// important
	public Block newBlock(CTX ctx, KonohaSpace ks, Stmt parent, List<Token> tls, int s, int e, int delim) {
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
	
	public int selectStmtLine(CTX ctx, KonohaSpace ks, int indent, List<Token> tls, int s, int e, int delim, List<Token> tlsdst, Token tkERRRef) {
		//TODO
		return 1;
	}
	
	public void Block_addStmtLine (CTX ctx, Block bk, List<Token> tls, int s, int e, Token tkERR) {
		//TODO
		Stmt stmt = new Stmt(s/*tls.toks[s].uline*/);
		stmt.add(bk);
		if (tkERR != null) {
			
		}
		
	}
	
}
