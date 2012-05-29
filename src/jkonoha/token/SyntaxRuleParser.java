package sugar.token;

import sugar.K_Token;
import sugar.Sugar;
import commons.konoha2.CTX;
import commons.konoha2.kclass.K_Array;
import commons.sugar.CtxSugar;
import commons.sugar.K_KonohaSpace;

public final class SyntaxRuleParser {
	
	private static int findTopCh(CTX ctx, K_Array<K_Token> tls, int s, int e, int tt, int closech) {
		int i;
		for(i = s; i < e; i++) {
			K_Token tk = tls.get(i);
			if(tk.tt == tt && tk.text.text.charAt(0) == closech) return i; // TODO S_text ?
		}
		CTX.DBG_ASSERT(i != e);
		return 0;
	}
	
	private static boolean checkNestedSyntax(CTX ctx, K_Array<K_Token> tls, int[] sPtr, int e, int tt, int opench, int closech) {
		int i = sPtr[0];
		K_Token tk = tls.get(i);
		String t = tk.text.text; // TODO const char *t =  S_text(tk->text);
		if(t.charAt(0) == opench && t.charAt(1) == 0) {
			int ne = findTopCh(ctx, tls, i + 1, e, tk.tt, closech);
			tk.tt = tt; tk.kw = tt;
			tk.sub = new K_Array<K_Token>();
			// TODO KSETv(tk->sub, new(TokenArray, 0));
			tk.topch = opench; tk.closech = closech;
			makeSyntaxRule(ctx, tls, i + 1, ne, tk.sub);
			sPtr[0] = ne; /* TODO *s */
			return true;
		}
		return false;
	}
	
	private static boolean makeSyntaxRule(CTX ctx, K_Array<K_Token> tls, int s, int e, K_Array<K_Token> adst) {
		int i;
		int[] iPtr = new int[1];
		String nbuf;
		int nameid = 0;
		// TODO dumpTokenArray(_ctx, 0, tls, s, e);
		for(i = s; i < e; i++) {
			K_Token tk = tls.get(i);
			if(tk.tt == K_Token.TK_INDENT) continue;
			if(tk.tt == K_Token.TK_TEXT /*|| tk.tt == KToken.TK_STEXT*/) {
				iPtr[0] = i;
				if(checkNestedSyntax(ctx, tls, iPtr, e, K_Token.AST_PARENTHESIS, '(', ')') ||
						checkNestedSyntax(ctx, tls, iPtr, e, K_Token.AST_PARENTHESIS, '[', ']') ||
						checkNestedSyntax(ctx, tls, iPtr, e, K_Token.AST_PARENTHESIS, '{', '}')) {
				}
				else {
					tk.tt = K_Token.TK_CODE;
					tk.kw = Sugar.keyword(ctx, tk.text.text, CTX.FN_NEWID);
					// tk.kw = keyword(_ctx, S_text(tk->text), S_size(tk->text), FN_NEWID);
				}
				adst.add(tk);
				continue;
			}
			if(tk.tt == K_Token.TK_SYMBOL || tk.tt == K_Token.TK_USYMBOL) {
				if(i > 0 && (tls.get(i - 1)).topch == '$') {
					nbuf = "$" + tk.text.text;
					tk.kw = Sugar.keyword(ctx, nbuf, CTX.FN_NEWID);
					// tk->kw = keyword(_ctx, (const char*)nbuf, strlen(nbuf), FN_NEWID);
					tk.tt = K_Token.TK_METNAME;
					if(nameid == 0) nameid = tk.kw;
					tk.nameid = nameid;
					nameid = 0;
					adst.add(tk); continue;
				}
				if(i + 1 < e && (tls.get(i + 1)).topch == ':') {
					tk = tls.get(i);
					nameid = Sugar.keyword(ctx, tk.text.text, CTX.FN_NEWID);
					// nameid = keyword(_ctx, S_text(tk->text), S_size(tk->text), FN_NEWID);
					i++;
					continue;
				}
			}
			if(tk.tt == K_Token.TK_OPERATOR) {
				if(checkNestedSyntax(ctx, tls, iPtr, e, K_Token.AST_OPTIONAL, '[', ']')) {
					adst.add(tk);
					continue;
				}
				if(((K_Token)tls.get(i)).topch == '$') continue;
			}
			// TODO SUGAR_P(ERR_, tk->uline, tk->lpos, "illegal sugar syntax: %s", kToken_s(tk));
			return false;
		}
		return true;
	}
	
	public static void parseSyntaxRule(CTX ctx, String rule, int uline, K_Array<K_Token> a) {
		CtxSugar ctxsugar = (CtxSugar)ctx.ctxsugar();
		K_Array<K_Token> tls = ctxsugar.tokens;
		int pos = tls.size();
		K_KonohaSpace ks = null;
		ks.tokenize(ctx, rule, uline, a);
//		Tokenizer.ktokenize(ctx, null, rule, uline, tls);
		makeSyntaxRule(ctx, tls, pos, tls.size(), a);
		tls.clear();
	}
}