package jkonoha;

import java.util.*;

public class Parser {

	// important
	public Block newBlock(CTX ctx, KonohaSpace ks, Stmt parent, List<Token> tls, int s, int e, int delim) {
		Block bk = new Block();
		int i = s, indent = 0, atop = tls.size();
		while (i < e) {
			Token tkERR = null;
			assert (atop == tls.size());
			i = selectStmtLine (ctx, ks, indent, tls, i, e, delim, tls, tkERR);//TODO How to tkERR?
			int asize = tls.size();
			if (asize > atop) {
				Block_addStmtLine (ctx, bk, tls, atop, asize, tkERR);
				tls.remove(atop);
			}
		}
		return bk;
	}
	
	public void Block_addStmtLine (CTX ctx, Block bk, List<Token> tls, int s, int e, Token tkERR) {
		Stmt stmt = new Stmt(tls.get(s).uline);
		bk.blocks.add(stmt);
		stmt.parentNULL = bk;
		if (tkERR != null) {
			stmt.syntax = new Syntax();//TODO rf. /src/ast.h (SYN_ function in Block_addStmtLine). 
			stmt.build = TSTMT.ERR;
			stmt.setObject(KW.Err, tkERR);
		}
		else {
			int estart = ctx.sugar.errors.size();
			s = stmt.addAnnotation(ctx, stmt, tls, s, e);
			if (!stmt.parseSyntaxRule(ctx, stmt, tls, s, e)) {
				stmt.toERR(stmt, estart);
			}
		}
		assert (stmt.syntax != null);
	}

	public int makeTree(CTX ctx, KonohaSpace ks, int tt, List<Token> tls, int s, int e, int closech, List<Token> tlsdst, Token tkERRRef) {
		int i, probablyCloseBefore = e - 1;
		RawToken tk = (RawToken)tls.get(s); // tk : .topch .sub .lpos .text
		assert(tk.kw == 0);
//		if(TK.AST_PARENTHESIS <= tk.tt && tk.tt <= TK.AST_BRACE) {  // already transformed
//			kArray_add(tlsdst, tk);
//			return s;
//		}
		AstToken tkP = new AstToken(); // tkP : .topch .lpos .sub  .text
		tlsdst.add(tkP);
		//tkP.tt = tt; tkP.kw = tt; tkP.uline = tk.uline; tkP.topch = tk.topch; tkP.lpos = closech;
		//KSETv(tkP.sub, new_(TokenArray, 0));
		tkP.sub = new ArrayList<Token>();
		for(i = s + 1; i < e; i++) {
			tk = (RawToken)tls.get(i);
			if(tk.kw != 0) {
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
				i = makeTree(ctx, ks, TK.AST_BRACKET, tls, i, e, ']', tkP.sub, tkERRRef);
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
			//KSETv(tkP.text, tk.text);
			tkP.text = tk.text;
		}
		//tkERRRef.set(0,tkP);//TODO
		return e;
	}
	
	public int selectStmtLine(CTX ctx, KonohaSpace ks, int indent, List<Token> tls, int s, int e, int delim, List<Token> tlsdst, Token tkERRRef) {
		int i = s;
		assert(e <= tls.size());
		for(; i < e - 1; i++) {
			RawToken tk = (RawToken)tls.get(i); // tk : .topch .lpos .
			RawToken tk1 = (RawToken)tls.get(i+1); //tk1 : 
			if(tk.kw > 0) break;  // already parsed
			if(tk.topch == '@' && (tk1.tt == TK.SYMBOL || tk1.tt == TK.USYMBOL)) {
				tk1.tt = TK.METANAME;  tk1.kw = 0;
				tlsdst.add(tk1); i++;
				if(i + 1 < e && ((RawToken)tls.get(i+1)).topch == '(') {
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
			if(indent == 0) indent = tk.lpos;
		}
		for(; i < e ; i++) {
			RawToken tk = (RawToken)tls.get(i);
			if(tk.topch == delim && tk.tt == TK.OPERATOR) {
				return i+1;
			}
			if(tk.kw > 0) {
				tlsdst.add(tk);
				continue;
			}
			else if(tk.topch == '(') {
				i = makeTree(ctx, ks, TK.AST_PARENTHESIS, tls,  i, e, ')', tlsdst, tkERRRef);
				continue;
			}
			else if(tk.topch == '[') {
				i = makeTree(ctx, ks, TK.AST_BRACKET, tls, i, e, ']', tlsdst, tkERRRef);
				continue;
			}
			else if(tk.tt == TK.ERR) {
				//tkERRRef.add(0, tk); //TODO
			}
			if(tk.tt == TK.INDENT) {
				if(tk.lpos <= indent) {
					return i+1;
				}
				continue;
			}
			i = appendKeyword(ctx, ks, tls, i, e, tlsdst, tkERRRef);
		}
		return i;
	}
	
	private int appendKeyword(CTX ctx, KonohaSpace ks, List<Token> tls, int s, int e, List<Token> dst, Token tkERR) {
		int next = s; // don't add
		RawToken tk = (RawToken)tls.get(s); // tk : .text .ty .lpos
		if(tk.tt < TK.OPERATOR) {
			tk.kw = tk.tt;
		}
		if(tk.tt == TK.SYMBOL) {
			Token_resolved(ctx, ks, tk);
		}
		else if(tk.tt == TK.USYMBOL) {
			if(!Token_resolved(ctx, ks, tk)) {
				KClass ct = ks.getCT(ctx, null, tk.text, TY.unknown); // TODO Konohaspace_getCT?
				if(ct != null) {
					tk.kw = KW.Type;
					tk.ty = ct.cid;
				}
			}
		}
		else if(tk.tt == TK.OPERATOR) {
			if(!Token_resolved(ctx, ks, tk)) {
				int errref = SUGAR_P(ERR_, tk.uline, tk.lpos, "undefined token: %s", kToken_s(tk)); //TODO SUGAR_P? kToken_s?
				Token_toERR(ctx, tk, errref);  // TODO Token_toERR?
				tkERR.set(0,tk);
				return e;
			}
		}
		else if(tk.tt == TK.CODE) {
			tk.kw = KW.Brace;
		}
		if(tk.kw == KW.Type) {   // trying to resolve Type[Type, Type]
			dst.add(tk);
			while(next + 1 < e) {
				RawToken tkB = (RawToken)tls.get(next + 1);
				if(tkB.topch != '[') break;
				List<Token> abuf = ctx.sugar.tokens;
				int atop = abuf.size();
				next = makeTree(ctx, ks, TK.AST_BRACKET, tls, next+1, e, ']', abuf, tkERR);
				if(!(abuf.size() > atop)) return next;
				tkB = (RawToken)abuf.get(atop);
				tk = TokenType_resolveGenerics(ctx, ks, tk, tkB);
				if(tk == null) {
					DBG_P("APPEND tkB.tt=%s", T_tt(tkB.tt)); // TODO T_tt?
					if(abuf != dst) {
						dst.add(tkB);
						abuf.remove(atop);
					}
					DBG_P("next=%d", next); // TODO DBG_P?
					return next;
				}
				abuf.remove(atop);
			}
		}
		else if(tk.kw > KW.Expr) {
			dst.add(tk);
		}
		return next;
	}
	
	private boolean Token_resolved(CTX ctx, KonohaSpace ks, Token tk) {
		//int kw = keyword(ctx, S_text(tk.text), S_size(tk.text), FN_NONAME); // tk : .text .ty
		String kw = ((RawTokentk).text;
		//if(kw != FN_NONAME) {
			Syntax syn = SYN_(ks, kw); // TODO SYN_?
			if(syn != null) {
				if(syn.ty != TY.unknown) {
					tk.kw = KW.Type; tk.ty = syn.ty;
				}
				else {
					tk.kw = kw;
				}
				return true;
			}
		//}
		return false;
	}
	
	private Token TokenType_resolveGenerics(CTX ctx, KonohaSpace ks, Token tk, Token tkP) {
		if(tkP.tt == TK.AST_BRACKET) { // tk : .ty .lpos, tkP : .sub, tkT : .ty .topch 
			int i, psize= 0, size = tkP.sub.size();
			kparam_t p[size]; // TODO kparam_t?
			for(i = 0; i < size; i++) {
				Token tkT = ((AstToken)tkP).sub.get(i);
				if(tkT.kw == KW.Type) {
					p[psize].ty = tkT.ty;
					psize++;
					continue;
				}
				if(((RawToken)tkT).topch == ',') continue;
				return null; // new int[10];  // not generics
			}
			KClass ct = null;
			if(psize > 0) {
				ct = CT_(tk.ty); // TODO CT_? (ctx.share.ca.cts[t])
				if(ct.bcid == CLASS_Func) {
					ct = kClassTable_Generics(ct, p[0].ty, psize-1, p+1); // TODO kClassTable_Generics?
				}
				else if(ct.p0 == TY.VOID) {
					SUGAR_P(ERR_, tk.uline, tk.lpos, "not generic type: %s", T_ty(tk.ty)); //TODO T_ty?
					return tk;
				}
				else {
					ct = kClassTable_Generics(ct, TY.VOID, psize, p);
				}
			}
			else {
				ct = CT_p0(ctx, CT_Array, tk.ty); // TODO CT_p0?
			}
			tk.ty = ct.cid;
			return tk;
		}
		return null;
	}
	
	//TODO
	void DBG_P(String fmt, Object...args) {
		System.err.printf(fmt, args);
	}
	
}
