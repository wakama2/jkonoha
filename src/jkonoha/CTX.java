package jkonoha;

import java.io.PrintStream;

import jkonoha.ast.*;

public class CTX extends KObject {
	
	public final boolean debug = true;
	
	public Konoha konoha;
	public KonohaSpace ks = new KonohaSpace();
	public Gamma gamma = new Gamma();
	public KonohaClass scriptClass = new KonohaClass("Script", KClass.objectClass, new KClass[0]);

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
	
}
