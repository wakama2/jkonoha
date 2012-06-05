package jkonoha;

import java.util.*;

public abstract class Syntax {
	public final String kw;   // id
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
		//TODO default parseExpr
		return null;
	}
	
	public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		//TODO default parseExpr
		return 0;
	}

//	KMethod ParseStmtNULL;
//	KMethod ParseExpr;
//	KMethod TopStmtTyCheck;
//	KMethod StmtTyCheck;
//	KMethod ExprTyCheck;
	
	// "if" "(" $expr ")" $block ["else" $block]
	//Func ParseExpr;
	//Func ..;
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
}

class AddSyntax extends Syntax {
	public AddSyntax() {
		super("+");
	}

}

