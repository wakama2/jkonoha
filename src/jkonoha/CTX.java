package jkonoha;

import java.io.PrintStream;

import jkonoha.ast.*;

public class CTX extends KObject {
	
	public final boolean debug = true;
	
	public final KonohaSpace ks;
	public Gamma gamma = new Gamma();
	public KonohaClass scriptClass = new KonohaClass("Script", KClass.objectClass, new KClass[0]);
	
	public CTX() {
		this.ks = new KonohaSpace();
		ks.defineDefaultSyntax(this);
	}

	public void SUGAR_P(PrintStream out, long uline, int pos, String fmt, Object...args) {
		out.printf(fmt, args);
	}
	
	public void Token_p(Token tk, PrintStream out, String fmt, Object...args) {
		out.printf(fmt, args);
	}
	
	public void DBG_P(String fmt, Object...args) {
		if(debug) {
			StackTraceElement e = Thread.currentThread().getStackTrace()[2];
			System.out.printf("DEBUG(%s.%s:%d) ", e.getClassName(), e.getMethodName() ,e.getLineNumber());
			System.out.printf(fmt, args);
			System.out.println();
		}
	}
	
//	public String Ksymbol2(CTX ctx, String name, int len, int spol, int def) {
//		int mask = 0;
//		if(name.charAt(1) == 'e' && name.charAt(2) == 't') {
//			if(name.charAt(0) == 'g' || name.charAt(0) == 'G') {
//				len -= 3; name += 3;
//				mask = MN.GETTER;
//			}
//			else if(name.charAt(0) == 's' || name.charAt(0) == 'S') {
//				len -= 3; name += 3;
//				mask = MN.SETTER;
//			}
//		}
//		else if(name.charAt(1) == 's' && (name.charAt(0) == 'i' || name.charAt(0) == 'I')) {
//			len -= 2; name += 2;
//			mask = MN.ISBOOL;
//		}
//		else if(name.charAt(0) == '@') {
//			len -= 1; name += 1;
//			mask = MN.Annotation;
//		}
//		int hcode = name.hashCode();
//		return Kmap_getcode(ctx, ctx.share.unameMapNN, ctx.share.unameList, name, len, hcode, spol | SPOL.ASCII, def) | mask;
//	}
}
