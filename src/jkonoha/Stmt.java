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
	
	public int addAnnotation(CTX ctx, List<Token> tls, int s, int e) {//used in Parser.java
		int i;
		for (i = s; i < e; i++) {
			Token tk = tls.get(i);
			if (tk.tt != TK.METANAME) break;
			if (i+1 < e) {
				//String buf;
				int kw = 1;//keyword(ctx, (const char*)buf, S_size(tk.text)+1, FN_NEWID);TODO
				Token tk1 = tls.get(i+1);//Something wrong?
				KObject value = new KObject();
				if (tk1.tt == KW.Parenthesis) {
					value = (KObject)newExpr2(ctx, tk1.sub, 0, tk1.sub.size());//TODO
					i++;
				}
				if (value != null) {
					value.setObject(kw, value);
				}
			}
		}
		return 1;
	}
	
	public Expr newExpr2(CTX ctx, List<Token> tls, int s, int e) {//Return value is not void
		//TODO return
		if (s < e) {
			Syntax syn = null;
			int idx = findBinaryOp(ctx, tls, s, e, syn);
			if (idx != -1) {
				return ParseExpr(ctx, syn, tls, s, idx, e);
			}
			int c = s;
			syn = SYN_(this.parentNULL.ks, tls.get(c).kw);//TODO syn = SYN_
			return ParseExpr(ctx, syn, tls, c, c, e);
		}
		else {
			if (0 < s - 1) {
				//SUGAR_P(ERR_, uline, -1, "expected expression after %s", "TODO"/*kToken_s (tls.toks[s-1])*/);
			}
			else if (e < tls.size()) {
				//SUGAR_P(ERR_, uline, -1, "expected expression before %s", "TODO"/*kToken_s(tls.toks[e])*/);
			}
			else {
				//SUGAR_P(ERR_, uline, 0, "expected expression");
			}
			return null;//TODO
		}
	}
	
	public int lookAheadKeyword (List<Token> tls, int s, int e, Token rule){
		int i;
		for (i = s; i < e; i++) {
			Token tk = tls.get(i);
			if (rule.kw == tk.kw) return i;
		}
		return -1;
	}
	
	public int matchSyntaxRule(CTX ctx, List<Token> rules, long uline, List<Token> tls, int s, int e, boolean optional) {
		int ri, ti, ruleSize = rules.size();
		ti = s;
		for (ri = 0; ri < ruleSize && ti < e; ri++) {
			Token rule = rules.get(ri);
			Token tk = tls.get(ti);
			uline = tk.uline;
			if (rule.tt == TK.CODE) {
				if (rule.kw != tk.kw) {
					if (optional)	return s;
					//kToken_p(tk, ERR_, "%s needs '%s'", T_statement(syntax.kw), T_kw(rule.kw));
					return -1;
				}
				ti++;
				continue;
			}
			else if (rule.tt == TK.METANAME) {
				Syntax syn = SYN_(parentNULL.ks, rule.kw);
				if (syn == null || syn.ParseStmtNULL == null) {//TODO Syntax has KMethod ParseStmtNULL
					//kToken_p (tk, ERR_, "unknown syntax pattern: %s", T_kw(rule.kw));
					return -1;
				}
				int c = e;
				if (ri +1 < ruleSize && rules.get(ri+1).tt == TK.CODE) {
					c = lookAheadKeyword (tls, ti+1, e, rules.get(ri+1));
					if (c == -1) {
						if (optional) return s;
						//kTOken_p(tk, ERR_, "%s needs '%s'", T_statement(syntax.kw), T_kw(rule.kw));
						return -1;
					}
					ri++;
				}
				int errCount = ctx.ctxsugar.errCount;//TODO
				int next = ParseStmt(ctx, syn, rule.nameid, tls, ti, c);
				if (next == -1) {
					if (optional) return s;
					if (errCount == ctx.ctxsugar.errCount) {
						kToken_p(tk, ERR_, "%s needs syntax pattern %s, not %s ..", T_statement(syntax.kw), T_kw(rule.kw), kToken_s(tk));
					}
					return -1;
				}
				ti = (c == e) ? next : c + 1;
				continue;
			}
			else if (rule.tt == TK.AST_OPTIONAL) {
				int next = matchSyntaxRule(ctx, rule.sub, uline, tls, ti, e, true);
				if (next == -1) return -1;
				ti = next;
				continue;
			}
			else if (rule.tt == TK.AST_PARENTHESIS || rule.tt == TK.AST_BRACE || rule.tt == TK.AST_BRANCET) {
				if (tk.tt == rule.tt && rule.topch == tk.topch) {//topch is int type in Token
					int next = matchSyntaxRule(ctx, rule.sub, uline, tk.sub, 0, tk.sub.size(), false);
					if (next == -1) return -1;
					ti++;
				}
			}
		}
		if (!optional) {
			for (; ri < rules.size(); ri++) {
				Token rule = rules.get(ri);
				if (rule.tt != TK.AST_OPTIONAL) {
					SUGAR_P (ERR_, uline, -1, "%s needs syntax pattern: %s", T_statement(syntax.kw), T_kw (rule.kw));
					return -1;
				}
			}
			//WARN_Ignored(ctx, tls, ti, e);
		}
		return ti;
	}
	
	public boolean parseSyntaxRule(CTX ctx, List<Token> tls, int s, int e) {
		boolean ret = false;
		Syntax syn = (parentNULL.ks).getSyntaxRule(ctx, tls, s, e);//TODO KonohaSpace_getSyntaxRule
		assert (syn != null);
		if (syn.syntaxRuleNULL != null) {
			syntax = syn;
			ret = (matchSyntaxRule(ctx, syn.syntaxRuleNULL, uline, tls, s, e, false) != -1);//TODO matchSyntaxRule
		}
		else {
			//sugar_p(ERR_, uline, 0, "undefined syntax rule for '%s'", null);//TODO ERR_
		}
		return ret;
	}
	
	public void toERR (int eno) {
//		stmt.syntax = SYN_(stmt, KW.Err);//TODO SYN_ = KonohaSpace_syntax(_ctx, KS, KW, 0)
//		stmt.build = TSTMT.ERR;
//		setObject(KW.Err, kstrerror(eno));//TODO
	}
	
//	private Expr ParseExpr(CTX ctx, Syntax syn, List<Token> tls, int s, int c, int e) // TODO
//	{
//		KMethod mtd = (syn == null || syn.ParseExpr == null) ? kmodsugar.UndefinedParseExpr : syn.ParseExpr;
//		BEGIN_LOCAL(lsfp, 10); // BEGIN_LOCAL is at konoha2.h, lsfp is in command.c (ksfp_t *lsfp = base->stack + base->evalidx;)
//		KSETv(lsfp[K.CALLDELTA+0].o, (KObject)this);
//		lsfp[K.CALLDELTA+0].ndata = (uintptr_t)syn;  // quick access, TODO Why cast ksyntax_t* to uintptr_t?
//		KSETv(lsfp[K.CALLDELTA+1].o, tls);
//		lsfp[K.CALLDELTA+2].ivalue = s;
//		lsfp[K.CALLDELTA+3].ivalue = c;
//		lsfp[K.CALLDELTA+4].ivalue = e;
//		KCALL(lsfp, 0, mtd, 4, K.NULLEXPR);//TODO KNULLEXPR
//		END_LOCAL(); // END_LOCAL is at konoha2.h
//		assert(IS_Expr(lsfp[0].o));
//		return lsfp[0].expr;
//	}
	
	private boolean isUnaryOp(CTX ctx, Token tk)
	{
		//Syntax syn = SYN_(this.parentNULL.ks, tk.kw); // kStmt_ks is at sugar.h
		this.parentNULL.ks.syntax(ctx, tk.kw, 0);
		return (syn.op1 != MN_NONAME);
	}
	
	private int skipUnaryOp(CTX ctx, List<Token> tls, int s, int e) {
		int i;
		for(i = s; i < e; i++) {
			Token tk = tls.get(i);
			if(!isUnaryOp(ctx, tk)) {
				break;
			}
		}
		return i;
	}
	
	private int findBinaryOp(CTX ctx, List<Token> tls, int s, int e, Syntax synRef) {
		int idx = -1, i, prif = 0;
		for(i = skipUnaryOp(ctx, tls, s, e) + 1; i < e; i++) {
			Token tk = tls.get(i);
			Syntax syn = SYN_(this.parentNULL.ks, tk.kw); // kStmt_ks is at sugar.h
//			if(syn != NULL && syn.op2 != 0) {
			if(syn.priority > 0) {
				if(prif < syn.priority || (prif == syn.priority && !(FLAG_is(syn.flag, SYNFLAG_ExprLeftJoinOp2)) )) {
					prif = syn.priority;
					idx = i;
					synRef = syn;
				}
				if(!FLAG_is(syn.flag, SYNFLAG_ExprPostfixOp2)) {  /* check if real binary operator to parse f() + 1 */
					i = skipUnaryOp(ctx, tls, i+1, e) - 1;
				}
			}
		}
		return idx;
	}
}
