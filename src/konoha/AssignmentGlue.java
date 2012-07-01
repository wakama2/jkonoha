package konoha;

import java.util.*;

import jkonoha.*;
import jkonoha.ast.*;

class AsnSyntax extends OpSyntax {
	public AsnSyntax(String kw) {
		super(kw);
		this.priority = 4096;
		this.flag = (SYNFLAG.ExprOp | SYNFLAG.ExprLeftJoinOp2);
	}
	@Override
	public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		int atop = tls.size();
		s = transformOprAssignment(ctx, tls, s, c, e);
		Expr expr = stmt.newExpr2(ctx, tls, s, tls.size());
		KArray.clear(tls, atop);
		return expr;
	}
	
	private int transformOprAssignment(CTX ctx, List<Token> tls, int s, int c, int e) {
		Token tkNew, tkNewOp;
		Token tmp, tkHead;
		int newc, news = e;
		int i = s;

		while (i < c) {
			tkNew = new Token();
			tmp = tls.get(i);
			setToken(tkNew, tmp.text, tmp.tt, tmp.topch, tmp.kw);
			tls.add(tkNew);
			i++;
		}
		//check operator
		tkNewOp = new Token();
		tmp = tls.get(c);
		String opr = tmp.text;
		String newopr = opr.substring(0,opr.length()-1);
		setToken(tkNewOp, newopr, tmp.tt, tmp.topch, newopr);

		tkNew = new Token();
		setToken(tkNew, "=", TK.OPERATOR, '=', KW.LET);
		tls.add(tkNew);
		newc = tls.size() - 1;

		Token newtk = new Token();
		tkHead = tls.get(e+1);
		newtk.tt = TK.AST_PARENTHESIS;
		newtk.kw = KW.TK_KW[TK.AST_PARENTHESIS];
		newtk.uline = tkHead.uline;
		newtk.sub = new ArrayList<Token>();
		i = news;

		while (i < newc) {
			tkNew = new Token();
			tmp = tls.get(i);
			setToken(tkNew, tmp.text, tmp.tt, tmp.topch, tmp.kw);
			newtk.sub.add(tkNew);
			i++;
		}
		tls.add(newtk);
		tls.add(tkNewOp);

		tkNew = new Token();
		i = c+1;
		while (i < news) {
			tkNew = new Token();
			tmp = tls.get(i);
			setToken(tkNew, tmp.text, tmp.tt, tmp.topch, tmp.kw);
			tls.add(tkNew);
			i++;
		}
		return news;
	}
	
	private void setToken(Token tk, String str, int t, int c, String k) {
		tk.text = str;
		tk.tt = t;
		tk.topch = c;
		tk.kw = k;
	}
}

public class AssignmentGlue implements KonohaPackageInitializer {

	private final Syntax asnSyntax = new OpSyntax("=") {
		{
			this.priority = 4096;
		}
		@Override
		public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
			Expr lexpr = expr.tyCheckAt(ctx, 1, gamma, ty, TPOL.ALLOWVOID);
			Expr rexpr = expr.tyCheckAt(ctx, 2, gamma, lexpr.ty, 0);
			if (rexpr != null && lexpr != null) {
				if (rexpr != null) {
					if (lexpr.build == TEXPR.LOCAL || lexpr.build == TEXPR.LOCAL_ || lexpr.build == TEXPR.FIELD) {
						expr.build = TEXPR.LET;
						rexpr.ty = lexpr.ty;
						return expr;
					}
					if (lexpr.build == TEXPR.CALL) {
						KonohaMethod mtd = (KonohaMethod)lexpr.cons.get(0);
						assert(mtd instanceof KonohaMethod);
						//TODO					if (/* MN_isGETTER(mtd->mn) || MN_isISBOOL(mtd->mn) &&*/ !mtd.isStatic()){
						//						KClass cid = (KClass)lexpr.cons.get(1);
						//						mtd = kKonohaSpace_getMethodNULL(gma->genv->ks, cid, MN_toSETTER(mtd->mn));
						if (mtd != null) {
							//								lexpr.cons.set(0, mtd);
							lexpr.cons.add(rexpr);
							return lexpr;
						}
						//					}
					}
					System.out.println("varriable name is expected.");
				}
			}
			return null;
		}
	};

	private final Syntax addasnSyntax = new AsnSyntax("+=");
	private final Syntax subasnSyntax = new AsnSyntax("-=");
	private final Syntax mulasnSyntax = new AsnSyntax("*=");
	private final Syntax divasnSyntax = new AsnSyntax("/=");
	private final Syntax modasnSyntax = new AsnSyntax("%=");

	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef = {
				asnSyntax,
				addasnSyntax,
				subasnSyntax,
				mulasnSyntax,
				divasnSyntax,
				modasnSyntax
		};
		ks.defineSyntax(ctx, syndef);
	}
}
