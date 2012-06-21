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
	
	public KObject eval(CTX ctx, String source) { // FIXME This method is dumping divided token now.
		return ks.eval(ctx, source, 0);
	}
	
	private void loadScript(CTX ctx, String path) {
		BufferedReader br = null;
		try {
			File inputFile = new File(path);
			br = new BufferedReader(new FileReader(inputFile));
			String msg;
			String script = "";
			while ((msg = br.readLine()) != null) {
				script = script + msg;
				if (checkStmt(script) == 0){
					this.eval(ctx, script);
					script = "";
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private int checkStmt(String src) {
		int i = 0, ch, nest = 0, quote = 0;
		while (true){
			int flag = 0;
			for (; i < src.length(); i++){
				ch = src.charAt(i);
				if(ch == '{' || ch == '[' || ch == '(') nest++;
				if(ch == '}' || ch == ']' || ch == ')') nest--;
				if(ch == '\'' || ch == '"' || ch == '`') {
					if(i+2 < src.length() && src.charAt(i+1) == ch && src.charAt(i+2) == ch) {
						quote = ch; i+=2;
						flag = 1;
						break;
					}
				}
			}
			if (flag == 0) return nest;
			assert(i > 0);
			flag = 0;
			for(; i < src.length(); i++) {
				ch = src.charAt(i);
				if(src.charAt(i-1) != '\\' && ch == quote) {
					if(i+2 < src.length() && src.charAt(i+1) == ch && src.charAt(i+2) == ch) {
						i+=2;
						flag = 1;
						break;
					}
				}
			}
			if (flag == 0) return 1;
		}
	}
	
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
				while ((l = s.nextLine()) != null) {
					script = script + l;
					int check = k.checkStmt(script);
					if (check == 0) {
						KObject o = k.eval(ctx, script);
						if(o != null) {
							System.out.println(o);
						}
						script = "";
						System.out.print(">>>");
						continue;
					}
					else if (check < 0) {
						script = "";
						System.out.println("(Cancelled)...");
						System.out.print(">>>");
						continue;
					}
					else{
						System.out.print("   ");
					}
				}
			}
		}
		else {}// TODO option
	}
}
