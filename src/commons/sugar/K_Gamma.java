package commons.sugar;

import commons.konoha2.KObjectHeader;

public class K_Gamma {//joseph:kGamma in original konoha2 (/include/konoha2/sugar.h)
	public KObjectHeader h;
	public GmaBuf genv;

	public static final int TOPLEVEL = 1;
	public static final boolean isTOPLEVEL(K_Gamma GMA) {
				return TFLAG_is(GMA.genv.flag, TOPLEVEL);
	}
	private static boolean TFLAG_is (int f, int op) {//TODO
		return true;
	}
//	
	public static final int ERROR = (1<<1);
	public static final boolean isERROR (K_Gamma GMA) {
		return TFLAG_is(GMA.genv.flag, ERROR);
	}
	public static final void setERROR(K_Gamma GMA, boolean B) {
		if(B) {
			TFLAG_set1 (GMA.genv.flag, ERROR);
		} else {
			TFLAG_set0 (GMA.genv.flag, ERROR);
		}
	}
	private static final int TFLAG_set1 (int f, int op) {
		return f | op;
	}
	private static final int TFLAG_set0 (int f, int op) {
		return f & (~op);
	}

	/*TODO
		#define kGamma_TOPLEVEL        (kflag_t)(1)
		#define kGamma_isTOPLEVEL(GMA)  TFLAG_is(kflag_t, GMA->genv->flag, kGamma_TOPLEVEL)
		#define kGamma_ERROR           (kflag_t)(1<<1)
		#define kGamma_isERROR(GMA)    TFLAG_is(kflag_t, GMA->genv->flag, kGamma_ERROR)
		#define kGamma_setERROR(GMA,B) TFLAG_set(kflag_t, GMA->genv->flag, kGamma_ERROR, B)
	 */
}
