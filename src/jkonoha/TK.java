package jkonoha;

public interface TK {
	int NONE = 0;          // KW_Err
	int INDENT = 1;        // KW_Expr
	int SYMBOL = 2;        // KW_Symbol
	int USYMBOL = 3;       // KW_Usymbol
	int TEXT = 4;          // KW_Text
	int INT = 5;           // KW_Int
	int FLOAT = 6;         // KW_Float
	int TYPE = 7;          // KW_Type
	int AST_PARENTHESIS = 8;  // KW_Parenthesis
	int AST_BRANCET = 9;      // KW_Brancet
	int AST_BRACE = 10;       // KW_Brace

	int OPERATOR = 11;
	int MSYMBOL = 12;       //
	int ERR = 13;           //
	int CODE = 14;          //
	int WHITESPACE = 15;    //
	int METANAME = 16;
	int MN = 17;
	int AST_OPTIONAL = 18;      // for syntax sugar
}
