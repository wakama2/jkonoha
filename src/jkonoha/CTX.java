package jkonoha;

public class CTX extends KObject {
	public Konoha konoha;
	public CtxSugar ctxsugar = new CtxSugar();
	public ModSugar modsugar = new ModSugar();

	public long kfileid(String name, long def)
	{
		return this.konoha.kfileid(name, def);
	}

	public String S_file(long uline) {
		return this.konoha.S_file(uline);
	}

	public final Block NULLBLOCK = null;//TODO
	
	public KClass ct(int ty) {
		return null;//TODO
	}
	
//	public static final int MOD_logger = 0;
////	public static final int MOD_gc = 1;
//	public static final int MOD_code = 2;
//	public static final int MOD_sugar = 3;
//	public static final int MOD_float = 11;
////	public static final int MOD_jit = 12;
//	public static final int MOD_iconv = 13;
//	public static final int MOD_IO = 14;
//	public static final int MOD_llvm = 15;
//	public static final int MOD_REGEX = 16;
	
//	public final KClass CT_Token() {
//		return ((KModSugar)this.kmodsugar()).cToken;
//	}
//	
//	public final KClass CT_Expr() {
//		return ((KModSugar)this.kmodsugar()).cExpr;
//	}
//	
//	public final KClass CT_Stmt() {
//		return ((KModSugar)this.kmodsugar()).cStmt;
//	}
//	
//	public final KClass CT_Block() {
//		return ((KModSugar)this.kmodsugar()).cBlock;
//	}
//	
//	public final KClass CT_KonohaSpace() {
//		return ((KModSugar)this.kmodsugar()).cKonohaSpace;
//	}
//	
//	public final KClass CT_Gamma() {
//		return ((KModSugar)this.kmodsugar()).cGamma;
//	}
//	
//	public final KClass CT_TokenArray() {
//		return ((KModSugar)this.kmodsugar()).cTokenArray;
//	}

}
