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
	
	public int addAnnotation(CTX ctx, List<Token> tls, int s, int e) {//used in Parser.java
		int i;
		for (i = s; i < e; i++) {
			Token tk = tls.get(i);
			if (tk.tt != TK.METANAME) break;
			if (i+1 < e) {
				String buf;
				String kw = "dummy"/*keyword(ctx, buf, S_size(tk.text)+1, FN_NEWID)*/;
				Token tk1 = tls.get(i+1);
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
				return null/*ParseExpr(ctx, syn, tls, s, idx, e)*/;
			}
			int c = s;
			syn = this.parentNULL.ks.syntax(ctx, tls.get(c).kw);
			return null/*ParseExpr(ctx, syn, tls, c, c, e)*/;
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
				Syntax syn = parentNULL.ks.syntax(ctx, rule.kw);
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
				int next = 1;//ParseStmt(ctx, syn, rule.nameid, tls, ti, c);
				if (next == -1) {
					if (optional) return s;
					if (errCount == ctx.ctxsugar.errCount) {
						//kToken_p(tk, ERR_, "%s needs syntax pattern %s, not %s ..", T_statement(syntax.kw), T_kw(rule.kw), kToken_s(tk));
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
					//SUGAR_P (ERR_, uline, -1, "%s needs syntax pattern: %s", T_statement(syntax.kw), T_kw (rule.kw));
					return -1;
				}
			}
			//WARN_Ignored(ctx, tls, ti, e);
		}
		return ti;
	}
	
	public boolean parseSyntaxRule(CTX ctx, List<Token> tls, int s, int e) {
		boolean ret = false;
		Syntax syn = parentNULL.ks.getSyntaxRule(ctx, tls, s, e);//TODO KonohaSpace_getSyntaxRule
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
	
	private boolean isUnaryOp(CTX ctx, Token tk)
	{
		Syntax syn = parentNULL.ks.syntax(ctx, tk.kw);
		return (syn.op1 != 0/*MN.NONAME*/);//TODO what is MN_NONAME?
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
			Syntax syn = this.parentNULL.ks.syntax(ctx, tk.kw); 
//			if(syn != NULL && syn.op2 != 0) {
			if(syn.priority > 0) {
				if(prif < syn.priority || (prif == syn.priority && !((syn.flag & SYNFLAG.ExprLeftJoinOp2) == SYNFLAG.ExprLeftJoinOp2) )) {
					prif = syn.priority;
					idx = i;
					synRef = syn;
				}
				if(! ((syn.flag & SYNFLAG.ExprPostfixOp2) == SYNFLAG.ExprLeftJoinOp2)) {  /* check if real binary operator to parse f() + 1 */
					i = skipUnaryOp(ctx, tls, i+1, e) - 1;
				}
			}
		}
		return idx;
	}
}
