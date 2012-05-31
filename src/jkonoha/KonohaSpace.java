package jkonoha;

import java.util.*;

public class KonohaSpace extends KObject {
	
	public void tokenize(CTX ctx, String source, long uline, List<Token> toks) {
		int i, pos = toks.size();
		TEnv tenv = new TEnv(source, uline, toks, 4, this);
		tokenize(ctx, tenv);
		if(uline == 0) {
			for(i = pos; i < toks.size(); i++) {
				toks.get(i).uline = 0;
			}
		}
	}
	
	public void eval(CTX ctx, String script, long uline) {
		// TODO
		List<Token> tls = new ArrayList<Token>();
		tokenize(script, uline, tls);
		Block bk = null;//newBlock();
		//cctx.evalBlock(bk);
	}
	
	public void tokenize(String source, long uline, List<Token> a) {
		//TODO
	}
	
	public boolean importPackage(String name, long pline) {
		//TODO
		return false;
	}
	
	public boolean loadScript(String path) {
		//TODO
		return false;
	}


	public Syntax getSyntaxRule(CTX ctx, Stmt stmt, List<Object> tls, int s, int e) {
		//TODO
		Token tk = (Token)tls.get(s);
		if (tk.kw == KW.Type) {
			
		}
		return null;
	}
}

class TEnv {
	
	public String source;
	public long uline;
	public ArrayList<Token> list;
	public int bol;   // begin of line
	public int indent_tab;
	public FTokenizer[] fmat;
	
	/**
	 * constructer
	 * sourcecode
	 * @param source
	 * which line
	 * @param uline
	 * tokens
	 * @param a
	 * indent
	 * @param indent_tab
	 * parser
	 * @param fmat
	 */
	
	TEnv(String source, long uline, ArrayList<Token> a, int indent_tab, KonohaSpace ks) {
		this.source = source;
		this.uline = uline;
		this.list = a;
		this.bol = 0;
		this.indent_tab = indent_tab;
		this.fmat = Tokenizer.MiniKonohaTokenMatrix(); // TODO
	}
	
//	/**
//	 * This method is used to know the position of line.
//	 * @param pos
//	 * @return int
//	 */
//	
//	public final int lpos(int pos) {
//		return (this.bol == 0) ? -1 : (int)(pos - this.bol);
//	}
}

final class ParseINDENT implements FTokenizer {
	
	@Override public final int parse(CTX ctx,  Token tk, TEnv tenv, int pos, Method thunk) {
		int ch, c = 0;
//		while((ch = tenv.source.charAt(pos++)) != 0) {
		while(true) {
			pos++;
			if(pos >= tenv.source.length()) break;
			if((ch = tenv.source.charAt(pos)) == 0) break;
			if(ch == '\t') { c += tenv.indent_tab; }
			else if(ch == ' ') { c += 1; }
			break;
		}
		if(tk != null/* TODO IS_NOTNULL(tk) */) {
			tk.tt = Token.TK_INDENT;
//			tk.lpos = 0;		
		}
		return pos - 1;
	}
}

final class ParseNL implements FTokenizer {
	
	@Override public final int parse(CTX ctx,  Token tk, TEnv tenv, int pos, Method thunk) {
		tenv.uline += 1;
		tenv.bol = pos + 1;
		return Tokenizer.parseINDENT.parse(ctx, tk, tenv, pos + 1, thunk);
	}
}

final class ParseNUM implements FTokenizer {
	
	@Override public final int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, Method thunk) {
		int ch, pos = tok_start, dot = 0;
		String ts = tenv.source;
//		while((ch = ts.charAt(pos++)) != 0) {
		while(true) {
			pos++;
			if(pos >= ts.length()) break;
			if((ch = ts.charAt(pos)) == 0) break;
			if(ch == '_') continue; // nothing
			if(ch == '.') {
				if(!Character.isDigit(ts.charAt(pos))) {
					break;
				}
				dot++;
				continue;
			}
			if((ch == 'e' || ch == 'E') && (ts.charAt(pos) == '+' || ts.charAt(pos) == '-')) {
				pos++;
				continue;
			}
			int pos2 = fmat[ch].parse(ctx, tk, tenv, pos, null);
			assert pos2 > pos;
			pos = pos2;
		}
		if(tk.tt != 0) {
			tenv.list.add(tk);
		}
	}
	
	public FTokenizer[] tokenizerMatrix(CTX ctx) {
		//TODO
		return null;
	}
	
	public void setTokenizer(int ch, FTokenizer f, KMethod mtd) {
		//TODO
	}
	
	public Syntax getSyntaxRule(CTX ctx, List<Token> tls, int s, int e) {
		//TODO
		return null;
	}
	
	public void eval(CTX ctx, String script, long uline) {
		List<Token> tls = new ArrayList<Token>();
		tokenize(ctx, script, uline, tls);
		
		Parser p = new Parser();
		int pos = tls.size();
		Block bk = p.newBlock(ctx, this, null, tls, pos, tls.size(), ';');
		evalBlock(ctx, bk);
	}
	
	private void evalBlock(CTX ctx, Block bk) {
		//Block bk1 = ctx.sugar.singleBlock;
		//KMethod mtd = null;//TODO
	}
	
	public boolean importPackage(CTX ctx, String name, long pline) {
		//TODO
		return false;
	}
	
	public boolean loadScript(CTX ctx, String path) {
		//TODO
		return false;
	}
	
}

