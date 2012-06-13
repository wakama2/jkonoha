package jkonoha;

import java.util.*;

import jkonoha.compiler.CompilerContext;

public class KonohaSpace extends KObject {

	public KonohaSpace parentNULL;
	public FTokenizer[] fmat;
	public Map<String, Syntax> syntaxMapNN = new HashMap<String, Syntax>();

	public void tokenize(CTX ctx, String source, long uline, List<Token> toks) {
		int i, pos = toks.size();
		TEnv tenv = new TEnv(source, uline, toks, 4, this);
		Tokenizer.tokenize(ctx, tenv);
		if(uline == 0) {
			for(i = pos; i < toks.size(); i++) {
				toks.get(i).uline = 0;
			}
		}
	}	
	
	public FTokenizer[] tokenizerMatrix(CTX ctx) {
		//TODO
		return null;
	}	

	public void setTokenizer(int ch, FTokenizer f) {
		//TODO
	}

	private Token lookAhead(CTX ctx, List<Token> tls, int s, int e)
	{
		return (s < e) ? tls.get(s) : null/*K_NULLTOKEN*/;
	}
	
	public Syntax getSyntaxRule(CTX ctx, List<Token> tls, int s, int e) {
		Token tk = tls.get(s);
		if (tk.kw.equals(KW.Type)) {
			tk = lookAhead(ctx, tls, s+1, e);
			if (tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL) {
				tk = lookAhead(ctx, tls, s+2, e);
				if(tk.tt == TK.AST_PARENTHESIS || tk.kw.equals(KW.DOT)) {
					return syntax(ctx, KW.StmtMethodDecl); //
				}
				return syntax(ctx, KW.StmtTypeDecl);  //
			}
			return syntax(ctx, KW.Expr);  // expression
		}
		Syntax syn = syntax(ctx, tk.kw);
		if(syn.syntaxRuleNULL == null) {
			ctx.DBG_P("kw='%s', %d, %d", syn.kw, 0, 0);//TODO syn.ParseExpr == kmodsugar.UndefinedParseExpr, kmodsugar.UndefinedExprTyCheck == syn.ExprTyCheck);
			int i;
			for(i = s + 1; i < e; i++) {
				tk = tls.get(i);
				syn = syntax(ctx, tk.kw);
				if(syn.syntaxRuleNULL != null && syn.priority > 0) {
					ctx.SUGAR_P(System.out, tk.uline, tk.lpos, "binary operator syntax kw='%s'", syn.kw);
					return syn;
				}
			}
			return syntax(ctx, KW.Expr);
		}
		return syn;
	}
	
	public void defineDefaultSyntax(CTX ctx) {
		Syntax[] s = {
				new ERRSyntax(),
				new ExprSyntax(),
				new SYMBOLSyntax(),
				new USYMBOLSyntax(),
				new TextSyntax(),
				new IntSyntax(),
				new FloatSyntax(),
				new TypeSyntax(),
				new AST_ParenthesisSyntax(),
				new AST_BracketSyntax(),
				new AST_BraceSyntax(),
				new BlockSyntax(),
				new ParamsSyntax(),
				new ToksSyntax(),
				new DotSyntax(),
				new DivSyntax(),
				new ModSyntax(),
				new MulSyntax(),
				new AddSyntax(),
				new SubSyntax(),
				new LTSyntax(),
				new LTESyntax(),
				new GTSyntax(),
				new GTESyntax(),
				new EQSyntax(),
				new NEQSyntax(),
				new ANDSyntax(),
				new ORSyntax(),
				new NOTSyntax(),
				new OPLEFTSyntax(),
				new COMMASyntax(),
				new DOLLARSyntax(),
				new VOIDSyntax(),
				new BOOLEANSyntax(),
				new INTTypeSyntax(),
				new TRUESyntax(),
				new FALSESyntax(),
				new IFSyntax(),
				new ELSESyntax(),
				new RETURNSyntax()
				//TODO
		};
		defineSyntax(ctx, s);
	}
	
	public Syntax syntax(CTX ctx, String kw) {
		KonohaSpace ks0 = this;
		KonohaSpace ks = ks0;
		while(ks != null) {
			if(ks.syntaxMapNN != null) {
				Syntax parent = ks.syntaxMapNN.get(kw);
				if(parent != null) {
					if(ks0 != ks) {
						break;
					}
					return parent;
				}
			}
			ks = ks.parentNULL;
		}
		//throw new RuntimeException("syntax not found: " + kw);
		return null;
	}
	
	private int findTopCh(CTX ctx, List<Token> tls, int s, int e, int tt, int closech) {
		for(int i=s; i<e; i++) {
			Token tk = tls.get(i);
			if(tk.tt == tt && tk.text.charAt(0) == closech) return i;
		}
		return e;
	}
	
	private boolean checkNestedSyntax(CTX ctx, List<Token> tls, int[] s, int e, int tt, int opench, int closech) {
		int i = s[0];
		Token tk = tls.get(i);
		String t = tk.text;
		if(t.length() == 1 && t.charAt(0) == opench) {
			int ne = findTopCh(ctx, tls, i+1, e, tk.tt, closech);
			tk.tt = tt;
			if(tt >= 0 && tt < KW.TK_KW.length) tk.kw = KW.TK_KW[tt];
			tk.sub = new ArrayList<Token>();
			tk.topch = opench;
			tk.closech = closech;
			makeSyntaxRule(ctx, tls, i+1, ne, tk.sub);
			s[0] = ne;
			return true;
		}
		return false;
	}
	
	private boolean makeSyntaxRule(CTX ctx, List<Token> tls, int s, int e, List<Token> adst) {
		String nameid = null;
		for(int i=s; i<e; i++) {
			Token tk = tls.get(i);
			if(tk.tt == TK.INDENT) continue;
			if(tk.tt == TK.TEXT) {
				int[] ia = new int[]{i};
				if(checkNestedSyntax(ctx, tls, ia, e, TK.AST_PARENTHESIS, '(', ')') ||
				   checkNestedSyntax(ctx, tls, ia, e, TK.AST_PARENTHESIS, '(', ')') ||
				   checkNestedSyntax(ctx, tls, ia, e, TK.AST_PARENTHESIS, '(', ')')) {
					i = ia[0];
				} else {
					tk.tt = TK.CODE;
					tk.kw = tk.text;
				}
				adst.add(tk);
				continue;
			}
			if(tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL) {
				if(i > 0 && tls.get(i-1).topch == '$') {
					tk.kw = "$" + tk.text;
					tk.tt = TK.METANAME;
					if(nameid == null) nameid = tk.kw;
					tk.nameid = nameid;
					nameid = null;
					adst.add(tk);
					continue;
				}
				if(i + 1 < e && tls.get(i+1).topch == ':') {
					nameid = tls.get(i).text;
					i++;
					continue;
				}
			}
			if(tk.tt == TK.OPERATOR) {
				int[] ia = new int[]{i};
				if(checkNestedSyntax(ctx, tls, ia, e, TK.AST_OPTIONAL, '[', ']')) {
					adst.add(tk);
					i = ia[0];
					continue;
				}
				if(tls.get(i).topch == '$') continue;
			}
			return false;
		}
		return true;
	}
	
	private void parseSyntaxRule(CTX ctx, String rule, long uline, List<Token> a) {
		List<Token> tls = ctx.ctxsugar.tokens;
		int pos = tls.size();
		tokenize(ctx, rule, uline, tls);
		makeSyntaxRule(ctx, tls, pos, tls.size(), a);
		KArray.clear(tls, pos);
	}

	public void defineSyntax(CTX ctx, Syntax[] syndef) {
		for(Syntax syn : syndef) {
			if(syn.rule != null) {
				syn.syntaxRuleNULL = new ArrayList<Token>();
				parseSyntaxRule(ctx, syn.rule, 0, syn.syntaxRuleNULL);
			}
			this.syntaxMapNN.put(syn.kw, syn);
		}
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
	
	public KObject eval(CTX ctx, String script, long uline) {
		ctx.modsugar.setup();
		List<Token> tls = new ArrayList<Token>();
		int pos = tls.size();
		tokenize(ctx, script, uline, tls);
		Token.dumpTokenArray(System.out,tls);
		Block bk = Parser.newBlock(ctx, this, null, tls, pos, tls.size(), ';');
		Token.dumpTokenArray(System.out,tls);
		return evalBlock(ctx, bk);
	}

	private KObject evalBlock(CTX ctx, Block bk) {
		bk.tyCheckAll(ctx, ctx.modsugar.gamma);
		CompilerContext cc = new CompilerContext(ctx);
		cc.evalBlock(bk);
		try {
			// exec
			ClassLoader cl = cc.createClassLoader();
			Class<?> c = cl.loadClass("Script");
			Object r = c.getMethod("main").invoke(null);
			if(r != null) {
				return (KObject)r;
			} else {
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
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
