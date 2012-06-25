package konoha;

import jkonoha.CTX;
import jkonoha.KClass;
import jkonoha.ast.*;

class NewSyntax extends Syntax {
	public NewSyntax() {
		super("new");
		// TODO Auto-generated constructor stub
	}
}

class ClassSyntax extends Syntax {
	public ClassSyntax() {
		super("class");
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		//in /package/konoha/class_glue.h:271
		Object o = expr.cons.get(0);
		Token tkN;
		if (o instanceof Token) {
			tkN = (Token)o;
			int fn = tosymbolUM(ctx, tkN);//TODO
			Expr self = expr.tyCheckAt(ctx, 1, gamma, ty.varClass, 0);
			if (self != null) {
				KonohaClass klass = ctx.scriptClass;
				KonohaMethod mtd = (KonohaMethod)klass.getMethod(MN_toGETTER(fn), self.ty);//TODO
				if (mtd == null) {
					mtd = (KonohaMethod)klass.getMethod(MN_toISBOOL(fn), self.ty);//TODO
				}
				if (mtd != null) {
					expr.cons.set(0, mtd);
					return expr.tyCheckCallParams(ctx, stmt, mtd, gamma, reqty);
				}
			}
			System.out.println("undefined field: " + tkN.text);
		}
		return null;
	}
	private int tosymbolUM (CTX ctx, Token tk) {
		assert(tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL || tk.tt == TK.MSYMBOL);
		return ctx.Ksymbol2(tk.text);//TODO in src/konoha/klibexec.h: 339
	}
}
