package jkonoha;

import java.util.*;

public class Block extends KObject {
	public KonohaSpace ks;
	public Stmt parentNULL;
	public final List<Stmt> blocks = new ArrayList<Stmt>();
	public Expr esp;  // BlockScopeVariable() to record maximun used stack
	
	// src/sugar/struct.h:952
	public Block(CTX ctx, KonohaSpace ks) {
		this.ks = ks != null ? ks : ctx.modsugar.rootks;
	}
}
