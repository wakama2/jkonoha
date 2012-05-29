package commons.sugar;

import sugar.K_Token;
import commons.konoha2.kclass.*;
import commons.konoha2.*;

public class CtxSugar extends KModLocal {//joseph:ctxsugar_t in original konoha2 (/include/konoha2/sugar.h)
	public K_Array<K_Token>		tokens;
//	public KArray		cwb;	TODO karray_t is a struct including union.
	public int			errCount;
	public K_Array		errors;	// TODO generics
	public K_Block		singleBlock;
	public K_Gamma		gma;
	public K_Array		lvarlst; //	TODO generics
	public K_Array		definedMethods; // TODO generics
}
