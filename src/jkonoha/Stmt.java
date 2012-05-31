package jkonoha;

import java.util.*;

public class Stmt extends KObject {
	public long uline;
	public Syntax syntax;
	public Block parentNULL;
	public int build;  // TSTMT.XXXX
	
	public Stmt(long uline) {
		this.uline = uline;
	}
	
	// src/sugar/struct.h:930
	public Block getBlock(CTX ctx, int kw, Block def) {
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
	
	public String getText(CTX ctx, int kw, String def) {
		return (String)getObject(kw);
	}
	
	public Expr getExpr(CTX ctx, int kw, Expr def) {
		return (Expr)getObject(kw);
	}
	
	public int addAnnotation(CTX ctx, Stmt stmt, List<Token> tls, int s, int e) {//used in Parser.java
		int i;
		for (i = s; i < e; i++) {
			Token tk = (Token)tls.get(i);//Something wrong?
			if (tk.tt != TK_METANAME) break;//TODO
			if (i+1 < e) {
				String buf;
				int kw;//keyword(_ctx, (const char*)buf, S_size(tk->text)+1, FN_NEWID);TODO
				Token tk1 = (Token)tls.get(i+1);//Something wrong?
				KObject value = new KObject();
				if (tk1.tt == KW.Parenthesis) {
					value = (KObject)Stmt_newExpr2(ctx, stmt, tk1.sub, 0, tk1.sub.size());//TODO
					i++;
				}
				if (value != null) {
					value.setObject(kw, value);// the args in setObject is (String, Object) or (Int, Object)?
				}
			}
		}
		return 1;
	}
	
	public int matchSyntaxRule(CTX ctx, Stmt stmt, List<Token> rules, long uline, List<Token> tol, int s, int e, int optional) {
		// TODO
		return 0;
	}
	
	public boolean parseSyntaxRule(CTX ctx, Stmt stmt, List<Token> tls, int s, int e) {
		boolean ret = false;
		Syntax syn = (stmt.parentNULL.ks).getSyntaxRule(ctx, tls, s, e);//TODO KonohaSpace_getSyntaxRule
		assert (syn != null);
		if (syn.syntaxRuleNULL != null) {
			stmt.syntax = syn;
			ret = (matchSyntaxRule(ctx, stmt, syn.syntaxRuleNULL, stmt.uline, tls, s, e, 0) != -1);//TODO matchSyntaxRule
		}
		else {
			sugar_p(ERR_, stmt.uline, 0, "undefined syntax rule for '%s'", null);//TODO ERR_
		}
		return ret;
	}
	
	public void toERR (Stmt stmt, int eno) {
		stmt.syntax = SYN_(stmt, KW.Err);//TODO SYN_ = KonohaSpace_syntax(_ctx, KS, KW, 0)
		stmt.build = TSTMT.ERR;
		setObject(KW.Err, kstrerror(eno));//TODO
	}
}
