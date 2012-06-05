package jkonoha;

public interface TK {
	String NONE = "NONE";          // KW_Err
	String INDENT = "INDENT";        // KW_Expr
	String SYMBOL = "SYMBOL";        // KW_Symbol
	String USYMBOL = "USYMBOL";       // KW_Usymbol
	String TEXT = "TEXT";          // KW_Text
	String String = "String";           // KW_Int
	String FLOAT = "FLOAT";         // KW_Float
	String TYPE = "TYPE";          // KW_Type
	String AST_PARENTHESIS = "AST_PARENTHESIS";  // KW_Parenthesis
	String AST_BRANCET = "AST_BRANCET";      // KW_Brancet
	String AST_BRACE = "AST_BRACE";       // KW_Brace

	String OPERATOR = "OPERATOR";
	String MSYMBOL = "MSYMBOL";       //
	String ERR = "ERR";           //
	String CODE = "CODE";          //
	String WHITESPACE = "WHITESPACE";    //
	String METANAME = "METANAME";
	String MN = "MN";
	String AST_OPTIONAL = "AST_OPTIONAL";      // for syntax sugar
}
