package jkonoha;

public class KMethod extends KObject {
	
	public int flag;
	public int cid;
	public int mn;
	public int paramdom;
	public int paramid;
	public static int Public  = (1<<0);
	
	public KMethod(CTX ctx, int flag, int cid, int mn, int func) {
		this.flag = flag;
		this.cid = cid;
		this.mn = mn;
		setFunc(func);
	}
	
	public void setFunc(int func) {
		
	}
}
