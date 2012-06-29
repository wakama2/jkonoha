package jkonoha;

import java.io.*;
import java.util.*;

public class Konoha {
	
	private final CTX defaultCtx = new CTX();
	
	//TODO
	public Konoha(CTX ctx) {}
	public Konoha() {}
	
	public KObject eval(String source) {
		return eval(defaultCtx, source);
	}
	
	public KObject eval(CTX ctx, String source) {
		return ctx.ks.eval(ctx, source, 0);
	}
	
	public void loadScript(String path) {
		loadScript(defaultCtx, path);
	}
	
	public void loadScript(CTX ctx, String path) {
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
	
	public void shell() {
		shell(defaultCtx);
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
		Konoha k = new Konoha();
		if (args.length == 1) {
			String path = args[0];
			k.loadScript(path);
		}
		else if (args.length == 0){
			k.shell();
		}
		else {}// TODO option
	}
	
}
