package jkonoha;

import java.util.ArrayList;
import java.util.List;

import jkonoha.compiler.kobject.KKonohaSpace;

public class Block {
	public KonohaSpace ks;
	public Stmt parentNULL;
	public final List<Stmt> blocks = new ArrayList<Stmt>();
	public Expr esp;  // BlockScopeVariable() to record maximun used stack
}
