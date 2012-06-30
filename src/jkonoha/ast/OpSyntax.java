package jkonoha.ast;

import java.util.List;

import jkonoha.CTX;
import jkonoha.MNTYPE;

public abstract class OpSyntax extends Syntax {
	public OpSyntax(String kw) {
		super(kw);
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		Token tk = tls.get(c);
		Expr expr, rexpr = stmt.newExpr2(ctx, tls, c+1, e);
		String mn = (s == c) ? op1 : op2;
		Syntax syn = this;
		if (mn != null) {
			tk.tt = TK.MN;
			tk.mn = mn;
			tk.mn_type = (s == c) ? MNTYPE.unary : MNTYPE.binary;
			syn = stmt.parentNULL.ks.syntax(ctx, KW.ExprMethodCall);
		}
		if (s == c) {
			expr = new Expr(syn);
			expr.setCons(tk, rexpr);
		}
		else {
			Expr lexpr = stmt.newExpr2(ctx, tls, s, c);
			expr = new Expr(syn);
			expr.setCons(tk, lexpr, rexpr);
		}
		return expr;
	}
}

class DivSyntax extends OpSyntax {
	public DivSyntax() {
		super("/");
		this.op2 = "opDIV";
		this.priority = 32;
	}
}

class ModSyntax extends OpSyntax {
	public ModSyntax() {
		super("%");
		this.op2 = "opMOD";
		this.priority = 32;
	}
}

class MulSyntax extends OpSyntax {
	public MulSyntax() {
		super("*");
		this.op2 = "opMUL";
		this.priority = 32;
	}
}

class AddSyntax extends OpSyntax {
	public AddSyntax() {
		super("+");
		this.flag = SYNFLAG.ExprOp;
		this.op1 = "opPLUS";
		this.op2 = "opADD";
		this.priority = 64;
	}
}

class SubSyntax extends OpSyntax {
	public SubSyntax() {
		super("-");
		this.op1 = "opMINUS";
		this.op2 = "opSUB";
		this.priority = 64;
	}
}

class LTSyntax extends OpSyntax {
	public LTSyntax () {
		super("<");
		this.flag = SYNFLAG.ExprOp;
		this.op2 = "opLT";
		this.priority = 256;
	}
}

class LTESyntax extends OpSyntax {
	public LTESyntax () {
		super("<=");
		this.flag = SYNFLAG.ExprOp;
		this.op2 = "opLTE";
		this.priority = 256;
	}
}

class GTSyntax extends OpSyntax {
	public GTSyntax () {
		super(">");
		this.flag = SYNFLAG.ExprOp;
		this.op2 = "opGT";
		this.priority = 256;
	}
}

class GTESyntax extends OpSyntax {
	public GTESyntax () {
		super(">=");
		this.flag = SYNFLAG.ExprOp;
		this.op2 = "opGTE";
		this.priority = 256;
	}
}

class EQSyntax extends OpSyntax {
	public EQSyntax () {
		super("==");
		this.flag = SYNFLAG.ExprOp;
		this.op2 = "opEQ";
		this.priority = 512;
	}
}

class NEQSyntax extends OpSyntax {
	public NEQSyntax () {
		super("!=");
		this.flag = SYNFLAG.ExprOp;
		this.op2 = "opNEQ";
		this.priority = 512;
	}
}

class ANDSyntax extends OpSyntax {
	public ANDSyntax () {
		super("&&");
		this.flag = SYNFLAG.ExprOp;
		this.priority = 1024;
	}
}

class ORSyntax extends OpSyntax {
	public ORSyntax () {
		super("||");
		this.flag = SYNFLAG.ExprOp;
		this.priority = 2048;
	}
}

class NOTSyntax extends OpSyntax {
	public NOTSyntax () {
		super("!");
		this.flag = SYNFLAG.ExprOp;
		this.op1 = "opNOT";
	}
}

class OPLEFTSyntax extends OpSyntax {
	public OPLEFTSyntax () {
		super("=");
		this.flag = (SYNFLAG.ExprOp | SYNFLAG.ExprLeftJoinOp2);
		this.priority = 4096;
	}
}
