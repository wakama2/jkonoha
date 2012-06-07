package jkonoha;

import java.util.*;

public class Parser {
	
	public static Block newBlock(CTX ctx, KonohaSpace ks, Stmt parent, List<Token> tls, int s, int e, int delim) {
		Block bk = new Block(ctx, ks);
		if (parent != null) {
			bk.parentNULL = parent;
		}
		int i = s, atop = tls.size();
		int[] indent = new int[]{0};
		while (i < e) {
			Token tkERR = null;
			assert (atop == tls.size());
			i = selectStmtLine (ctx, ks, indent, tls, i, e, delim, tls, tkERR);//TODO How to tkERR?
			int asize = tls.size();
			if (asize > atop) {
				bk.addStmtLine(ctx, tls, atop, asize, tkERR);
				tls.remove(atop);
			}
		}
		return bk;
	}

	public static int makeTree(CTX ctx, KonohaSpace ks, int tt, List<Token> tls, int s, int e, int closech, List<Token> tlsdst, Token tkERRRef) {
		int i, probablyCloseBefore = e - 1;
		Token tk = tls.get(s);
		assert(tk.kw == KW.Err);
//		if(TK.AST_PARENTHESIS <= tk.tt && tk.tt <= TK.AST_BRACE) {  // already transformed
//			kArray_add(tlsdst, tk);
//			return s;
//		}
		Token tkP = new Token(); // tkP : .topch .lpos .sub  .text
		tlsdst.add(tkP);
		tkP.tt = tt; tkP.kw = KW.TK_KW[tt]; tkP.uline = tk.uline; tkP.topch = tk.topch; tkP.lpos = closech;
		tkP.sub = new ArrayList<Token>();
		for(i = s + 1; i < e; i++) {
			tk = tls.get(i);
			if(tk.kw != KW.Err) {
				tkP.sub.add(tk);
				continue;
			}
			if(tk.tt == TK.ERR) break;  // ERR
			assert(tk.topch != '{');
			if(tk.topch == '(') {
				i = makeTree(ctx, ks, TK.AST_PARENTHESIS, tls, i, e, ')', tkP.sub, tkERRRef);
				continue;
			}
			if(tk.topch == '[') {
				i = makeTree(ctx, ks, TK.AST_BRANCET, tls, i, e, ']', tkP.sub, tkERRRef);
				continue;
			}
			if(tk.topch == closech) {
				return i;
			}
			if((closech == ')' || closech == ']') && tk.tt == TK.CODE) probablyCloseBefore = i;
			if(tk.tt == TK.INDENT && closech != '}') continue;  // remove INDENT;
			i = appendKeyword(ctx, ks, tls, i, e, tkP.sub, tkERRRef);
		}
		if(tk.tt != TK.ERR) {
			//TODO
			//int errref = SUGAR_P(ERR_, tk.uline, tk.lpos, "'%c' is expected (probably before %s)", closech, kToken_s(tls.get(probablyCloseBefore)));
			//Token_toERR(ctx, tkP, errref);
		}
		else {
			tkP.tt = TK.ERR;
			tkP.text = tk.text;
		}
		//tkERRRef.set(0,tkP);//TODO
		return e;
	}
	
	public static int selectStmtLine(CTX ctx, KonohaSpace ks, int[] indent, List<Token> tls, int s, int e, int delim, List<Token> tlsdst, Token tkERRRef) {
		int i = s;
		assert(e <= tls.size());
		for(; i < e - 1; i++) {
			Token tk = tls.get(i);
			Token tk1 = tls.get(i+1);
			if(!tk.kw.equals(KW.Err)) break;  // already parsed
			if(tk.topch == '@' && (tk1.tt == TK.SYMBOL || tk1.tt == TK.USYMBOL)) {
				tk1.tt = TK.METANAME;
				tk1.kw = KW.Err;
				tlsdst.add(tk1);
				i++;
				Token tktest = tls.get(i+1);//I'm not sure.
				if(i + 1 < e && /*tls.get(i+1)*/tktest.topch == '(') {
					i = makeTree(ctx, ks, TK.AST_PARENTHESIS, tls, i+1, e, ')', tlsdst, tkERRRef);
				}
				continue;
			}
			if(tk.tt == TK.METANAME) {  // already parsed
				tlsdst.add(tk);
				if(tk1.tt == TK.AST_PARENTHESIS) {
					tlsdst.add(tk1);
					i++;
				}
				continue;
			}
			if(tk.tt != TK.INDENT) break;
			if(indent[0] == 0) indent[0] = tk.lpos;
		}
		for(; i < e ; i++) {
			Token tk = tls.get(i);
			if(tk.topch == delim && tk.tt == TK.OPERATOR) {
				return i+1;
			}
			if(!tk.kw.equals(KW.Err)) {
				tlsdst.add(tk);
				continue;
			}
			else if(tk.topch == '(') {
				i = makeTree(ctx, ks, TK.AST_PARENTHESIS, tls,  i, e, ')', tlsdst, tkERRRef);
				continue;
			}
			else if(tk.topch == '[') {
				i = makeTree(ctx, ks, TK.AST_BRANCET, tls, i, e, ']', tlsdst, tkERRRef);
				continue;
			}
			else if(tk.tt == TK.ERR) {
				//tkERRRef.add(0, tk); //TODO
			}
			if(tk.tt == TK.INDENT) {
				if(tk.lpos <= indent[0]) {
					return i+1;
				}
				continue;
			}
			i = appendKeyword(ctx, ks, tls, i, e, tlsdst, tkERRRef);//What's wrong?
		}
		return i;
	}
	
	private static int appendKeyword(CTX ctx, KonohaSpace ks, List<Token> tls, int s, int e, List<Token> dst, Token tkERR) {
		int next = s; // don't add
		Token tk = tls.get(s);
		if(tk.tt < TK.OPERATOR) {
			tk.kw = KW.TK_KW[tk.tt];
		}
		if(tk.tt == TK.SYMBOL) {
			tk.resolved(ctx, ks);
		}
		else if(tk.tt == TK.USYMBOL) {
			if(! tk.resolved(ctx, ks)) {
				KClass ct = ks.getCT(ctx, null, tk.text, tk.text.length(), TY.unknown); // TODO Konohaspace_getCT?
				if(ct != null) {
					tk.kw = KW.Type;
					tk.ty = ct.cid;
				}
			}
		}
		else if(tk.tt == TK.OPERATOR) {
			if(! tk.resolved(ctx, ks)) {
				//FIXME
//				int errref = SUGAR_P(ERR_, tk.uline, tk.lpos, "undefined token: %s", kToken_s(tk)); //TODO SUGAR_P? kToken_s?
//				Token_toERR(ctx, tk, errref);  // TODO Token_toERR?
//				tkERR.set(0,tk);
				return e;
			}
		}
		else if (tk.tt == TK.CODE) {
			tk.kw = KW.Brace;
		}
		if (tk.kw == KW.Type) {
			dst.add(tk);
			while (next + 1 < e) {
				Token tkN = tls.get(next+1);
				if (tkN.topch != '[' ) break;
				List<Token> abuf = /*CtxSugar.tokens;*/ctx.ctxsugar.tokens;//TODO CtxSugar.tokens should be static?
				int atop = abuf.size();
				next = makeTree(ctx, ks, TK.AST_BRANCET, tls, next+1, e, ']', abuf, tkERR);
				if(!(abuf.size() > atop)) return next;
				Token tkB = abuf.get(atop);
				tk = TokenType_resolveGenerics(ctx, ks, tk, tkB);
				if(tk == null) {
					ctx.DBG_P("APPEND tkB.tt=%s", Token.ttToStr(tkB.tt));
					if(abuf != dst) {
						dst.add(tkB);
						abuf.remove(atop);
					}
					ctx.DBG_P("next=%d", next);
					return next;
				}
			}
		}
		else if(tk.kw != KW.Err && tk.kw != KW.Expr) {
			dst.add(tk);
		}
		return next;
	}
	
	private static Token TokenType_resolveGenerics(CTX ctx, KonohaSpace ks, Token tk, Token tkP) {
		if(tkP.tt == TK.AST_BRANCET) {
			int i, psize= 0, size = tkP.sub.size();
			Param[] p = new Param[size];
			for(i = 0; i < size; i++) {
				Token tkT = tkP.sub.get(i);
				if(tkT.kw == KW.Type) {
					p[psize].ty = tkT.ty;
					psize++;
					continue;
				}
				if(tkT.topch == ',') continue;
				return null; // new int[10];  // not generics
			}
			KClass ct = null;
			if(psize > 0) {
				//ct = ctx.ct(tk.ty); // TODO CT_? (ctx.share.ca.cts[t])
				if(ct.bcid == CLASS.Func) {
					//TODO
//					ct = kClassTable_Generics(ct, p[0].ty, psize-1, p+1); // TODO kClassTable_Generics? src/konoha/datatype.h/CT_Generics
				}
				else if(ct.p0 == TY.VOID) {
//					SUGAR_P(ERR_, tk.uline, tk.lpos, "not generic type: %s", T_ty(tk.ty)); //TODO T_ty?
					return tk;
				}
				else {
					//ct = ct.generics(ctx, TY.VOID, psize, p);
				}
			}
			else {
				//ct = ct.CT_p0(ctx, tk.ty);
			}
			tk.ty = ct.cid;
			return tk;
		}
		return null;
	}
	
}
