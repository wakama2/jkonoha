package jkonoha;

public class KMethod extends KObject {
	
	public int flag;
	public int cid;
	public int mn;
	public int paramdom;
	public int paramid;
	public static int Public  = (1<<0);
	public static int Virtual  = (1<<1);
	public static int Hidden = (1<<2);
	public static int Const  = (1<<3);
	public static int Static  = (1<<4);
	public static int Immutable  = (1<<5);
	public static int Restricted  = (1<<6);
	public static int Overloaded  = (1<<7);
	public static int CALLCC  = (1<<8);
	public static int FASTCALL  = (1<<9);
	public static int D  = (1<<10);
	public static int Abstract  = (1<<11);
	public static int Coercion  = (1<<12);
	public static int SmartReturn  = (1<<13);
	
	public KMethod(CTX ctx, int flag, int cid, int mn, int func) {
		this.flag = flag;
		this.cid = cid;
		this.mn = mn;
		setFunc(func);
	}
	
	public void setParam(CTX ctx, int rtype, int psize) {//TODO
/*		int paramid = Kparam(ctx, rtype, psize, p); 
		if(this != null) {
			KMethod mtd = this;
			mtd.paramdom = p[psize].Kparamdom(ctx);
			mtd.paramid  = paramid;
		}
		return ctx.share.paramList.params[paramid];*/
	}
	
	public void setFunc(int func) {//TODO
		/*static void Method_setFunc(CTX, kMethod *mtd, knh_Fmethod func)// in src/vm/asm.c : 993
		{
			func = (func == NULL) ? Fmethod_abstract : func;
			((struct _kMethod*)mtd)->fcall_1 = func;
			((struct _kMethod*)mtd)->pc_start = CODE_NCALL;
		 }*/
	}
}
