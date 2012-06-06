package jkonoha;

import java.util.*;

public class Konoha {
	public static final int NEWID = -2;
	
	public ArrayList<String> fileidList;
	public Map<String, Long> fileidMap;
	
	public Konoha() {
		this.fileidList = new ArrayList<String>();
		this.fileidMap  = new HashMap<String, Long>(83);
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
		KonohaSpace ks = new KonohaSpace();
		ks.eval(ctx, source, 0);
	}
	
	public void load(String script) {
		// TODO
	}
	
//	public Object eval(String script, long uline) {
//		
//	}
	
//	public static int keyword(CTX ctx, String name, int def) {
//		KModSugar kmodsugar = (KModSugar)ctx.kmodsugar();
//		return kmodsugar.keywordMapNN.getcode(ctx, kmodsugar.keywordList, name, def);
//	}
	
	public static void main(String[] args) {
		Konoha k = new Konoha();
		CTX ctx = new CTX();
		k.eval(ctx, "123  + 456");
//		k.eval(ctx, "int fibo(int n) { if(n<3) return 1; else return fib(n-1) + fib(n-2); }");
//		k.eval(ctx, "fibo(10);");
//		k.load("file.k");
	}
	
}