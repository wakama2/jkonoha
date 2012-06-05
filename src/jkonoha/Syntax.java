package jkonoha;

import java.util.*;

public class Syntax {
	public String kw;   // id
	public int flag; // flag
	List<Token> syntaxRuleNULL;
	KMethod ParseStmtNULL;
	KMethod ParseExpr;
	KMethod TopStmtTyCheck;
	KMethod StmtTyCheck;
	KMethod ExprTyCheck;
	
	// "if" "(" $expr ")" $block ["else" $block]
	//Func ParseExpr;
	//Func ..;
	int ty;        // "void" ==> TY_void
	int priority;  // op2   
	int op2;
	int op1;
}
