package jkonoha;

import java.util.*;

//import commons.konoha2.*;
//import commons.konoha2.kclass.*;

/**
 * _kToken in original konoha2
 * @author okachin
 *
 */

public abstract class Token {
	public int  tt;
	public int  kw;
	public long uline;
	
	public Token() {
		this.tt = 0;
		this.kw = 0;
	}
	
	public static final void tokenize(CTX ctx, TEnv tenv) {
		int ch, pos = 0;
		FTokenizer fmat[] = tenv.fmat;
		Token tk = new RawToken(tenv.uline);
//		tk.lpos = tenv.lpos(0);
		pos = Tokenizer.parseINDENT.parse(ctx, tk, tenv, pos, null);
		while(pos < tenv.source.length() && (ch = Tokenizer.kchar(tenv.source, pos)) != 0) {
			if(tk.tt != 0) {
				tenv.list.add(tk);
				tk = new RawToken(tenv.uline);
				//tk.lpos = tenv.lpos(pos);
			}
			int pos2 = fmat[ch].parse(ctx, tk, tenv, pos, null);
			assert pos2 > pos;
			pos = pos2;
		}
		if(tk.tt != 0) {
			tenv.list.add(tk);
		}
	}
	
	/* ktoken_t */
	public static final int TK_NONE = 0;
	public static final int TK_INDENT = 1;
	public static final int TK_SYMBOL = 2;
	public static final int TK_USYMBOL = 3;
	public static final int TK_TEXT = 4;
	public static final int TK_INT = 5;
	public static final int TK_FLOAT = 6;
	public static final int TK_TYPE = 7;
	public static final int AST_PARENTHESIS = 8;
	public static final int AST_BRANCET = 9;
	public static final int AST_BRACE = 10;
	public static final int TK_OPERATOR = 11;
	public static final int TK_MSYMBOL = 12;
	public static final int TK_ERR = 13;
	public static final int TK_CODE = 14;
	public static final int TK_WHITESPACE = 15;
	public static final int TK_METNAME = 16;
	public static final int TK_MN = 17;
	public static final int AST_OPTIONAL = 18;
}

final class RawToken extends Token {
	public String text;
	public int topch;
	
	RawToken(long uline){
		super();
		this.uline = uline;
		this.topch = 0;
	}
}

final class AstToken extends Token {
	public ArrayList<Token> sub;
	public int topch;
	public int closech;
}

final class TypeToken extends Token {
	public int ty;
}

final class MethodToken extends Token {
	public int mn_type;
	public int mn;
}

final class SyntaxRuleToken extends Token {
	public int nameid;
}