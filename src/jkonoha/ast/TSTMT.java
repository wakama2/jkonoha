package jkonoha.ast;

public interface TSTMT {
	int UNDEFINED = 0;
	int ERR = 1;
	int EXPR = 2;
	int BLOCK = 3;
	int RETURN = 4;
	int IF = 5;
	int LOOP = 6;
	int JUMP = 7;
}
