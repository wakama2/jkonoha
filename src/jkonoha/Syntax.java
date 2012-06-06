package jkonoha;

import java.util.*;

public abstract class Syntax {
	public String kw;   // id
	public int flag; // flag
	public List<Token> syntaxRuleNULL;

	public int ty;        // "void" ==> TY_void
	public int priority;  // op2   
	public String op2;
	public String op1;
	
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

class ERRSyntax extends Syntax {
	this.flag = SYNFRAG.StmtBreakExec;
}

class ExprSyntax extends Syntax {
	@Override public int parseExpr(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		// TODO ParseStmt_Expr
		return 0;
	}
}

class ExprSYMBOL extends Syntax {
	public ExprSYMBOL(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		VAR_ParseStmt(stmt, syn, name, tls, s, e);
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.SYMBOL) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		sfp[K_RIX].ivalue = r;
	}
}

class ExprUSYMBOL extends Syntax {
	public ExprUSYMBOL(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		VAR_ParseStmt(stmt, syn, name, tls, s, e);
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.USYMBOL) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		sfp[K_RIX].ivalue = r;
	}
}

class ExprTEXT extends Syntax {
	public ExprTEXT() {
		this.flag = SYNFLAG.ExprTerm;
	}
}

class ExprInt extends Syntax {
	public ExprInt() {
		this.flag = SYNFLAG.ExprTerm;
	}
}

class ExprFloat extends Syntax {
	public ExprFloat() {
		this.flag = SYNFLAG.ExprTerm;
	}
}

class ExprTYPE extends Syntax {
	public ExprTYPE(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		VAR_ParseStmt(stmt, syn, name, tls, s, e);
		int r = -1;
		Token tk = tls.get(s);
		if(tk.kw == KW.Type) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		sfp[K_RIX].ivalue = r;
	}
}

class AddSyntax extends Syntax {
	public AddSyntax() {
		this.flag = SYNFLAG.ExprOp;
		op1 = "opPLUS";
		op2 = "opADD";
		priority = 64;
	}
}

