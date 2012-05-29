package jkonoha.compiler.kobject;

class KDefineSyntax {
	final String name;
	int kw;
	int flags;
	String rule;
	String op2;
	String op1;
	int priority_op2;
	int type;
	
	public KDefineSyntax(String name) {
		this.name = name;
	}
	
	// default methods
	public void parseStmt() {}
	public void parseExpr() {}
	public void topStmtTyCheck() {}
	public void stmtTyCheck() {}
	public void exprTyCheck() {}
}