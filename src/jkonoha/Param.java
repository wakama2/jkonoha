package jkonoha;

class Param {
	int ty;
	int fn;
	
	public void setParam(CTX ctx, int rtype, int psize, Param[] p) {
		int paramid = Kparam(ctx, rtype, psize, p);
		if(this != null) {
			KMethod mtd = this;
			mtd.paramdom = Kparamdom(ctx, psize, p);
			mtd.paramid  = paramid;
		}
		return ctx.share.paramList.params[paramid];
	}
	
	
	private int Kparam(CTX ctx, int rtype, int psize, Param[] p) {
		int hcode = hashparam(rtype, psize, p);
		return Kmap_getparamid(ctx, ctx.share.paramMapNN, ctx.share.paramList, hcode, equalsParam, rtype, psize, this);
	}
	
	static int Kparamdom(CTX ctx, int psize, Param[] p)
	{
		int hcode = hashparamdom(psize, p);
		return Kmap_getparamid(ctx, ctx.share.paramdomMapNN, ctx.share.paramdomList, hcode, equalsParamDom, TY_void, psize, this);
	}
	
	private int hashparam(int rtype, int psize, Param[] p)
	{
		int i, hcode = rtype;
		for(i = 0; i < psize; i++) {
			hcode = (p[i].ty + p[i].fn) + (31 * hcode);
		}
		return hcode;
	}
	
	private int hashparamdom(int psize, Param[] p)
	{
		int i, hcode = 0;
		for(i = 0; i < psize; i++) {
			hcode = p[i].ty + (31 * hcode);
		}
		return hcode;
	}
}
