package jkonoha;

import java.util.*;

public abstract class Syntax {
	public String kw;   // id
	public int flag; // flag
	public List<Token> syntaxRuleNULL;

	public int ty;        // "void" ==> TY_void
	public int priority;  // op2   
	public int op2;
	public int op1;
	
	public int parseExpr(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		//TODO default parseExpr
		return 0;
	}
	
	public int parseStmt() {
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
	@Override public int parseExpr(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		// TODO ParseStmt_Expr
		return 0;
	}
}

class IntSyntax extends Syntax {
	IntSyntax() {
		this.flag = SYNFLAG.ExprTerm;
	}
}

class AddSyntax extends Syntax {

}

