package commons.sugar;

public class KDEFINE_PACKAGE {
	int konohaChecksum;
	String name;
	String version;
	String libname;
	String note;
	// TODO kobool_t = ?
	kbool_t (*initPackage)(CTX, const struct _kKonohaSpace *, int, String**, kline_t);
	kbool_t (*setupPackage)(CTX, const struct _kKonohaSpace *, kline_t);
	kbool_t (*initKonohaSpace)(CTX, const struct _kKonohaSpace *, kline_t);
	kbool_t (*setupKonohaSpace)(CTX, const struct _kKonohaSpace *, kline_t);
	int konoha_revision;
}
