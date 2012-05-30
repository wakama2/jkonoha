package jkonoha;

import java.util.*;

public abstract class Expr extends KObject {
	public int ty;     // TY_var ==> TY_int
	public int build;  // 
	public Token tk;   // unfamiliar 
}

class ConsExpr extends Expr {
	
	public List<Object> cons = new ArrayList<Object>();
	
	public Expr at(int n) {
		return (Expr)cons.get(n);
	}
//	buildid;
//	int CALL     =  9;
//	int AND      = 10;
//	int OR       = 11;
//	int LET      = 12;
}

class TermExpr extends Expr {
	
}

class ConstExpr extends Expr {  // as if NConstExpr 
	public Object data;
//	int CONST    =  0;
//	int NCONST   =  3;  if ty is unboxed
}

//new P("name");
//(new newExpr(P) name)

class NewExpr extends Expr {
	//int NEW      =  1;
}

class NullExpr extends Expr {
	//int NULL     =  2;	
}

// local variable (block), (function) 

class FuncScopeVariableExpr extends Expr {
	public int index;
	//int LOCAL    =  4;
}

class BlockScopeVariableExpr extends Expr {
	public int index;
	int LOCAL_   = -4;   /*THIS IS NEVER PASSED*/
	int BLOCK_   = -3;   /*THIS IS NEVER PASSED*/
	int FIELD_   = -2;   /*THIS IS NEVER PASSED*/
}

class FieldExpr extends Expr {
	public int index;
	public int xindex;
	int FIELD    =  6;
}

class BoxingExpr extends Expr {
	public Expr single;
//	int BOX      =  7;
//	int UNBOX    =  8;
}

class BlockExpr extends Expr {
	public Block block;
//	int BLOCK    =  5;
}

class StackTopExpr extends Expr {
	//TODO;
}
