package commons.sugar;

import commons.konoha2.kclass.K_Object;

public class KArray {//joseph: karray_t in original konoha2 (/include/konoha2/konoha2.h)
	public int byteSize;
	
	/*union*/
	public String		bytebuf;
	public KClass		cts;
	public Kvs			kvs;
	public Kopl		opl;
	public K_Object	objects;//TODO
	public K_Object	refhead;
	
	/*union {	//TODO
		char  *bytebuf;
		const struct _kclass **cts;
		struct kvs_t          *kvs;
		struct kopl_t          *opl;
		const struct _kObject **objects;
		struct _kObject       **refhead;  // stack->ref
	};*/
	public int byteMax;
}
