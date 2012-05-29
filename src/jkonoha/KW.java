package jkonoha;

public interface KW {
	int Err     = 0;
	int Expr    = 1;
	int Symbol  = 2;
//	int name   = 2;
	int Usymbol = 3;
//	int cname  = 3;
	int Text    = 4;
	int Int     = 5;
	int Float   = 6;
	int Type    = 7;
	int StmtTypeDecl = Type;
	int Parenthesis  = 8;
	int Brancet      = 9;
	int Brace        = 10;

	int Block   = 11;
	int Params  = 12;
	int ExprMethodCall = 12;/*FIXME*/
	int Toks    = 13;

	int DOT     = 14;
	int DIV     = (1+DOT);
	int MOD     = (2+DOT);
	int MUL     = (3+DOT);
	int ADD     = (4+DOT);
	int SUB     = (5+DOT);
	int LT      = (6+DOT);
	int LTE     = (7+DOT);
	int GT      = (8+DOT);
	int GTE     = (9+DOT);
	int EQ      = (10+DOT);
	int NEQ     = (11+DOT);
	int AND     = (12+DOT);
	int OR      = (13+DOT);
	int NOT     = (14+DOT);
//	int COLON  = (15+DOT);
	int LET     = (15+DOT);
	int COMMA   = (16+DOT);
	int DOLLAR  = (17+DOT);

	int _void      = (18+DOT);
	int StmtMethodDecl = _void;
	int _boolean   = (1+_void);
	int _int       = (2+_void);
//	int _null     = (3+_void);
	int _true      = (3+_void);
	int _false     = (4+_void);
	int _if        = (5+_void);
	int _else      = (6+_void);
	int _return    = (7+_void);
// reserved
	int _new       = (8+_void);
}
