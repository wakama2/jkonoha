package jkonoha;

public interface KW {
	String[] TK_KW = {"Err", "Expr", "Symbol", "Usymbol", "Text", "Int", "Float", "Type", "Parenthesis", "Brancet", "Brace"};

	String Err     = "Err";
	String Expr    = "Expr";
	String Symbol  = "Symbol";
//	String name   = 2;
	String Usymbol = "Usymbol";
//	String cname  = 3;
	String Text    = "Text";
	String String     = "String";
	String Float   = "Float";
	String Type    = "Type";
	String StmtTypeDecl = Type;
	String Parenthesis  = "Parenthesis";
	String Brancet      = "Brancet";
	String Brace        = "Brace";
	String Block   = "Block";
	String Params  = "Params";
	String ExprMethodCall = "ExprMethodCall";/*FIXME*/
	String Toks    = "Toks";

	String DOT     = "DOT";
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
	String COMMA   = "COMMA";
	String DOLLAR  = "DOLLAR";

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
