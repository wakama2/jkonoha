package commons.sugar;

import commons.konoha2.kclass.*;

public class Kopl {//joseph kopl_t in original konoha2 (/module/classicvm/vm.h)
	//KCODE_HEAD;//TODO
	
	/*union*/
	long data[] = new long[5];
	//void *p[5];//TODO
	K_Object[] o = new K_Object[5];
	KClass[] ct = new KClass[5];
	String[] u = new String[5];
}
