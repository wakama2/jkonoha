package jkonoha;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import jkonoha.ast.*;
import jkonoha.compiler.*;

public class KonohaSpace extends KObject {

	public KonohaSpace parentNULL;
	public Tokenizer[] fmat;
	private final Map<String, Syntax> syntaxMapNN = new HashMap<String, Syntax>();
	private final Map<String, KClass> cl = new HashMap<String, KClass>();
	
	public KonohaSpace() {
		// konoha<->java class map
		cl.put("int", KClass.intClass);
		cl.put("Int", KClass.intClass);
		cl.put("boolean", KClass.booleanClass);
		cl.put("Boolean", KClass.booleanClass);
		cl.put("void", KClass.voidClass);
		cl.put("String", KClass.stringClass);
		cl.put("System", KClass.systemClass);
	}

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
	
	public Tokenizer[] tokenizerMatrix(CTX ctx) {
		//TODO
		return null;
	}	

	public void setTokenizer(int ch, Tokenizer f) {
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
			if (tk != null && (tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL)) {
				tk = lookAhead(ctx, tls, s+2, e);
				if(tk != null && (tk.tt == TK.AST_PARENTHESIS || tk.kw.equals(KW.DOT))) {
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
		defineSyntax(ctx, Syntax.defaultSyntax);
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
				   checkNestedSyntax(ctx, tls, ia, e, TK.AST_BRANCET, '[', ']') ||
				   checkNestedSyntax(ctx, tls, ia, e, TK.AST_BRACE, '{', '}')) {
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
		List<Token> tls = new ArrayList<Token>();
		tokenize(ctx, rule, uline, tls);
		makeSyntaxRule(ctx, tls, 0, tls.size(), a);
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
	
	public KClass getClass(CTX ctx, String key) {
		return cl.get(key);
	}

	public void setSyntaxMethod(CTX ctx, KMethod f, KMethod[] synp, KMethod p, KMethod[] mp) {
		//TODO
	}

	public KObject eval(CTX ctx, String script, long uline) {
		ctx.gamma.ks = this;
		List<Token> tls = new ArrayList<Token>();
		int pos = tls.size();
		tokenize(ctx, script, uline, tls);
		Token.dumpTokenArray(System.out, tls);
		Block bk = Parser.newBlock(ctx, this, null, tls, pos, tls.size(), ';');
		KArray.clear(tls, pos);
		return evalBlock(ctx, bk);
	}

	private KObject evalBlock(CTX ctx, Block bk) {
		CompilerContext cc = new CompilerContext(ctx);
		ctx.gamma.cc = cc;
		if(!bk.tyCheckAll(ctx, ctx.gamma)) return null;
		cc.evalBlock(bk);
		try {
			cc.writeClassFile(".");
			LocalCtx.set(ctx);
			// exec
			ClassLoader cl = cc.createClassLoader();
			Class<?> c = cl.loadClass("Script");
			Object r = c.getMethod("main").invoke(null);
			if(r != null) {
				return (KObject)r;
			} else {
				return null;
			}
		} catch(InvocationTargetException e) {
			e.getCause().printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean _import(String name) {
		return importPackage(name);
	}
	
	public static boolean importPackage(String name) {
		CTX ctx = LocalCtx.get();
		// package ?
		try {
			Class<?> c = Class.forName(name + ".package-info");
			KonohaPackageAnnotation an = c.getAnnotation(KonohaPackageAnnotation.class);
			if(an != null) {
				KonohaPackage kp = an.getInitClass().newInstance();
				kp.init(ctx, ctx.ks);
				return true;
			}
		} catch(Exception e) {
		}
		// class ?
		try {
			Class<?> c = Class.forName(name);
			ctx.ks.cl.put(c.getSimpleName(), JavaClass.create(c));
			return true;
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean loadScript(CTX ctx, String path) {
		//TODO
		return false;
	}

}
