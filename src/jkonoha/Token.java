package jkonoha;

import java.util.*;

public class Token extends KObject {
	public String tt;
	public String kw;
	public long uline;
	public int lpos;
	public String text;
	public int topch;
	public ArrayList<Token> sub;
	public int ty;
	public int closech;
	public int mn_type;
	public int mn;
	public int nameid;
	
	public Token() {
		
	}
	
	public Token(long uline) {
		this.uline = uline;
	}
	boolean resolved(CTX ctx, KonohaSpace ks) {//Token_resolved in Parser.java
		String kw = "dummy"/*keyword(ctx, S_text(tk.text), S_size(tk.text), FN_NONAME)*/;
		if(kw != "dummy"/*FN_NONAME*/) {
			Syntax syn = ks.syntax(ctx,kw);
			if(syn != null) {
				if(syn.ty != TY.unknown) {//#define TY_unknown ((kcid_t)-2)
					this.kw = KW.Type; this.ty = syn.ty;
				}
				else {
					this.kw = kw;
				}
				return true;
			}
		}
		return false;
	}
}

//import java.util.*;
//
///**
// * _kToken in original konoha2
// * @author okachin
// *
// */
//
//public abstract class Token extends KObject {
//	public int  tt;
//	public int  kw;
//	public long uline;
//	
//	public int lpos;//use?
//	
//	public Token() {
//		this.tt = 0;
//		this.kw = 0;
//	}
//}
//
//class RawToken extends Token {
//	public String text;
//	public int topch;
//	
//	public RawToken(long uline) {
//		super();
//		this.uline = uline;
//		this.topch = 0;
//	}
//}
//
//class AstToken extends Token {
//	public ArrayList<Token> sub;
//	public int topch;
//	public int closech;
//}
//
//class TypeToken extends Token {
//	public int ty;
//}
//
//class MethodToken extends Token {
//	public int mn_type;
//	public int mn;
//}
//
//class SyntaxRuleToken extends Token {
//	public int nameid;
//}
