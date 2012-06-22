package jkonoha;

public class LocalCtx {
	private static final ThreadLocal<CTX> ctxs = new ThreadLocal<CTX>();
	
	public static void set(CTX ctx) {
		ctxs.set(ctx);
	}
	
	public static CTX get() {
		return ctxs.get();
	}
	
}
