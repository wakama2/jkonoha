package konoha;

import java.util.*;
import jkonoha.compiler.*;

import jkonoha.*;
import jkonoha.ast.*;

public class ArrayGlue implements KonohaPackageInitializer {
	
	private final Syntax bracketSyntax = new Syntax("[]") {
		{
			this.flag = SYNFLAG.ExprPostfixOp2;
			this.priority = 16;
		}
		@Override
		public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
			Token tkBRACKET = tls.get(c);
			if(s == c) { // TODO
				Expr expr = stmt.newExpr2(ctx, tkBRACKET.sub, 0, tkBRACKET.sub.size());
				return expr;
			}
			else {
				Expr lexpr = stmt.newExpr2(ctx, tls, s, c);
				if(lexpr == null) {
					return lexpr;
				}
				if(lexpr.syn.kw.equals(KW._new)) { // new int[100]
					lexpr.syn = stmt.parentNULL.ks.syntax(ctx, KW.ExprMethodCall);
					lexpr = stmt.addExprParams(ctx, lexpr, tkBRACKET.sub, 0, tkBRACKET.sub.size(), false/* allowEmpty */);
				}
				else { // X[1] => get X 1
					Token tkN = new Token();
					tkN.tt = TK.MN;
					tkN.mn = "get";
					tkN.uline = tkBRACKET.uline;
					Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW.ExprMethodCall);
					Expr expr = new Expr(ctx, syn, tkN, tkN.ty, 0);
					lexpr = new Expr(syn);
					lexpr.setCons(tkN, expr);
					lexpr = stmt.addExprParams(ctx, lexpr, tkBRACKET.sub, 0, tkBRACKET.sub.size(), false/* allowEmpty */);
				}
				return lexpr;
			}
		}
	};
	
	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef= {
				bracketSyntax,
		};
		ks.defineSyntax(ctx, syndef);
		ks.addClass("IntArray", JavaClass.create(IntArray.class));
		ks.addClass("FloatArray", JavaClass.create(FloatArray.class));
		ks.addClass("BooleanArray", JavaClass.create(BooleanArray.class));
		ks.addClass("ObjectArray", JavaClass.create(ObjectArray.class));
	}
	
}