package jkonoha.ast;

import java.io.PrintStream;
import java.util.*;

import jkonoha.CTX;
import jkonoha.KArray;
import jkonoha.KBoolean;
import jkonoha.KClass;
import jkonoha.KObject;

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
	
	private int lookAheadKeyword (List<Token> tls, int s, int e, Token rule){
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
//				int errCount = ctx.ctxsugar.errCount;
				int next = syn.parseStmt(ctx, this, rule.nameid, tls, ti, c);
				if (next == -1) {
					if (optional) return s;
//					if (errCount == ctx.ctxsugar.errCount) {
						ctx.Token_p(tk, System.err, "%s needs syntax pattern %s, not %s ..", syntax.kw, rule.kw, tk);
//					}
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
	
	private void toERR (int eno) {
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
				if(! ((syn.flag & SYNFLAG.ExprPostfixOp2) != 0)) {  /* check if real binary operator to parse f() + 1 */
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
	
	private void dumpToken(CTX ctx, String key, Object value) {
		PrintStream out = System.out;
		out.printf("key='%s': ", key);
		if(value instanceof Token) {
			((Token)value).dump(out);
		} else if(value instanceof Expr){
			((Expr)value).dump(ctx, 0, 0);
		} else {
			out.println(value);
		}
	}
	
	public void dump(CTX ctx) {
		PrintStream out = System.out;
		if (this.syntax == null) {
			out.println("STMT (DONE)");
		} else {
			out.println("STMT " + this.syntax.kw + " {");
			for(Map.Entry<String, Object> e : this.entrySet()) {
				dumpToken(ctx, e.getKey(), e.getValue());
			}
			out.println();
			out.println("}");
		}
		out.flush();
	}
	public Expr addExprParams(CTX ctx, Expr expr, List<Token> tls, int s, int e, boolean allowEmpty) {
		int i, start = s;
		for(i = s; i < e; i++) {
			Token tk = tls.get(i);
			if(tk.kw.equals(KW.COMMA)) {
				expr = expr.add(ctx, newExpr2(ctx, tls, start, i));
				start = i + 1;
			}
		}
		if(!allowEmpty || start < i) {
			expr = expr.add(ctx, newExpr2(ctx, tls, start, i));
		}
		KArray.clear(tls, s);
		return expr;
	}

	public Block block(CTX ctx, String kw, Block def) {
		Object bk = getObject(kw);
		if(bk != null) {
			if(bk instanceof Token) {
				Token tk = (Token)bk;
				if (tk.tt == TK.CODE) {
					tk.toBRACE(ctx, this.parentNULL.ks);
				}
				if (tk.tt == TK.AST_BRACE) {
					bk = Parser.newBlock(ctx, this.parentNULL.ks, this, tk.sub, 0, tk.sub.size(), ';');
					this.setObject(kw, bk);
				}
			}
			if(bk instanceof Block) return (Block)bk;
		}
		return def;
	}

	public Stmt lookupIfStmtNULL(CTX ctx) {
		int i;
		List<Stmt> bka = this.parentNULL.blocks;
		Stmt prevIfStmt = null;
		for(i = 0; bka.size() != 0; i++) {
			Stmt s = bka.get(i);
			if(s == this) {
				if(prevIfStmt != null) {
					return prevIfStmt.lookupIfStmtWithoutElse(ctx);
				}
				return null;
			}
			if(s.syntax == null) continue;  // this is done
			prevIfStmt = (s.syntax.kw.equals(KW._if)) ? s : null;
		}
		return null;
	}

	private Stmt lookupIfStmtWithoutElse(CTX ctx) {
		Block bkElse = block(ctx, KW._else, null);
		if(bkElse != null) {
			if(bkElse.blocks.size() == 1) {
				Stmt stmtIf = bkElse.blocks.get(0);
				if(stmtIf.syntax.kw.equals(KW._if)) {
					return stmtIf.lookupIfStmtWithoutElse(ctx);
				}
			}
			return null;
		}
		return this;
	}

	public Token token(CTX ctx, String kw, Token def) {
		Object tk = getObject(kw);
		if(tk != null && tk instanceof Token) {
			return (Token)tk;
		}
		return def;
	}
	
	//FIXME package/konoha/class_glue.h
	public void parseClassBlock(CTX ctx, Token tkC) {
		Token tkP = (Token)getObject(KW.Block);
		if(tkP != null && tkP.tt == TK.CODE) {
			List<Token> a = new ArrayList<Token>();//ctx.ctxsugar.tokens;
			int atop = a.size(), s, i;
			parentNULL.ks.tokenize(ctx, tkP.text, tkP.uline, a);
			s = a.size();
			String cname = tkC.text;
			for(i = atop; i < s; i++) {
				Token tk = a.get(i);
				ctx.DBG_P("cname='%s'", cname);
				if(tk.topch == '(' && tkP.tt == TK.USYMBOL && cname.equals(tkP.text)) {
					Token tkNEW = new Token();
					tkNEW.tt = TK.SYMBOL;
					tkNEW.text = KW._new;
					tkNEW.uline = tkP.uline;
					a.add(tkNEW);
				}
				a.add(tk);
				tkP = tk;
			}
			Block bk = Parser.newBlock(ctx, parentNULL.ks, this, a, s, a.size(), ';');
			for (i = 0; i < bk.blocks.size(); i++) {
				Stmt methodDecl = bk.blocks.get(i);
				if(methodDecl.syntax.kw == KW.StmtMethodDecl) {
					methodDecl.setObject(KW.Usymbol, tkC);
				}
			}
			setObject(KW.Block, bk);
			KArray.clear(a, atop);
		}
	}
	
//	public Expr expr(CTX ctx, String kw, Expr def)
//	{
//		Expr expr = (Expr)getObject(kw);
//		if(expr != null && expr.equals(h.ct) == CT_Expr) {
//			return expr;
//		}
//		return def;
//	}
}

