package commons.sugar;

import sugar.K_Token;
import sugar.token.Tokenizer;
import sugar.token.codeParser.FTokenizer;
import commons.konoha2.CTX;
import commons.konoha2.kclass.K_Array;
import commons.konoha2.kclass.K_Object;

public class K_KonohaSpace extends K_Object {
	int packid;  int packdom;
	K_KonohaSpace parentNULL;
	FTokenizer fmat;
	KMap syntaxMapNN;
	
//	void gluehdr;
	K_Object scrNUL;
	int static_cid; int function_cid;
	K_Array<K_Object> methods;  // default K_EMPTYARRAY
	KArray cl;
	
	public void tokenize(CTX ctx, String source, int uline, K_Array<K_Token> a) {
		int i, pos = a.size();
		FTokenizer fmat[];
		fmat = Tokenizer.MiniKonohaTokenMatrix();
		// TODD ks = null ? fmat = MiniKonohaTokenMatrix() : KKonohaSpace.tokenizerMatrix();
		TEnv tenv = new TEnv(source, uline, a, 4, fmat);
		Tokenizer.tokenize(ctx, tenv);
		if(tenv.uline == 0) {
			for(i = pos; i < a.size(); i++) {
				a.get(i).uline = 0;
			}
		}
	}	
}
