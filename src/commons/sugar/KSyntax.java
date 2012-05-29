package commons.sugar;

import commons.konoha2.kclass.*;

public class KSyntax {
	
	int kw;  int flag;
	K_Array<K_Object>   syntaxRuleNULL;
	K_Method  ParseStmtNULL;
	K_Method  ParseExpr;
	K_Method  TopStmtTyCheck;
	K_Method  StmtTyCheck;
	K_Method  ExprTyCheck;
	// binary
	int ty; int priority;
	int op2; int op1;      // & a
	//kshort_t dummy;
}
