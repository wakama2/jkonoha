package jkonoha;

public interface TEXPR {
	int LOCAL_   = -4;   /*THIS IS NEVER PASSED*/
	int BLOCK_   = -3;   /*THIS IS NEVER PASSED*/
	int FIELD_   = -2;   /*THIS IS NEVER PASSED*/
	//int shift    = (TEXPR_LOCAL - (TEXPR_LOCAL_))
	int UNTYPED  = -1;   /*THIS MUST NOT HAPPEN*/
	int CONST    =  0;
	int NEW      =  1;
	int NULL     =  2;
	int NCONST   =  3;
	int LOCAL    =  4;
	int BLOCK    =  5;
	int FIELD    =  6;
	int BOX      =  7;
	int UNBOX    =  8;
	int CALL     =  9;
	int AND      = 10;
	int OR       = 11;
	int LET      = 12;
	int STACKTOP = 13;
	int MAX      = 14;
}

