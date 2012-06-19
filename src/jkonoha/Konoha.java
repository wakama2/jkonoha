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
		Scanner s = new Scanner(System.in);
		while(true) {
			System.out.print(">>>");
			String l = s.nextLine();
			if(l == null) break;
			k.eval(ctx, l);
		}
	}
	
}
