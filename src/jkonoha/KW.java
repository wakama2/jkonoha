package jkonoha;

public interface KW {

	String Err     = "$ERR";
	String Expr    = "$expr";
	String Symbol  = "$SYMBOL";
//	String name   = 2;
	String Usymbol = "$USYMBOL";
//	String cname  = 3;
	String Text    = "$TEXT";
	String Int     = "$INT";
	String Float   = "$FLOAT";
	String Type    = "$type";
	String StmtTypeDecl = Type;
	String Parenthesis  = "()";
	String Brancet      = "[]";
	String Brace        = "{}";
	
	String String     = "String";
	String Block   = "$block";
	String Params  = "$params";
	String ExprMethodCall = Params;//"ExprMethodCall";/*FIXME*/
	String Toks    = "$toks";
	String[] TK_KW = { Err, Expr, Symbol, Usymbol, Text, Int, Float, Type, Parenthesis, Brancet, Brace };

	String DOT     = ".";
	String DIV     = "DIV";
	String MOD     = "MOD";
	String MUL     = "MUL";
	String ADD     = "ADD";
	String SUB     = "SUB";
	String LT      = "LT";
	String LTE     = "LTE";
	String GT      = "GT";
	String GTE     = "GTE";
	String EQ      = "EQ";
	String NEQ     = "NEQ";
	String AND     = "AND";
	String OR      = "OR";
	String NOT     = "NOT";
//	String COLON  = (15+DOT);
	String LET     = "LET";
	String COMMA   = ",";
	String DOLLAR  = "$";

	String _void      = "_void";
	String StmtMethodDecl = _void;
	String _boolean   = "_boolean";
	String _String       = "_String";
//	String _null     = (3+_void);
	String _true      = "_true";
	String _false     = "_false";
	String _if        = "_if";
	String _else      = "_else";
	String _return    = "_return";
// reserved
	String _new       = "_new";
}
