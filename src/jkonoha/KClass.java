package jkonoha;

public class KClass {
	public int cid;
	public int cflag;
	public int bcid;
	public int supcid;
	public int p0;
	public static final int CLASS_Func = 0;
	public static final int CT_Array = 0;
	
	public KClass generics(CTX ctx, int rtype, int psize, Param[] p) { //at src/konoha/datatype.h
		kparamid_t paramdom = Kparamdom(_ctx, psize, p); // TODO kparamid_t?
		KClass ct0 = this;
		boolean isNotFuncClass = (bcid != CLASS_Func);
		do {
			if(paramdom == paramdom && (isNotFuncClass || p0 == rtype)) {
				return this;
			}
			if(searchSimilarClassNULL == null) break;
			this = searchSimilarClassNULL;
		} while(this != null);
		KClass newct = new_CT(ctx, ct0, null, NOPLINE);
		newct.paramdom = paramdom;
		newct.p0 = isNotFuncClass ? p[0].ty : rtype;
		newct.methods =  K_EMPTYARRAY);
		if(newct.searchSuperMethodClassNULL == null) {
			newct.searchSuperMethodClassNULL = ct0;
		}
		((KClass)this).searchSimilarClassNULL = (KClass)newct;
		return searchSimilarClassNULL;
	}
	
	KClass new_CT(CTX ctx, KDEFINE_CLASS s, int pline) {
/*		kshare_t *share = _ctx.share;
		kcid_t newid = share.ca.bytesize / sizeof(struct _kclass*);
		if(share.ca.bytesize == share.ca.bytemax) {
			KARRAY_EXPAND(&share.ca, share.ca.bytemax * 2);
		}
		share.ca.bytesize += sizeof(struct _kclass*);
		struct _kclass *ct = (struct _kclass*)KCALLOC(sizeof(kclass_t), 1);
		share.ca.cts[newid] = (kclass_t*)ct;
		if(bct != NULL) {
			DBG_ASSERT(s == NULL);
			memcpy(ct, bct, offsetof(kclass_t, methods));
			ct.cid = newid;
			if(ct.fnull == DEFAULT_fnull) ct.fnull =  DEFAULT_fnullinit;
		}
		else {
			DBG_ASSERT(s != NULL);
			ct.cflag   = s.cflag;
			ct.cid     = newid;
			ct.bcid    = (s.bcid == 0) ? newid : s.bcid;
			ct.supcid  = (s.supcid == 0) ? CLASS_Object : s.supcid;
			ct.fields = s.fields;
			ct.fsize  = s.fsize;
			ct.fallocsize = s.fallocsize;
			ct.cstruct_size = size64(s.cstruct_size);
			DBG_ASSERT(ct.cstruct_size <= 128);
			ct.DBG_NAME = (s.structname != NULL) ? s.structname : "N/A";
			if(s.psize > 0 && s.cparams != NULL) {
				ct.p0 = s.cparams[0].ty;
				ct.paramdom = Kparamdom(_ctx, s.rtype, s.psize, s.cparams);
			}
			// function
			ct.init = (s.init != NULL) ? s.init : DEFAULT_init;
			ct.reftrace = (s.reftrace != NULL) ? s.reftrace : DEFAULT_reftrace;
			ct.p     = (s.p != NULL) ? s.p : DEFAULT_p;
			ct.unbox = (s.unbox != NULL) ? s.unbox : DEFAULT_unbox;
			ct.free = (s.free != NULL) ? s.free : DEFAULT_free;
			ct.fnull = (s.fnull != NULL) ? s.fnull : DEFAULT_fnullinit;
			ct.realtype = (s.realtype != NULL) ? s.realtype : DEFAULT_realtype;
			ct.isSubType = (s.isSubType != NULL) ? s.isSubType : DEFAULT_isSubType;
			ct.initdef = s.initdef;
		}
		if(ct.initdef != NULL) {
			ct.initdef(_ctx, ct, pline);
		}
		return ct;*/
	}
}
