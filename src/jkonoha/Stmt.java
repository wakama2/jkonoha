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
			Token tk = (Token)tls.get(i);
			if (tk.tt != TK.METANAME) break;
			if (i+1 < e) {
				//String buf;
				int kw;//keyword(_ctx, (const char*)buf, S_size(tk->text)+1, FN_NEWID);TODO
				Token tk1 = (Token)tls.get(i+1);//Something wrong?
				KObject value = new KObject();
				if (tk1.tt == KW.Parenthesis) {
					value = (KObject)stmt.newExpr2(ctx, stmt, tk1.sub, 0, tk1.sub.size());//TODO
					i++;
				}
				if (value != null) {
					value.setObject(kw, value);
				}
			}
		}
		return 1;
	}
	
	public void newExpr2(CTX ctx, Stmt stmt, List<Token> tls, int s, int e) {//Return value is not void
		//TODO return
		if (s < e) {
			Syntax syn = null;
			int idx = stmt.findBinaryOp(ctx, stmt, tls, s, e, syn);//TODO Stmt_findBinaryOp
			if (idx != -1) {
				return ParseExpr(_ctx, syn, stmt, tls, c, c, e);
			}
			int c = s;
			syn = SYN_(stmt);//TODO syn = SYN_(kStmt_ks(stmt), (tls->toks[c])->kw);
			return ParseExpr(_ctx, syn, stmt, tls, c, c, e);
		}
		else {
			if (0 < s - 1) {
				SUGAR_P(ERR_, stmt.uline, -1, "expected expression after %s", "TODO"/*kToken_s (tls->toks[s-1])*/);
			}
			else if (e < tls.size()) {
				SUGAR_P(ERR_, stmt.uline, -1, "expected expression before %s", "TODO"/*kToken_s(tls->toks[e])*/);
			}
			else {
				SUGAR_P(ERR_, stmt.uline, 0, "expected expression");
			}
			return null;//TODO
		}
	}
	
	public int matchSyntaxRule(CTX ctx, Stmt stmt, List<Token> rules, long uline, List<Token> tls, int s, int e, boolean optional) {
		int ri, ti, ruleSize = rules.size();
		ti = s;
		for (ri = 0; ri < ruleSize && ti < e; ri++) {
			Token rule = rules.get(ri);
			Token tk = tls.get(ti);
			uline = tk.uline;
			if (rule.tt == TK.CODE) {
				if (rule.kw != tk.kw) {
					if (optional)	return s;
					//kTOken_p(tk, ERR_, "%s needs '%s'", T_statement(stmt.syntax.kw), T_kw(rule.kw));
					return -1;
				}
				ti++;
				continue;
			}
			else if (rule.tt == TK.METANAME) {
				Syntax syn = SYN_(stmt.parentNULL.ks, rule.kw);
				if (syn == null || syn.ParseStemtNULL == null) {
					//kToken_p (tk, ERR_, "unknown syntax pattern: %s", T_kw(rule->kw));
					return -1;
				}
				int c = e;
				if (ri +1 < ruleSize && rules.get(ri+1).tt == TK.CODE) {
					c = lookAheadKeyword (tls, ti+1, e, rules.get(ri+1));
					if (c == -1) {
						if (optional) return s;
						//kTOken_p(tk, ERR_, "%s needs '%s'", T_statement(stmt.syntax.kw), T_kw(rule.kw));
						return -1;
					}
					ri++;
				}
				int errCount = CtxSugar.errCount;//TODO
				int next = ParseStmt(ctx, syn, stmt,rule.nameid, tls, ti, c);
				if (next == -1) {
					if (optional) return s;
					if (errCount == CtxSugar.errCount) {
						kToken_p(tk, ERR_, "%s needs syntax pattern %s, not %s ..", T_statement(stmt.syntax.kw), T_kw(rule.kw), kToken_s(tk));
					}
					return -1;
				}
				ti = (c == e) ? next : c + 1;
				continue;
			}
			else if (rule.tt == TK.AST_OPTIONAL) {
				int next = matchSyntaxRule(ctx, stmt, rule.sub, uline, tls, ti, e, 1);
				if (next == -1) return -1;
				ti = next;
				continue;
			}
			else if (rule.tt == TK.AST_PARENTHESIS || rule.tt == TK.AST_BRACE || rule.tt == TK.AST_BRACKET) {
				if (tk.tt == rule.tt && rule.topch == tk.topch) {//topch is int type in Token
					int next = matchSyntaxRule(ctx, stmt, rule.sub, uline, tk.sub, 0, tk.sub.size(), 0 );
					if (next == -1) return -1;
					ti++;
				}
			}
		}
		if (!optional) {
			for (; ri < rules.size(); ri++) {
				Token rule = rules.get(ri);
				if (rule.tt != TK.AST_OPTIONAL) {
					SUGAR_P (ERR_, uline, -1, "%s needs syntax pattern: %s", T_statement(stmt.syntax.kw), T_kw (rule.kw));
					return -1;
				}
			}
			//WARN_Ignored(ctx, tls, ti, e);
		}
		return ti;
	}
	
	public boolean parseSyntaxRule(CTX ctx, Stmt stmt, List<Token> tls, int s, int e) {
		boolean ret = false;
		Syntax syn = (stmt.parentNULL.ks).getSyntaxRule(ctx, tls, s, e);//TODO KonohaSpace_getSyntaxRule
		assert (syn != null);
		if (syn.syntaxRuleNULL != null) {
			stmt.syntax = syn;
			ret = (matchSyntaxRule(ctx, stmt, syn.syntaxRuleNULL, stmt.uline, tls, s, e, false) != -1);//TODO matchSyntaxRule
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
