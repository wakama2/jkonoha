package konoha;

import java.util.*;
import jkonoha.*;
import jkonoha.ast.*;

public class ClassGlue implements KonohaPackageInitializer {
	
	private final Syntax newSyntax = new Syntax("new") {
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
				if(tk1.kw.equals(KW.Type) && tk2.tt == TK.AST_BRANCET) {     // new C [...]
					Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW._new);
					KClass ct = KClass.arrayClass;//CT_p0(ctx, CT_Array, tk1.ty);//TODO
					tkNEW.setmn("newArray", MNTYPE.method);
					Expr nexpr = new Expr(ctx, syn, tk1, ct, 0); //TODO cid : unsigned int
					Expr expr = new Expr(syn);
					expr.setCons(tkNEW, nexpr);
					return expr;
				}
			}
			return null;
		}
	};
	
	private final Syntax classSyntax = new Syntax("class") {
		{
			this.rule = "\"class\" $USYMBOL [\"extends\" extends: $USYMBOL] $block";
		}
	
		@Override
		public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
			Token tkC = stmt.token(ctx, KW.Usymbol, null);
			Token tkE= stmt.token(ctx, "extends", null);
			KClass supct = KClass.objectClass;
			List<KClass> ifct = new ArrayList<KClass>();
			if (tkE != null) { //TODO if(tkE)
				assert(KW.TK_KW[tkE.tt].equals(KW.Usymbol));
				supct = tkE.ty;
				if(supct.isFinal()) {
					ctx.SUGAR_P(System.err, 0, -1,  "%s is final", supct.getName()); //TODO (...,0, -1, ...) is correct?
					return false;
				}
				if(supct.isFinal()) {
					ctx.SUGAR_P(System.err, 0, -1, "%s has undefined field(s)", supct.getName()); //TODO (...,0, -1, ...) is correct?
					return false;
				}
			}
			//KClass ct = defineClassName(ctx, gamma, gamma.ks, cflag, tkC.text, supcid, stmt.uline);
			KonohaClass ct = new KonohaClass(tkC.text, supct, ifct.toArray(new KClass[0]));
			gamma.cc.addClass(ct);
			gamma.ks.addClass(tkC.text, ct);
			tkC.kw = KW.Type;
			tkC.ty = ct;
			stmt.parseClassBlock(ctx, tkC);
			Block bk = stmt.block(ctx, KW.Block, null);
	//		CT_setField(ctx, ct, supct, checkFieldSize(ctx, bk));
	//		if(!CT_addClassFields(ctx, ct, gamma, bk, stmt.uline)) {
	//			return false;
	//		}
			stmt.syntax = null;
			for(Stmt s : bk.blocks) {
				stmt.parentNULL.blocks.add(s);
			}
	//		checkMethodDecl(ctx, tkC, bk);
			
			return true;
		}
		
//		private int checkFieldSize(CTX ctx, Block bk) {
//			int i, c = 0;
//			for(i = 0; i < bk.blocks.size(); i++) {
//				Stmt stmt = bk.blocks.get(i);
//				DBG_P("stmt.kw=%s", KW_t(stmt.syntax.kw));
//				if(stmt.syntax.kw.equals(KW.StmtTypeDecl)) {
//					Expr expr = stmt.expr(ctx, KW.Expr, null);
//					if(expr.syn.kw.equals(KW.COMMA)) {
//						c += (expr.cons.size() - 1);
//					}
//					else if(expr.syn.kw == KW.LET || Expr_isTerm(expr)) {
//						c++;
//					}
//				}
//			}
//			return c;
//		}
	
	};

	private final Syntax extendsSyntax = new Syntax("extends") {
		//TODO
	};

	private final Syntax dotSyntax = new Syntax(".") {
		// TODO
	};	
	
	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef= {
				newSyntax,
				classSyntax,
				extendsSyntax,
				dotSyntax,
		};
		ks.defineSyntax(ctx, syndef);
	}
	
}