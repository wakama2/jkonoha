package jkonoha;

import java.io.*;
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
		KObject o = ks.eval(ctx, source, 0);
		if(o != null) {
			System.out.println(o);
		}
	}
	
	private void loadScript(CTX ctx, String path) {
		BufferedReader br = null;
		try {
			File inputFile = new File(path);
			br = new BufferedReader(new FileReader(inputFile));
			String msg;
			String script = "";
			boolean check = false;
			while ((msg = br.readLine()) != null) {
				script = script + msg;
				check = checkStmt(script);
				if (check){
					this.eval(ctx, script);
					script = "";
				}
			}
			if (!check) System.out.println("(Cancelled)...");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean checkStmt(String path) {
		int i = 0, ch, nest = 0, quote = 0;
		while (true){
			int flag = 0;
			for (; i < path.length(); i++){
				ch = path.charAt(i);
				if(ch == '{' || ch == '[' || ch == '(') nest++;
				if(ch == '}' || ch == ']' || ch == ')') nest--;
				if(ch == '\'' || ch == '"' || ch == '`') {
					if(path.charAt(i+1) == ch && path.charAt(i+2) == ch) {
						quote = ch; i+=2;
						flag = 1;
						break;
					}
				}
			}
			if (flag == 0) return nest == 0;
			assert(i > 0);
			flag = 0;
			for(; i < path.length(); i++) {
				ch = path.charAt(i);
				if(path.charAt(i-1) != '\\' && ch == quote) {
					if(path.charAt(i+1) == ch && path.charAt(i+2) == ch) {
						i+=2;
						flag = 1;
						break;
					}
				}
			}
			if (flag == 0) return false;
		}
	}
	
//	public Object eval(String script, long uline) {
//		
//	}
		
	public static void main(String[] args) {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		Scanner s = new Scanner(System.in);
		if (args.length == 1) {
			String path = args[0];
			k.loadScript(ctx, path);
		}
		else if (args.length == 0){
			while(true) {
				System.out.print(">>>");
				String script = "";
				String l;
				int flag;
				while ((l = s.nextLine()) != null) {
					flag = 0;
					script = script + l;
					if (k.checkStmt(script)){
						k.eval(ctx, script);
						script = "";
						flag = 1;
					}
					if (flag == 0) System.out.print("   ");
					else System.out.print(">>>");
				}
			}
		}
		else {}// TODO option
	}
	
}
