package jkonoha;

import java.util.*;

public abstract class Syntax {
	public final String kw;
	public int flag;
	public String rule;
	public List<Token> syntaxRuleNULL;

	public int ty;
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

