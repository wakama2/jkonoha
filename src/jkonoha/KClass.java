package jkonoha;

import java.util.*;

import jkonoha.compiler.KField;

public class KClass {
	public int cid;
	public int cflag;
	public int bcid;
	public int supcid;
	public int p0;
//	//-add--------------------
//	public int packid;
//	public int paramdom;
//	public int magivflag;
//	public int cstruct_size;
//	public KField fields; //TODO KField ?
//	public int fsize;
//	public int fallocsize;
//	public String DBG_NAME;
//	public int nameid;
//	public int optvalue;
//	public List<Object> methods = new ArrayList<Object>();
//	public String shortNameNULL;
//	public KObject nulvalNUL;
//	public KObject WnulvalNUL;
//	public Map<String, Syntax> constPoolMapNO = new HashMap<String, Syntax>();
//	public KClass searchSimilarClassNULL;
//	public KClass searchSuperMethodClassNULL;
//	//----------------------------
//	public static final int CLASS_Func = 0;
//	public static final int CT_Array = 0;
	
/*	public KClass generics(CTX ctx, int rtype, int psize, Param[] p) { //at src/konoha/datatype.h
		int paramdom = Param.Kparamdom(ctx, psize, p);
		KClass ct0 = this;
		boolean isNotFuncClass = (bcid != CLASS_Func);
		do {
			if(this.paramdom == paramdom && (isNotFuncClass || p0 == rtype)) {
				return this;
			}
			if(searchSimilarClassNULL == null) break;
			this = searchSimilarClassNULL;
		} while(this != null);
		KClass newct = new_CT(ctx, ct0, null, NOPLINE);
		newct.paramdom = paramdom;
		newct.p0 = isNotFuncClass ? p[0].ty : rtype;
		newct.methods =  K_EMPTYARRAY;
		if(newct.searchSuperMethodClassNULL == null) {
			newct.searchSuperMethodClassNULL = ct0;
		}
		((KClass)this).searchSimilarClassNULL = (KClass)newct;
		return searchSimilarClassNULL;
	}
	
	KClass new_CT(CTX ctx, KDEFINE_CLASS s, int pline) {
		kshare_t *share = ctx.share; // TODO kshare_t?
		int newid = share.ca.bytesize / sizeof(KClass); // sizeof?
		if(share.ca.bytesize == share.ca.bytemax) {
			KARRAY_EXPAND(&share.ca, share.ca.bytemax * 2);
		} // not necessary?
		share.ca.bytesize += sizeof(KClass);
		KClass ct = (struct _kclass*)KCALLOC(sizeof(kclass_t), 1);
		share.ca.cts[newid] = (KClass)ct;
		if(bct != null) {
			assert(s == null);
			memcpy(ct, bct, offsetof(kclass_t, methods)); //TODO offsetof?
			ct.cid = newid;
			if(ct.fnull == DEFAULT_fnull) ct.fnull =  DEFAULT_fnullinit;
		}
		else {
			assert(s != null);
			ct.cflag   = s.cflag;
			ct.cid     = newid;
			ct.bcid    = (s.bcid == 0) ? newid : s.bcid;
			ct.supcid  = (s.supcid == 0) ? CLASS_Object : s.supcid;
			ct.fields = s.fields;
			ct.fsize  = s.fsize;
			ct.fallocsize = s.fallocsize;
			ct.cstruct_size = size64(s.cstruct_size);
			assert(ct.cstruct_size <= 128);
			ct.DBG_NAME = (s.structname != null) ? s.structname : "N/A";
			if(s.psize > 0 && s.cparams != null) {
				ct.p0 = s.cparams[0].ty;
				ct.paramdom = Kparamdom(ctx, s.rtype, s.psize, s.cparams);
			}
			// function
			ct.init = (s.init != null) ? s.init : DEFAULT_init;
			ct.reftrace = (s.reftrace != null) ? s.reftrace : DEFAULT_reftrace;
			ct.p     = (s.p != null) ? s.p : DEFAULT_p;
			ct.unbox = (s.unbox != null) ? s.unbox : DEFAULT_unbox;
			ct.free = (s.free != null) ? s.free : DEFAULT_free;
			ct.fnull = (s.fnull != null) ? s.fnull : DEFAULT_fnullinit;
			ct.realtype = (s.realtype != null) ? s.realtype : DEFAULT_realtype;
			ct.isSubType = (s.isSubType != null) ? s.isSubType : DEFAULT_isSubType;
			ct.initdef = s.initdef;
		}
		if(ct.initdef != null) {
			ct.initdef(ctx, ct, pline);
		}
		return ct;
	}
	
	public KClass CT_p0(CTX ctx, int ty) {
		Param p[] = new Param[1];
		p[0].ty = ty;
		p[0].fn = 0;
		return generics(ctx, TY.VOID, 1, p);
	}*/
}
