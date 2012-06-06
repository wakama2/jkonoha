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
	
	public boolean topStmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		return stmtTyCheck(ctx, stmt, gamma);
	}
	
}

class ExprSyntax extends Syntax {
	public ExprSyntax() {
		super("$expr");
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		// TODO ParseStmt_Expr
		return 0;
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
	}
}

