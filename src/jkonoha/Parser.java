package jkonoha;


import jkonoha.compiler.KClass;

import java.util.*;

public class Parser {

	// important
	public Block newBlock() {
		//TODO
		return null;
	}
	
	public void makeTree(CTX ctx, KonohaSpace ks, int tt, List<Token> tls, int s, int e, int closech, List<Token> tlsdst, Object tkERRRef) {
		//TODO 
				
	}
	
	public int selectStmtLine(CTX ctx, KonohaSpace ks, int indent, List<Token> tls, int s, int e, int delim, List<Token> tlsdst, List<Token> tkERRRef) {
		int i = s;
		assert(e <= tls.size());
		for(; i < e - 1; i++) {
			Token tk = tls.get(i); // TODO Token?
			Token tk1 = tls.get(i+1);
			if(tk.kw > 0) break;  // already parsed
			if(tk.topch == '@' && (tk1.tt == TK_SYMBOL || tk1.tt == TK_USYMBOL)) {
				tk1.tt = TK_METANAME;  tk1.kw = 0;
				tlsdst.add(tk1); i++;
				if(i + 1 < e && tls.get(i+1).topch == '(') {
					i = makeTree(ctx, ks, AST_PARENTHESIS, tls, i+1, e, ')', tlsdst, tkERRRef);
				}
				continue;
			}
			if(tk.tt == TK_METANAME) {  // already parsed
				tlsdst.add(tk);
				if(tk1.tt == AST_PARENTHESIS) {
					tlsdst.add(tk1);
					i++;
				}
				continue;
			}
			if(tk.tt != TK_INDENT) break;
			if(indent == 0) indent = tk.lpos;
		}
		for(; i < e ; i++) {
			Token tk = tls.get(i);
			if(tk.topch == delim && tk.tt == TK_OPERATOR) {
				return i+1;
			}
			if(tk.kw > 0) {
				tlsdst.add(tk);
				continue;
			}
			else if(tk.topch == '(') {
				i = makeTree(ctx, ks, AST_PARENTHESIS, tls,  i, e, ')', tlsdst, tkERRRef);
				continue;
			}
			else if(tk.topch == '[') {
				i = makeTree(ctx, ks, AST_BRACKET, tls, i, e, ']', tlsdst, tkERRRef);
				continue;
			}
			else if(tk.tt == TK_ERR) {
				tkERRRef.add(0, tk);
			}
			if(tk.tt == TK_INDENT) {
				if(tk.lpos <= indent) {
					return i+1;
				}
				continue;
			}
			i = appendKeyword(ctx, ks, tls, i, e, tlsdst, tkERRRef);
		}
		return i;
	}
	
	private int appendKeyword(CTX ctx, KonohaSpace ks, List<Token> tls, int s, int e, List<Token> dst, List<Token> tkERR) {
		int next = s; // don't add
		Token tk = (TypeToken) tls.get(s); // tk = ? .text .ty
		if(tk.tt < TK_OPERATOR) {
			tk.kw = tk.tt;
		}
		if(tk.tt == TK_SYMBOL) {
			Token_resolved(ctx, ks, tk);
		}
		else if(tk.tt == TK_USYMBOL) {
			if(!Token_resolved(ctx, ks, tk)) {
				KClass ct = KonohaSpace_getCT(ks, null, S_text(tk.text), S_size(tk.text), TY_unknown); // TODO Konohaspace_getCT?
				if(ct != null) {
					tk.kw = KW_Type;
					tk.ty = ct.cid;
				}
			}
		}
		else if(tk.tt == TK_OPERATOR) {
			if(!Token_resolved(ctx, ks, tk)) {
				int errref = SUGAR_P(ERR_, tk.uline, tk.lpos, "undefined token: %s", kToken_s(tk)); //TODO SUGAR_P? kToken_s?
				Token_toERR(ctx, tk, errref);  // TODO Token_toERR?
				tkERR.set(0,tk);
				return e;
			}
		}
		else if(tk.tt == TK_CODE) {
			tk.kw = KW_Brace;
		}
		if(tk.kw == KW_Type) {   // trying to resolve Type[Type, Type]
			dst.add(tk);
			while(next + 1 < e) {
				AstToken tkB = (AstToken) tls.get(next + 1);
				if(tkB.topch != '[') break;
				kArray abuf = ctxsugar.tokens; // TODO ctxsugar?
				int atop = abuf.size();
				next = makeTree(ctx, ks, AST_BRACKET, tls,  next+1, e, ']', abuf, tkERR);
				if(!(abuf.size() > atop)) return next;
				tkB = abuf.get(atop);
				tk = TokenType_resolveGenerics(ctx, ks, tk, tkB);
				if(tk == null) {
					DBG_P("APPEND tkB.tt=%s", T_tt(tkB.tt)); // TODO T_tt?
					if(abuf != dst) {
						dst.add(tkB);
						abuf.clear(atop);
					}
					DBG_P("next=%d", next); // TODO DBG_P?
					return next;
				}
				abuf.clear(atop);
			}
		}
		else if(tk.kw > KW_Expr) {
			dst.add(tk);
		}
		return next;
	}
	
	private boolean Token_resolved(CTX ctx, KonohaSpace ks, Token tk)
	{
		int kw = keyword(ctx, S_text(tk.text), S_size(tk.text), FN_NONAME); // tk = ? .text .ty
		if(kw != FN_NONAME) {
			Syntax syn = SYN_(ks, kw); // TODO SYN_?
			if(syn != null) {
				if(syn.ty != TY_unknown) {
					tk.kw = KW_Type; tk.ty = syn.ty;
				}
				else {
					tk.kw = kw;
				}
				return true;
			}
		}
		return false;
	}
	
	private Token TokenType_resolveGenerics(CTX ctx, KonohaSpace ks, TypeToken tk, AstToken tkP)
	{
		if(tkP.tt == AST_BRACKET) {
			int i, psize= 0, size = tkP.sub.size();
			kparam_t p[size]; // TODO kparam_t?
			for(i = 0; i < size; i++) {
				TypeToken tkT = (TypeToken) tkP.sub.get(i);
				if(tkT.kw == KW_Type) {
					p[psize].ty = tkT.ty;
					psize++;
					continue;
				}
				if(tkT.topch == ',') continue;
				return null; // new int[10];  // not generics
			}
			KClass ct = null;
			if(psize > 0) {
				ct = CT_(tk.ty); // TODO CT_? (_ctx->share->ca.cts[t])
				if(ct.bcid == CLASS_Func) {
					ct = kClassTable_Generics(ct, p[0].ty, psize-1, p+1); // TODO kClassTable_Generics?
				}
				else if(ct.p0 == TY_void) {
					SUGAR_P(ERR_, tk.uline, tk.lpos, "not generic type: %s", T_ty(tk.ty)); //TODO T_ty?
					return tk;
				}
				else {
					ct = kClassTable_Generics(ct, TY_void, psize, p);
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
}
