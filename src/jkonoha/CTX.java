package jkonoha;

import java.io.PrintStream;

import jkonoha.compiler.KonohaClass;

public class CTX extends KObject {
	
	private static final boolean debug = true;
	
	public Konoha konoha;
	public CtxSugar ctxsugar = new CtxSugar();
	public ModSugar modsugar = new ModSugar();
	public KonohaClass scriptClass = new KonohaClass("Script", KClass.objectClass, new KClass[0]);

	public long kfileid(String name, long def)
	{
		return this.konoha.kfileid(name, def);
	}

	public String S_file(long uline) {
		return this.konoha.S_file(uline);
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
	
}
