package jkonoha;

import java.util.*;

/**
 * _kToken in original konoha2
 * @author okachin
 *
 */

public abstract class Token extends KObject {
	public int  tt;
	public int  kw;
	public long uline;
	
	public Token() {
		this.tt = 0;
		this.kw = 0;
	}
}

class RawToken extends Token {
	public String text;
	public int topch;
	
	public RawToken(long uline) {
		super();
		this.uline = uline;
		this.topch = 0;
	}
}

class AstToken extends Token {
	public ArrayList<Token> sub;
	public int topch;
	public int closech;
}

class TypeToken extends Token {
	public int ty;
}

class MethodToken extends Token {
	public int mn_type;
	public int mn;
}

class SyntaxRuleToken extends Token {
	public int nameid;
}
