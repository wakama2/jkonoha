package jkonoha;

import java.io.PrintStream;
import java.util.*;

public class Token extends KObject {
	public int tt;
	public String kw = KW.Err; // default is 0
	public long uline;
	public int lpos;
	public String text;
	public int topch;
	public ArrayList<Token> sub;
	public int ty;
	public int closech;
	public int mn_type;
	public String mn;
	public String nameid;
	
	public Token() {
		this(0);
	}
	
	public Token(long uline) {
		this.uline = uline;
	}
	
	public boolean resolved(CTX ctx, KonohaSpace ks) {//Token_resolved in Parser.java
		String kw = this.text;
		if(kw != null) {//"dummy"/*FN_NONAME*/) {
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
	
	@Override public String toString() {
		switch(this.tt) {
		case TK.INDENT: return "end of line";
		case TK.CODE:
		case TK.AST_BRACE: return "{... }";
		case TK.AST_PARENTHESIS: return "(... )";
		case TK.AST_BRANCET: return "[... ]";
		default: return this.text;
		}
	}
	
	public static String ttToStr(int t) {
		String[] symTKDATA = {
			"TK_NONE",
			"TK_INDENT",
			"TK_SYMBOL",
			"TK_USYMBOL",
			"TK_TEXT",
			"TK_INT",
			"TK_FLOAT",
			"TK_TYPE",
			"AST_()",
			"AST_[]",
			"AST_{}",

			"TK_OPERATOR",
			"TK_MSYMBOL",
			"TK_ERR",
			"TK_CODE",
			"TK_WHITESPACE",
			"TK_METANAME",
			"TK_MN",
			"AST_OPTIONAL[]",
		};
		if(t <= TK.AST_OPTIONAL) {
			return symTKDATA[t];
		}
		return "TK_UNKNOWN";
	}
	
	public void dump(PrintStream out) {
		if(this.tt == TK.MN) {
			out.printf("%s %d+%d: %s(%s)\n", ttToStr(this.tt), this.uline, this.lpos, this.mn, this.toString());
		} else {
			out.printf("%s %d+%d: kw=%s '%s'\n", ttToStr(this.tt), this.uline, this.lpos, this.kw, this.toString());
		}
	}
	
	public static void dumpIndent(PrintStream out, int nest) {
		for(int i=0; i<nest; i++) {
			out.print(" ");
		}
	}
	
	public static void dumpTokenArray(PrintStream out, List<Token> a) {
		dumpTokenArray(out, 0, a, 0, a.size());
	}
	
	public static void dumpTokenArray(PrintStream out, int nest, List<Token> a, int s, int e) {
		if(nest == 0) out.println();
		while(s < e) {
			Token tk = a.get(s);
			dumpIndent(out, nest);
			if(tk.sub != null) {
				out.println((char)tk.topch);
				dumpTokenArray(out, nest+1, tk.sub, 0, tk.sub.size());
				dumpIndent(out, nest);
				out.println((char)tk.closech);
			} else {
				out.printf("TK(%d) ", s);
				tk.dump(out);
			}
			s++;
		}
		if(nest == 0) out.println("====");
	}

	public boolean toBRACE(CTX ctx, KonohaSpace ks) {
		if(this.tt == TK.CODE) {
			ArrayList<Token> a = new ArrayList<Token>();
			ks.tokenize(ctx, this.text, this.uline, a);
			this.tt = TK.AST_BRACE; this.topch = '{'; this.closech = '}';
			this.sub = a;
			return true;
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
