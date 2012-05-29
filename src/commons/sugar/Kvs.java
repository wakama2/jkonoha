package commons.sugar;

import commons.konoha2.kclass.*;

public class Kvs {//joseph: kvs_t in original konoha2 (/include/konoha2/konoha2.h)
	int key;
	int ty;
	
	/*union*/
	long uval;
	K_Object oval;
	K_String sval;//TODO const struct _kString
}
