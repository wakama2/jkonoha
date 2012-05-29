package commons.sugar;

public class KDEFINE_SYNTAX {
	String name;
	int kw;  int flag;
	String rule;
	String op2;
	String op1;
	int priorityOp2;
	int type;
	// TODO knh_Fmethod = ?
	Knh_Fmethod ParseStmt;
	knh_Fmethod ParseExpr;
	knh_Fmethod TopStmtTyCheck;
	knh_Fmethod StmtTyCheck;
	knh_Fmethod ExprTyCheck;
}
