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
	
}

class RawToken extends Token {
	public String text;
	public int topch;
	
	public RawToken(long uline){
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
