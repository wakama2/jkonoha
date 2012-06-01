package jkonoha;

import java.util.*;

public class KonohaSpace extends KObject {
	
	public KonohaSpace parentNULL;
	public FTokenizer[] fmat;
	public Map<String, Syntax> syntaxMapNN = new HashMap<String, Syntax>();
	
	public KonohaSpace() {
		//TODO
	}
	
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
	
	private void tokenize(CTX ctx, TEnv tenv) {
		int ch, pos = 0;
		FTokenizer fmat[] = tenv.fmat;
		Token tk = new Token(tenv.uline);
		assert(tk.tt == 0);
		tk.uline = tenv.uline;
//		tk.lpos = tenv.lpos(0);
		pos = Tokenizer.parseINDENT.parse(ctx, tk, tenv, pos, null);
		while(pos < tenv.source.length() && (ch = Tokenizer.kchar(tenv.source, pos)) != 0) {
			if(tk.tt != 0) {
				tenv.list.add(tk);
				tk = new Token(tenv.uline);
				tk.uline = tenv.uline;
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
	
	public Syntax syntax(CTX ctx, int kw, int isnew) {
		//TODO
		return null;
	}
	
	public void defineSyntax(CTX ctx, Syntax[] syndef) {
		//TODO
	}
	
	public void setSyntaxMethod(CTX ctx, KMethod f, KMethod[] synp, KMethod p, KMethod[] mp) {
		//TODO
	}
	
	public void addMethod(CTX ctx, KMethod mtd) {
		//TODO
	}
	
	public KMethod getMethodNULL(CTX ctx, int cid, String mn) {
		//TODO
		return null;
	}
	
	public KMethod getStaticMethodNULL(CTX ctx, String mn) {
		//TODO
		return null;
	}
	
	public boolean defineMethod(CTX ctx, KMethod mtd, long pline) {
		//TODO
		return false;
	}
	
	public void loadMethodData(CTX ctx, Object data) {
		//TODO
	}
	
	public KClass getCT(CTX ctx, KClass thisct, String name, int def) {
		//TODO
		return null;
	}
	
	public void eval(CTX ctx, String script, long uline) {
		List<Token> tls = new ArrayList<Token>();
		int pos = tls.size();
		tokenize(ctx, script, uline, tls);
		
		// debug: dump tokens
		for(int i = 0; i < tls.size(); i++) {
			Token rtk = tls.get(i);
			System.out.print("{ token type:" + rtk.tt + ", ");
			if(rtk.text != null) {
				System.out.print("text: " + rtk.text + ", ");
			}
			else {
				System.out.print("text: null, ");
			}
			System.out.println("uline: " + rtk.uline + " }");
		}
		
		Parser p = new Parser();
		Block bk = p.newBlock(ctx, this, null, tls, pos, tls.size(), ';');
		evalBlock(ctx, bk);
	}
	
	private void evalBlock(CTX ctx, Block bk) {
		//TODO
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

