package konoha;

import java.util.List;

import jkonoha.CTX;
import jkonoha.KClass;
import jkonoha.KMethod;
import jkonoha.KonohaSpace;
import jkonoha.SPOL;
import jkonoha.SYM;
import jkonoha.ast.*;
import jkonoha.compiler.CompilerContext;

class NewSyntax extends Syntax {
	public NewSyntax() {
		super("new");
	}

	@Override
	public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		assert(s == c);
		Token tkNEW = tls.get(s);
		if(s + 2 < tls.size()) {
			Token tk1 = tls.get(s+1);
			Token tk2 = tls.get(s+2);
			if(tk1.kw.equals(KW.Type) && tk2.tt == TK.AST_PARENTHESIS) {  // new C (...)
				Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW.ExprMethodCall);
				Expr nexpr = new Expr(ctx, syn, tk1, tk1.ty, 0);
				Expr expr = new Expr(syn);
				expr.setCons(tkNEW, nexpr);
				return expr;
			}
//			if(tk1.kw.equals(KW.Type) && tk2.tt == TK.AST_BRANCET) {     // new C [...]
//				Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW._new);
//				KClass ct = CT_p0(ctx, CT_Array, tk1.ty);
//				tkNEW.setmn(ctx.Ksymbol2(ctx, "newArray", "newArray".length(), SPOL.TEXT|SPOL.ASCII, SYM.NEWID), MNTYPE.method);
//				Expr nexpr = new Expr(ctx, syn, tk1, ct.cid, 0); //TODO cid : unsigned int
//				Expr expr = new Expr(syn);
//				expr.setCons(tkNEW, nexpr);
//				return expr;
//			}
		}
		return null; //this is dummy
	}
}

class ClassSyntax extends Syntax {
	public ClassSyntax() {
		super("class");
		this.rule = "\"class\" $USYMBOL [\"extends\" extends: $USYMBOL] $block";
	}

	@Override
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		Token tkC = stmt.token(ctx, KW.Usymbol, null);
		Token tkE= stmt.token(ctx, "extends", null);
		int cflag = 0;
		int supcid = TY.OBJECT;
		KClass supct = KClass.objectClass;
		if (tkE != null) { //TODO if(tkE)
			assert(KW.TK_KW[tkE.tt].equals(KW.Usymbol));
			supcid = tkE.ty;
			supct = CT_(supcid); //(_ctx->share->ca.cts[t])
			if((((int)(supct.cflag) & (int)(KClass.Final)) == (int)(KClass.Final))) {
				ctx.SUGAR_P(System.err, 0, -1,  "%s is final", CT_t(supct)); //TODO (...,0, -1, ...) is correct?
				return false;
			}
			if(!(((int)(supct.cflag) & (int)(KClass.Final)) == (int)(KClass.Final))) {
				ctx.SUGAR_P(System.err, 0, -1, "%s has undefined field(s)", CT_t(supct)); //TODO (...,0, -1, ...) is correct?
				return false;
			}
			/*#define CT_t(X)   S_text(CT_s_(_ctx, X))
			static kinline kString* CT_s_(CTX, kclass_t *ct)
			{
				return _ctx->lib2->KCT_shortName(_ctx, ct);
			}*/
		}
		KClass ct = defineClassName(ctx, gamma, gamma.genv.ks, cflag, tkC.text, supcid, stmt.uline);
		((Token)tkC).kw = KW.Type;
		((Token)tkC).ty = ct.cid;
		stmt.parseClassBlock(ctx, tkC);
		Block dummy; // this is dummy
		Block bk = stmt.block(ctx, KW.Block, dummy/*K.NULLBLOCK*/);
		CT_setField(ctx, ct, supct, checkFieldSize(ctx, bk));
		if(!CT_addClassFields(ctx, ct, gamma, bk, stmt.uline)) {
			return false;
		}
		stmt.syntax = null;
		CT_checkMethodDecl(ctx, tkC, bk, stmt);
		return true;
	}


	private int checkFieldSize(CTX ctx, Block bk)
	{
		int i, c = 0;
		for(i = 0; i < bk.blocks.size(); i++) {
			Stmt stmt = bk.blocks.get(i);
			DBG_P("stmt.kw=%s", KW_t(stmt.syntax.kw));
			if(stmt.syntax.kw.equals(KW.StmtTypeDecl)) {
				Expr expr = stmt.expr(ctx, KW.Expr, null);
				if(expr.syn.kw.equals(KW.COMMA)) {
					c += (expr.cons.size() - 1);
				}
				else if(expr.syn.kw == KW.LET || Expr_isTerm(expr)) {
					c++;
				}
			}
		}
		return c;
	}

	private KClass defineClassName(CTX ctx, Gamma gamma, KonohaSpace ks, int cflag, String name, int supcid, long pline)
	{
		KonohaClass KDEFINE_CLASS = new KonohaClass("KDEFINE_CLASS");
		gamma.cc.addClass(KDEFINE_CLASS);
		KDEFINE_CLASS defNewClass = {
				.cflag  = cflag,
						.cid    = CLASS_newid,
						.bcid   = CLASS_Object,
						.supcid = supcid,
						//		.init   = ObjectField_init,
		};
		KClass ct = Konoha_addClassDef(ks.packid, ks.packdom, name, defNewClass, pline);
		KDEFINE_CLASS_CONST ClassData[] = {
				{name, TY.TYPE, ct},
				{null},
		};
		kKonohaSpace_loadConstData(ks, ClassData, 0); // add class name to this namespace
		//	kMethod *mtd = new_kMethod(_Public/*flag*/, ct.cid, MN_new, NULL);
		//	kMethod_setParam(mtd, ct.cid, 0, NULL);
		//	CT_addMethod(_ctx, ct, mtd);
		return ct;
	}
}

class ExtendsSyntax extends Syntax {//Joseph
	public ExtendsSyntax() {
		super("extends");
		this.rule = "\"extends\" $USYMBOL";
	}
}

class DotSyntax extends Syntax {//Joseph
	public DotSyntax() {
		super(".");
	}

	@Override
	public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		//in /package/konoha/class_glue.h:271
		Object o = expr.cons.get(0);
		Token tkN;
		if (o instanceof Token) {
			tkN = (Token)o;
			//int fn = tosymbolUM(ctx, tkN);//TODO
			Expr self = expr.tyCheckAt(ctx, 1, gamma, ty.varClass, 0);
			if (self != null) {
//				KonohaClass klass = ctx.scriptClass;
//				KonohaMethod mtd = (KonohaMethod)klass.getMethod(MN_toGETTER(fn), self.ty);//TODO
//				if (mtd == null) {
//					mtd = (KonohaMethod)klass.getMethod(MN_toISBOOL(fn), self.ty);//TODO
//				}
//				if (mtd != null) {
//					expr.cons.set(0, mtd);
//					return expr.tyCheckCallParams(ctx, stmt, mtd, gamma, reqty);//TODO
//				}
			}
			System.out.println("undefined field: " + tkN.text);
		}
		return null;
	}
	private void tosymbolUM (CTX ctx, Token tk) {//TODO
		assert(tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL || tk.tt == TK.MSYMBOL);
		//return ctx.Ksymbol2(tk.text);//TODO in src/konoha/klibexec.h: 339
	}
}