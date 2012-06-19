package jkonoha;

import java.util.List;

import jkonoha.compiler.CompilerContext;

public class Gamma {
	public KClass this_cid = KClass.systemClass;
	public KonohaSpace ks;
	public CompilerContext cc;
	public KMethod method;
	public List<String> argNames;
}
