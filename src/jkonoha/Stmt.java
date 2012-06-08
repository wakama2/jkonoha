package jkonoha;

import java.io.PrintStream;
import java.util.*;

import jkonoha.compiler.kobject.KBoolean;

public class Stmt extends KObject {
	public long uline;
	public Syntax syntax;
	public Block parentNULL;
	public int build;  // TSTMT.XXXX
	
	public Stmt(long uline) {
		this.uline = uline;
	}
	
	public int addAnnotation(CTX ctx, List<Token> tls, int s, int e) {
		int i;
		for (i = s; i < e; i++) {
			Token tk = tls.get(i);
			if (tk.tt != TK.METANAME) break;
			if (i+1 < e) {
				String kw = "@" + tk.text;
				Token tk1 = tls.get(i+1);
				KObject value = KBoolean.box(true);
				if (tk1.tt == TK.AST_PARENTHESIS) {
					value = (KObject)newExpr2(ctx, tk1.sub, 0, tk1.sub.size());
					i++;
				}
				if (value != null) {
					this.setObject(kw, value);
				}
			}
		}
		return i;
	}
	
	public Expr newExpr2(CTX ctx, List<Token> tls, int s, int e) {//Return value is not void
		if (s < e) {
			Syntax[] syn = new Syntax[]{null};
			int idx = findBinaryOp(ctx, tls, s, e, syn);
			if (idx != -1) {
				ctx.DBG_P("** Found BinaryOp: s=%d, idx=%d, e=%d, '%s'**",
						s, idx, e, tls.get(idx).toString());
				return syn[0].parseExpr(ctx, this, tls, s, idx, e);
			}
			int c = s;
			syn[0] = this.parentNULL.ks.syntax(ctx, tls.get(c).kw);
			return syn[0].parseExpr(ctx, this, tls, c, c, e);
		}
		else {
			if (0 < s - 1) {
				ctx.SUGAR_P(System.err, uline, -1, "expected expression after %s", tls.get(s-1).toString());
			}
			else if (e < tls.size()) {
				ctx.SUGAR_P(System.err, uline, -1, "expected expression before %s", tls.get(e).toString());
			}
			else {
				ctx.SUGAR_P(System.err, uline, 0, "expected expression");
			}
			return null;
		}
	}
	
	public int lookAheadKeyword (List<Token> tls, int s, int e, Token rule){
		int i;
		for (i = s; i < e; i++) {
			Token tk = tls.get(i);
			if (rule.kw.equals(tk.kw)) return i;
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
				if (!rule.kw.equals(tk.kw)) {
					if (optional)	return s;
					ctx.Token_p(tk, System.err, "%s needs '%s'", syntax.kw, rule.kw);
					return -1;
				}
				ti++;
				continue;
			}
			else if (rule.tt == TK.METANAME) {
				Syntax syn = parentNULL.ks.syntax(ctx, rule.kw);
				if (syn == null/* || syn.ParseStmtNULL == null*/) {//TODO Syntax has KMethod ParseStmtNULL
					ctx.Token_p (tk, System.err, "unknown syntax pattern: %s", rule.kw);
					return -1;
				}
				int c = e;
				if (ri +1 < ruleSize && rules.get(ri+1).tt == TK.CODE) {
					c = lookAheadKeyword (tls, ti+1, e, rules.get(ri+1));
					if (c == -1) {
						if (optional) return s;
						ctx.Token_p(tk, System.err, "%s needs '%s'", syntax.kw, rule.kw);
						return -1;
					}
					ri++;
				}
				int errCount = ctx.ctxsugar.errCount;
				int next = syn.parseStmt(ctx, this, rule.nameid, tls, ti, c);
				if (next == -1) {
					if (optional) return s;
					if (errCount == ctx.ctxsugar.errCount) {
						ctx.Token_p(tk, System.err, "%s needs syntax pattern %s, not %s ..", syntax.kw, rule.kw, tk);
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
				} else {
					if(optional) return s;
					throw new RuntimeException();
				}
			}
		}
		if (!optional) {
			for (; ri < rules.size(); ri++) {
				Token rule = rules.get(ri);
				if (rule.tt != TK.AST_OPTIONAL) {
					ctx.SUGAR_P (System.err, uline, -1, "%s needs syntax pattern: %s", syntax.kw, rule.kw);
					return -1;
				}
			}
			//WARN_Ignored(ctx, tls, ti, e);
		}
		return ti;
	}
	
	public boolean parseSyntaxRule(CTX ctx, List<Token> tls, int s, int e) {
		boolean ret = false;
		Syntax syn = parentNULL.ks.getSyntaxRule(ctx, tls, s, e);
		assert (syn != null);
		if (syn.syntaxRuleNULL != null) {
			syntax = syn;
			ret = (matchSyntaxRule(ctx, syn.syntaxRuleNULL, uline, tls, s, e, false) != -1);
		}
		else {
			ctx.SUGAR_P(System.err, uline, 0, "undefined syntax rule for '%s'", syn.kw);
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
		return (syn.op1 != null/*MN.NONAME*/);//TODO what is MN_NONAME?
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
	
	private int findBinaryOp(CTX ctx, List<Token> tls, int s, int e, Syntax[] synRef) {
		int idx = -1, i, prif = 0;
		for(i = skipUnaryOp(ctx, tls, s, e) + 1; i < e; i++) {
			Token tk = tls.get(i);
			Syntax syn = this.parentNULL.ks.syntax(ctx, tk.kw); 
//			if(syn != NULL && syn.op2 != 0) {
			if(syn.priority > 0) {
				if(prif < syn.priority || (prif == syn.priority && !((syn.flag & SYNFLAG.ExprLeftJoinOp2) == SYNFLAG.ExprLeftJoinOp2) )) {
					prif = syn.priority;
					idx = i;
					synRef[0] = syn;
				}
				if(! ((syn.flag & SYNFLAG.ExprPostfixOp2) == SYNFLAG.ExprLeftJoinOp2)) {  /* check if real binary operator to parse f() + 1 */
					i = skipUnaryOp(ctx, tls, i+1, e) - 1;
				}
			}
		}
		return idx;
	}
	
	public boolean tyCheckExpr(CTX ctx, String nameid, Gamma gamma, KClass reqty, int pol) {
		Object o = this.getObject(nameid);
		if(o != null && o instanceof Expr) {
			Expr expr = (Expr)o;
			Expr texpr = expr.tyCheck(ctx, gamma, reqty, pol);
			if(texpr != null && texpr != expr) {
				this.setObject(nameid, texpr);
			}
			return true;
		}
		return false;
	}
	
	public void typed(int build) {
		this.build = build;
	}
	
	private void dumpToken(PrintStream out, String key, Object value) {
		out.printf("key='%s': ", key);
		if(value instanceof Token) {
			((Token)value).dump(out);
		} else if(value instanceof Expr){
			((Expr)value).dump(out, 0, 0);
		} else {
			out.println(value);
		}
	}
	
	public void dump(PrintStream out) {
		if (this.syntax == null) {
			out.println("STMT (DONE)");
		} else {
			out.println("STMT " + this.syntax.kw + " {");
			for(Map.Entry<String, Object> e : this.entrySet()) {
				dumpToken(out, e.getKey(), e.getValue());
			}
			out.println();
			out.println("}");
		}
		out.flush();
	}
	public Expr addExprParams(CTX ctx, Expr expr, List<Token> tls, int s, int e, int allowEmpty) {
		int i, start = s;
		for(i = s; i < e; i++) {
			Token tk = tls.get(i);
			if(tk.kw.equals(KW.COMMA)) {
				if(expr != null) expr = expr.add(ctx, newExpr2(ctx, tls, start, i));
				start = i + 1;
			}
		}
		if(allowEmpty == 0 || start < i) {
			if(expr != null) expr = expr.add(ctx, newExpr2(ctx, tls, start, i));
		}
		KArray.clear(tls, s);
		return expr;
	}
}

