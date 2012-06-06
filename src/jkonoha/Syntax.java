package jkonoha;

import java.util.*;

public abstract class Syntax {
	public String kw;   // id
	public int flag; // flag
	public String rule;
	public List<Token> syntaxRuleNULL;

	public int ty;        // "void" ==> TY_void
	public int priority;  // op2   
	public String op2;
	public String op1;
	
	public Syntax(String kw) {
		this.kw = kw;
	}
	
	public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO undefinedParseExpr ast.h:518
		return null;
	}
	
	public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		//TODO default parseStmt?
		return 0;
	}

	public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		//TODO tycheck.h:107
		return null;
	}
	
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		//TODO undefinedStmtTyCheck tycheck.h:734
		return false;
	}
	
	public final/*FIXME*/ boolean topStmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		return stmtTyCheck(ctx, stmt, gamma);
	}
}

/*private void dumpTokenArray (CTX ctx, int nest, List<Token> a, int s, int e) {
	if (verboseSugar) {
		if (nest ==0) System.out.println ("rf. dumpTokenArray");
		while (s < e) {
			Token tk = a.get(s);
			dumpIndent(nest);
			if (tk.sub.h.ct.bcid == TY.ARRAY)
		}
	}
}*/

class ERRSyntax extends Syntax {
	public ERRSyntax() {
		super("$ERR");
		this.flag = SYNFLAG.StmtBreakExec;
	}
}

class ExprSyntax extends Syntax {
	public ExprSyntax() {
		super("$expr");
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		//dumpTokenArray (ctx, 0, tls, s, e);
		Expr expr = stmt.newExpr2(ctx, tls, s, e);
		if (expr != null) {
			//dumpExpr (ctx, 0, 0, expr);
			stmt.setObject(name, expr);
			r = e;
		}
		return r;
	}
	@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		boolean r = stmt.tyCheckExpr(ctx, KW.Expr, gamma, TY.var, TPOL.ALLOWVOID);
		stmt.typed(TSTMT.EXPR);
		return r;
	}
}

class SYMBOLSyntax extends Syntax {
	public SYMBOLSyntax(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		super("$SYMBOL");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.SYMBOL) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		return expr.tyCheckVariable2(ctx, gamma, ty);
	}
}

class USYMBOLSyntax extends Syntax {
	public USYMBOLSyntax(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		super("$USYMBOL");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.USYMBOL) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		DBG_P("USYMBOL...");
		Token tk = expr.tk;
		int ukey = kuname(S_text(tk.text), S_size(tk.text), 0, FN_NONAME);
		if(ukey != FN_NONAME){
			Kvs kv = gamma.genv.ks.getConstNULL(ctx, ukey);
			if(kv != null) {
				if(FN_isBOXED(kv.key)) {
					expr.setConstValue(kv.ty, kv.oval);
				}
				else {
					expr.setNConstValue(kv.ty, kv.uval);
				}
				return expr;
			}
		}
		KObject v = gamma.genv.ks.getSymbolValueNULL(ctx, S_text(tk.text), S_size(tk.text));
		Expr texpr = (v == null) ?
				kToken_p(tk, ERR_, "undefined name: %s", kToken_s(tk)) : kExpr_setConstValue(expr, O_cid(v), v);
				return texpr;
	}
}

class TextSyntax extends Syntax {
	public TextSyntax() {
		super("$TEXT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		return expr.tyCheckVariable2(ctx, gamma, ty);
	}
}

class IntSyntax extends Syntax {
	public IntSyntax() {
		super("$INT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		Token tk = expr.tk;
		long l = Long.parseLong(tk.text);
		return new ConstExpr(l);
	}
}

class FloatSyntax extends Syntax {
	public FloatSyntax() {
		super("$FLOAT");
		this.flag = SYNFLAG.ExprTerm;
	}
}

class TypeSyntax extends Syntax {
	public TypeSyntax() {
		super("$type");
		this.flag = SYNFLAG.ExprTerm;
		this.rule = "$type $expr";
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.kw == KW.Type) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
	@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		Token tk  = stmt.token(KW.Type, null);
		Expr expr = stmt.expr(KW.Expr, null);
		if(tk == null || tk.kw != KW.Type || expr == null) {
			ERR_SyntaxError(stmt.uline);
			return false;
		}
		stmt.done(); //kStmt_done(stmt)
		return expr.declType(ctx, gamma, tk.ty, stmt);
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		assert(expr.tk.kw == KW.Type);
		return expr.setVariable(null, expr.tk.ty, 0, gamma);
	}
}

abstract class TermSyntax extends Syntax {
	public TermSyntax(String kw) {
		super(kw);
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO src/sugar/ast.h:638 ParseExpr_Term
		return null;
	}
}

abstract class OpSyntax extends Syntax {
	public OpSyntax(String kw) {
		super(kw);
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO src/sugar/ast.h:650 ParseExpr_Op
		return null;
	}
}

class AddSyntax extends OpSyntax {
	public AddSyntax() {
		super("+");
		this.flag = SYNFLAG.ExprOp;
		this.op1 = "opPULS";
		this.op2 = "opADD";
		this.priority = 64;
	}
}

