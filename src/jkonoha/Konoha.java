package jkonoha;

import java.util.*;

public class Konoha {
	public static final int NEWID = -2;
	
	public ArrayList<String> fileidList;
	public Map<String, Long> fileidMap;
	
	KonohaSpace ks = new KonohaSpace();
	
	public Konoha(CTX ctx) {
		this.fileidList = new ArrayList<String>();
		this.fileidMap  = new HashMap<String, Long>(83);
		ks.defineDefaultSyntax(ctx);
	}
	
	long kfileid(String name, long def) {
		Long id = this.fileidMap.get(name);
		if(id == null) {
			if(def != NEWID) {
				return def;
			}
			long newid = this.fileidList.size();
			this.fileidList.add(name);
			this.fileidMap.put(name, newid);
			id = newid;
		}
		long uline = id;
		return uline << 32;
	}

	String S_file(long uline) {
		uline >>= 32;
		return this.fileidList.get((int)uline);
	}
	
	public void eval(CTX ctx, String source) { // FIXME This method is dumping divided token now.
		Object o = ks.eval(ctx, source, 0);
		if(o != null) {
			System.out.println(o);
		}
	}
	
	public void load(String script) {
		// TODO
	}
	
//	public Object eval(String script, long uline) {
//		
//	}
		
	public static void main(String[] args) {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		k.eval(ctx, "int func(int x) { System.p(100); }");
		k.eval(ctx, "func(100);");
//		k.eval(ctx, "(1+2) * (3-4)");
//		k.eval(ctx, "if (1<10) System.p(10); else System.p(20);");
//		k.eval(ctx, "System.p(12);");
//		k.eval(ctx, "int fibo(int n) { if(n<3) return 1; else return fib(n-1) + fib(n-2); }");
//		k.eval(ctx, "fibo(10);");
//		k.load("file.k");
	}
	
}
