package commons.konoha2;

import commons.sugar.*;

public final class KObjectHeader {
	public int magicflag;
	KClass ct; //RENAME
	
	/*union*/
	public int refc;
	//void *gcinfo;
	public int hashcode;
	
	public KArray kvproto;
	// TODO
}