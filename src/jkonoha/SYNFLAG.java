package jkonoha;

interface SYNFLAG {
	int ExprTerm = 1;
	int ExprOp   = 1 << 1;
	int ExprLeftJoinOp2 = 1 << 2;
	int ExprPostfixOp2  = 1 << 3;
	int StmtBreakExec = 1 << 8;
	int StmtJumpAhead = 1 << 9;
	int StmtJumpSkip  = 1 << 10;
}