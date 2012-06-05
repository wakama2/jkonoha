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
		assert(tk.tt == TK.NONE);
		tk.uline = tenv.uline;
//		tk.lpos = tenv.lpos(0);
		pos = Tokenizer.parseINDENT.parse(ctx, tk, tenv, pos, null);
		while(pos < tenv.source.length() && (ch = Tokenizer.kchar(tenv.source, pos)) != 0) {
			if(tk.tt != TK.NONE) {
				tenv.list.add(tk);
				tk = new Token(tenv.uline);
				tk.uline = tenv.uline;
				//tk.lpos = tenv.lpos(pos);
			}
			int pos2 = fmat[ch].parse(ctx, tk, tenv, pos, null);
			assert pos2 > pos;
			pos = pos2;
		}
		if(tk.tt != TK.NONE) {
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
		Token tk = tls.get(s);
		if (tk.kw == KW.Type) {
			//tk = lookAhead(ctx, tls, s+1, e);
			if (tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL) {
				//tk = TokenArray_lookAhead(_ctx, tls, s+2, e);
				
			}
		}
		//TODO
		return null;
	}
	
	public Syntax syntax(CTX ctx, String kw) {
		assert(this != null);/* scan-build: remove warning */
		Syntax parent = syntaxMapNN.get(kw);
		if (parent != null) {
			return parent;
		}
		//DBG_P("creating new syntax %s old=%p", T_kw(kw), parent);
		Syntax syn = new Syntax(); //TODO fix Syntax.java
		syn.kw = kw;
		syn.ty  = TY.unknown;
		syn.op1 = 0/*MN_NONAME*/;
		syn.op2 = 0/*MN_NONAME*/;
		syn.ParseExpr = ctx.modsugar.UndefinedParseExpr;
		syn.TopStmtTyCheck = ctx.modsugar.UndefinedStmtTyCheck;
		syn.StmtTyCheck = ctx.modsugar.UndefinedStmtTyCheck;
		syn.ExprTyCheck = ctx.modsugar.UndefinedExprTyCheck;
		syntaxMapNN.put(kw, syn);
		//syn.parent = parent;
		return syn;
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

/*	public void loadMethodData(CTX ctx, KonohaSpace ks, int data[]) {
		int d[] = data;
		while(d[0] != -1) {
			int flag = d[0];
			int f = d[1];
			int rtype = d[2];
			int cid  = d[3];
			int mn = d[4];
			int i, psize = d[5];
		    Param p[];
//			d = d + 6;
			int j = 6;
			for(i = 0; i < psize; i++) {
				p[i].ty = d[j];
				p[i].fn = d[j+1];
				j += 2;
			}
			KMethod mtd = new KMethod(ctx, flag, cid, mn, f);
			mtd.setParam(ctx, rtype, psize, p);
			if(ks == null || ((mtd.flag & KMethod.Public) == KMethod.Public)) {
				CT_addMethod(ctx, CT_(cid), mtd); //TODO CT_addMethod, CT_
			} else {
				addMethod(ctx, mtd);
			}
		}
	}*/
	
	/*public Kvs getConstNULL (CTX ctx, int un) {
		//TODO
		Kvs kvs = new Kvs();
		return kvs;
	}
	public int longid (int packdom, int un) {
		int hcode = packdom;
		return (hcode << (32*8)) | un;		//int is 32 bits.
	}*/
	public KClass getCT(CTX ctx, KClass thisct, String name, int len, int def) {//TODO
		//int PN_konoha = 1;//TODO PN_konoha is Macro.
		KClass ct = null;
		/*int un = kuname (name, len, 0, FN_NONAME);//Don't know FN_NONAME
		if (un != FN_NONAME) {
			int hcode = longid (PN_konoha, un);
			ct = map_getu (ctx, ctx.share.lcnameMapNN, hcode, 0);
			if (ct == null) {
				Kvs kvs = getConstNULL (ctx, un);
				//DBG_P ("kvs = %s, %p", name, kvs);
				if (kvs != null && kvs.ty == TY.TYPE) {
					return kvs.uval;
				}
			}
		}*/
		return ct;
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
		//evalBlock(ctx, bk);
	}

/*	private void evalBlock(CTX ctx, Block bk) {
		Block bk1 = ctx.ctxsugar.singleBlock;
		KMethod mtd = new KMethod(ctx, KMethod.Static, 0, 0, 0);
		mtd.setParam(TY.OBJECT, 0, null);
		int i, jmpresult;
		int result = K_CONTINUE;
		
		//TODO
	}*/

	public boolean importPackage(CTX ctx, String name, long pline) {
		//TODO
		return false;
	}

	public boolean loadScript(CTX ctx, String path) {
		//TODO
		return false;
	}

}
