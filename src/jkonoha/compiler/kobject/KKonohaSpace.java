package jkonoha.compiler.kobject;

import java.util.*;

import jkonoha.CTX;
import jkonoha.Block;
import jkonoha.KObject;
import jkonoha.SYNFLAG;
import jkonoha.compiler.CompilerContext;

public class KKonohaSpace extends KObject {
	
	private final CTX ctx;
	private final CompilerContext cctx;
	private final Map<Integer, KDefineSyntax> syntaxMap = new HashMap<Integer, KDefineSyntax>();
	
	public KKonohaSpace(CTX ctx) {
		this.ctx = ctx;
		this.cctx = new CompilerContext(ctx);
	}
	
	public void defineDefaultSyntax() {
		// #define _TERM .flag = SYNFLAG_ExprTerm
		// #define _OP   .flag = SYNFLAG_OP
		defineSyntax(new KDefineSyntax("$ERR") {
			{ this.flags = SYNFLAG.StmtBreakExec; }
		});
		defineSyntax(new KDefineSyntax("$expr") {
			{ this.rule = "$expr"; }
			@Override public void parseStmt() {
				
			}
			@Override public void topStmtTyCheck() {
				
			}
			@Override public void stmtTyCheck() {
				
			}
		});
		defineSyntax(new KDefineSyntax("$SYMBOL") {
			{ this.flags = SYNFLAG.ExprTerm; }
			@Override public void parseStmt() {
				
			}
			@Override public void exprTyCheck() {
				
			}
		});
		//TODO
	}
	
	// src/sugar/struct.h:154
	public void defineSyntax(KDefineSyntax s) {
		syntaxMap.put(s.kw, s);
	}
	
	// src/sugar/sugar.c:100
	public int eval(String script, int uline) {
		//TODO
		List<KToken> tls = new ArrayList<KToken>();
		tokenize(script, uline, tls);
		Block bk = null;//newBlock();
		cctx.evalBlock(bk);
		return 0;
	}
	
	public void tokenize(String source, int uline, List<KToken> a) {
		//TODO
	}
	
	public boolean importPackage(String name, int pline) {
		//TODO
		return false;
	}
	
	public boolean loadScript(String path) {
		//TODO
		return false;
	}
	
}
