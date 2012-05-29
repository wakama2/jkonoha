package commons.sugar;

import commons.konoha2.kclass.*;

public class K_Block extends K_Object {
	K_KonohaSpace        ks;
	K_Stmt               parentNULL;
	K_Array<K_Object>    blocks;
	K_Expr               esp;
}
