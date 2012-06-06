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
	
	public int parseStmt(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
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
	@Override public int parseExpr(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		// TODO ParseStmt_Expr
		//VAR_ParseStmt (stmt, syn, name, tls, s, e);
		int r = -1;
		//dumpTokenArray (ctx, 0, tls, s, e);
		Expr expr = stmt.newExpr2(ctx, tls, s, e);
		if (expr != (Expr)ctx.modsugar.cExpr.nulvalNUL) {
			//dumpExpr (ctx, 0, 0, expr);
			//kObjecct_setObject(stmt, name, expr);
			r = e;
		}
		return r;
	}
	private boolean StmtTyCheck_Expr (CTX ctx, SFP sfp) {
		VAR_StmtTyCheck(stmt, syn, gma);
		boolean r = Stmt_tyCheckExpr(ctx, stmt, KW.Expr, gma, TY.VAR, (1 << 1)/*TPOL_ALLOWVOID*/);
		kStmt_typed(stmt, EXPR);
		return r;
	}
}

class IntSyntax extends Syntax {
	IntSyntax() {
		this.flag = SYNFLAG.ExprTerm;
	}
//	VAR_ExprTyCheck(expr, syn, gma, reqty);
//	 kToken *tk = expr->tk;
//	 long long n = strtoll(S_text(tk->text), NULL, 0); 
//	 RETURN_(kExpr_setNConstValue(expr, TY_Int, (uintptr_t)n));

}

class AddSyntax extends Syntax {
	AddSyntax() {
		this.flag = SYNFLAG.ExprOp;
		this.op1 = "opPULS";
		this.op2 = "opADD";
		this.priority = 64;
	}
}

