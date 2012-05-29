package jkonoha;

import java.util.*;

public class Syntax {
	public int kw;   // id
	public int flag; // flag
	List<Token> syntaxRuleNULL;
	// "if" "(" $expr ")" $block ["else" $block]
	//Func ParseExpr;
	//Func ..;
	int ty;        // "void" ==> TY_void
	int priority;  // op2   
	int op2;
	int op1;
}
