package jkonoha;

import java.io.*;
import java.util.*;

public class Konoha {
	public static final int NEWID = -2;
	
	public ArrayList<String> fileidList;
	public Map<String, Long> fileidMap;
	
	public Konoha(CTX ctx) {
		this.fileidList = new ArrayList<String>();
		this.fileidMap  = new HashMap<String, Long>(83);
		ctx.ks.defineDefaultSyntax(ctx);
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
	
//	KClass addClassDef(CTX ctx, int packid, int packdom, String name, KDEFINE_CLASS cdef, long pline) {
//		 KClass ct = new_CT(_ctx, NULL, cdef, pline);
//		ct.packid  = packid;
//		ct.packdom = packdom;
//		if(name == null) {
//			String n = cdef.structname;
//			assert(n != null); // structname must be set;
//			ct.nameid = ksymbolSPOL(n, strlen(n), SPOL_ASCII|SPOL_POOL|SPOL_TEXT, _NEWID);
//		}
//		else {
//			ct.nameid = ksymbolA(S_text(name), S_size(name), _NEWID);
//		}
//		CT_setName(_ctx, ct, pline);
//		return ct;
//	}
	
	public KObject eval(CTX ctx, String source) { // FIXME This method is dumping divided token now.
		return ctx.ks.eval(ctx, source, 0);
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
	
	public void shell(CTX ctx) {
		Scanner s = new Scanner(System.in);
		String script = "";
		while(true) {
			if(script.length() == 0) {
				System.out.print(">>>");
			}
			if(!s.hasNextLine()) {
				break;
			}
			String l = s.nextLine();
			script = script + l;
			int check = checkStmt(script);
			if (check == 0) {
				KObject o = eval(ctx, script);
				if(o != null) {
					System.out.println(o);
				}
				script = "";
			}
			else if (check < 0) {
				System.out.println("(Cancelled)...");
				script = "";
			}
			else {
				System.out.print("   ");
			}
		}
	}
		
	public static void main(String[] args) {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		if (args.length == 1) {
			String path = args[0];
			k.loadScript(ctx, path);
		}
		else if (args.length == 0){
			k.shell(ctx);
		}
		else {}// TODO option
	}
	
}
