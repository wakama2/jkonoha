package jkonoha;

import java.util.*;

import jkonoha.compiler.kobject.KInt;

public abstract class Syntax {
	public String kw;   // id
	public int flag; // flag
	public String rule;
	public List<Token> syntaxRuleNULL;

	public int ty = TY.unknown;        // "void" ==> TY_void
	public int priority;  // op2   
	public String op2;
	public String op1;
	
	public Syntax(String kw) {
		this.kw = kw;
	}
	
	public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO undefinedParseExpr ast.h:518
		return null;
	}
	
	public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		//TODO default parseStmt?
		return 0;
	}

	public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		//TODO tycheck.h:107
		return null;
	}
	
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		//TODO undefinedStmtTyCheck tycheck.h:734
		return false;
	}
	
	public final/*FIXME*/ boolean topStmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		return stmtTyCheck(ctx, stmt, gamma);
	}
}

class ERRSyntax extends Syntax {
	public ERRSyntax() {
		super("$ERR");
		this.flag = SYNFLAG.StmtBreakExec;
	}
}

class ExprSyntax extends Syntax {
	public ExprSyntax() {
		super("$expr");
		this.rule = "$expr";
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token.dumpTokenArray (System.out, 0, tls, s, e);
		Expr expr = stmt.newExpr2(ctx, tls, s, e);
		if (expr != null) {
			expr.dump(System.out, 0, 0);
			stmt.setObject(name, expr);
			r = e;
		}
		return r;
	}
	@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
		boolean r = stmt.tyCheckExpr(ctx, KW.Expr, gamma, TY.var, TPOL.ALLOWVOID);
		stmt.typed(TSTMT.EXPR);
		return r;
	}
}

abstract class TermSyntax extends Syntax {
	public TermSyntax(String kw) {
		super(kw);
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO src/sugar/ast.h:638 ParseExpr_Term
		assert(s == c);
		Token tk = tls.get(c);
		Expr expr = new Expr(this);
		expr.syn = stmt.parentNULL.ks.syntax(ctx, tk.kw);
		expr.setTerm(true);
		expr.tk = tk;
		return expr;
	}
}

class SYMBOLSyntax extends TermSyntax {
	public SYMBOLSyntax() {
		super("$SYMBOL");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.SYMBOL) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
//	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
//		return expr.tyCheckVariable2(ctx, gamma, ty);
//	}
}

class USYMBOLSyntax extends TermSyntax {
	public USYMBOLSyntax() {
		super("$USYMBOL");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.USYMBOL) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
//	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
//		DBG_P("USYMBOL...");
//		Token tk = expr.tk;
//		int ukey = kuname(S_text(tk.text), S_size(tk.text), 0, FN_NONAME);
//		if(ukey != FN_NONAME){
//			Kvs kv = gamma.genv.ks.getConstNULL(ctx, ukey);
//			if(kv != null) {
//				if(FN_isBOXED(kv.key)) {
//					expr.setConstValue(kv.ty, kv.oval);
//				}
//				else {
//					expr.setNConstValue(kv.ty, kv.uval);
//				}
//				return expr;
//			}
//		}
//		KObject v = gamma.genv.ks.getSymbolValueNULL(ctx, S_text(tk.text), S_size(tk.text));
//		Expr texpr = (v == null) ?
//				kToken_p(tk, ERR_, "undefined name: %s", kToken_s(tk)) : kExpr_setConstValue(expr, O_cid(v), v);
//				return texpr;
//	}
}

class TextSyntax extends TermSyntax {
	public TextSyntax() {
		super("$TEXT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		//return expr.serConstValue(ctx, gamma, ty);
		return null;
	}
}

class IntSyntax extends TermSyntax {
	public IntSyntax() {
		super("$INT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
		Token tk = expr.tk;
		long l = Long.parseLong(tk.text);
		return new ConstExpr(this, KInt.box(l));
	}
}

class FloatSyntax extends TermSyntax {
	public FloatSyntax() {
		super("$FLOAT");
		this.flag = SYNFLAG.ExprTerm;
	}
}

class TypeSyntax extends TermSyntax {
	public TypeSyntax() {
		super("$type");
		this.rule = "$type $expr";
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.kw == KW.Type) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
//	@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Object gamma) {
//		Token tk  = stmt.token(KW.Type, null);
//		Expr expr = stmt.expr(KW.Expr, null);
//		if(tk == null || tk.kw != KW.Type || expr == null) {
//			ERR_SyntaxError(stmt.uline);
//			return false;
//		}
//		stmt.done(); //kStmt_done(stmt)
//		return expr.declType(ctx, gamma, tk.ty, stmt);
//	}
//	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Object gamma, int ty) {
//		assert(expr.tk.kw == KW.Type);
//		return expr.setVariable(null, expr.tk.ty, 0, gamma);
//	}
}

class AST_ParenthesisSyntax extends Syntax {
	public AST_ParenthesisSyntax() {
		super("()");
		this.flag = SYNFLAG.ExprPostfixOp2;
		this.priority = 16;
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		Token tk = tls.get(c);
		if(s == c) {
			Expr expr = stmt.newExpr2(ctx, tk.sub, 0, tk.sub.size());
			return expr;
		}
		else {
			Expr lexpr = stmt.newExpr2(ctx, tls, s, c);
			if(lexpr == null) {
				return lexpr;
			}
			if(lexpr.syn.kw.equals(KW.DOT)) {
				lexpr.syn = stmt.parentNULL.ks.syntax(ctx, KW.ExprMethodCall); // CALL
			}
			else if(!lexpr.syn.kw.equals(KW.ExprMethodCall)) {
				Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW.Parenthesis);    // (f null ())
				Expr l = new Expr(syn);
				l.setCons(lexpr, null);
				lexpr = l;
			}
			lexpr = stmt.addExprParams(ctx, lexpr, tk.sub, 0, tk.sub.size(), 1/*allowEmpty*/);//TODO
			return lexpr;
		}
	}
}

class AST_BracketSyntax extends Syntax {

	public AST_BracketSyntax() {
		super("[]");
		// TODO Auto-generated constructor stub
	}

}

class AST_BraceSyntax extends Syntax {

	public AST_BraceSyntax() {
		super("{}");
		// TODO Auto-generated constructor stub
	}

}

class BlockSyntax extends Syntax {
	public BlockSyntax() {
		super("$block");
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		Token tk = tls.get(s);
		if (tk.tt == TK.CODE) {
			stmt.setObject(name, tk);
			return (s+1);
		}
		else if (tk.tt == TK.AST_BRACE) {
			Block bk =  Parser.newBlock(ctx, stmt.parentNULL.ks, stmt, tk.sub, 0, tk.sub.size(), ';');
			stmt.setObject(name, bk);
			return (s+1);
		}
		else {
			Block bk =  Parser.newBlock(ctx, stmt.parentNULL.ks, stmt, tls, s, e, ';');
			stmt.setObject(name, bk);
			return e;
		}
	}
}

class ParamsSyntax extends Syntax {
	public ParamsSyntax () {
		super("$params");
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		int r = -1;
		Token tk = tls.get(s);
		if(tk.tt == TK.AST_PARENTHESIS) {
			tls = tk.sub;
			int ss = 0, ee = tls.size();
			if(0 < ee && tls.get(0).kw.equals(KW._void)) ss = 1;  //  f(void) = > f()
			Block bk = Parser.newBlock(ctx, stmt.parentNULL.ks, stmt, tls, ss, ee, ',');
			stmt.setObject(name, bk);
			r = s + 1;
		}
		return r;
	}
}

class ToksSyntax extends Syntax {
	public ToksSyntax () {
		super("$toks");
	}
	@Override public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		if(s < e) {
			List<Token> a = new ArrayList<Token>();
			while(s < e) {
				a.add(tls.get(s));
				s++;
			}
			stmt.setObject(name, a);
			return e;
		}
		return (-1);
	}
}


class DotSyntax extends Syntax {
	public DotSyntax() {
		super(".");
	}
	
	private boolean isFileName(List<Token> tls, int c, int e){
		if(c+1 < e) {
			Token tk = tls.get(c+1);
			return (tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL || tk.tt == TK.MSYMBOL);
		}
		return false;
	}

	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//DBG_P("s=%d, c=%d", s, c);
		assert(s < c);
		if(isFileName(tls, c, e)) {
			Expr expr = stmt.newExpr2(ctx, tls, s, c);
			expr = new ConsExpr(this);
			return expr;
		}
		if(c + 1 < e) c++;
		//return kToken_p(tls.toks[c], ERR_, "expected field name: not %s", kToken_s(tls.toks[c])));
		return null;//TODO
	}
}

abstract class OpSyntax extends Syntax {
	public OpSyntax(String kw) {
		super(kw);
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO src/sugar/ast.h:650 ParseExpr_Op
		Token tk = tls.get(c);
		Expr expr, rexpr = stmt.newExpr2(ctx, tls, c+1, e);
		String mn = (s ==c) ? this.op1 : this.op2;
		if (mn != null /*TODO && this.exprTyCheck(ctx, rexpr, gamma, e)*/) {
			//TODO
		}
		if (s == c) {
			expr = new Expr(this);
			expr.setCons(rexpr);
		}
		else {
			Expr lexpr = stmt.newExpr2(ctx, tls, s, c);
			expr = new Expr(this);
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
		this.op2 = "opMul";
		this.priority = 32;
	}
}

class AddSyntax extends OpSyntax {
	public AddSyntax() {
		super("+");
		this.flag = SYNFLAG.ExprOp;
		this.op1 = "opPULS";
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

class OPLEFTSyntax extends Syntax {
	public OPLEFTSyntax () {
		super("=");
		this.flag = (SYNFLAG.ExprOp | SYNFLAG.ExprLeftJoinOp2);
		this.priority = 4096;
	}
}

class COMMASyntax extends Syntax {
	public COMMASyntax () {
		super(",");
		//this.flag = SYNFLAG.ExprOp;
		//this.op1 = "opNOT";
		this.op2 = "*";
		this.priority = 8192;
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		Expr expr = new Expr(this);
		expr.setCons(tls.get(c));
		stmt.addExprParams(ctx, expr, tls, s, e, 0);
		return expr;
	}
}

class DOLLARSyntax extends Syntax {
	public DOLLARSyntax () {
		super("$");
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		if (s == c && c + 1 < e) {
			Token tk = tls.get(c + 1);
			if (tk.tt == TK.CODE) {
				//TODO Token_toBRACE(_ctx, (struct _kToken*)tk, kStmt_ks(stmt));
			}
			if (tk.tt == TK.AST_BRACE) {
				Expr expr = new Expr(this);
				//expr.setTerm(expr, 1);//TODO
				expr.tk = tk;
				expr.block = Parser.newBlock(ctx, stmt.parentNULL.ks, stmt, tk.sub, 0, tk.sub.size(), ';');
				return expr;
				}
			}
			//RETURN_(kToken_p(tls->toks[c], ERR_, "unknown %s parser", kToken_s(tls->toks[c])));
			return null;
		}
	}

class VOIDSyntax extends Syntax {
	public VOIDSyntax () {
		super("void");
		this.ty = TY.VOID;
		this.rule = "$type [$USYMBOL \".\"] $SYMBOL $params [$block]";
	}
}

class BOOLEANSyntax extends Syntax {
	public BOOLEANSyntax () {
		super("boolean");
		this.ty = TY.BOOLEAN;
	}
}

// can't name INTSyntax, why??
class INTTypeSyntax extends Syntax {
	public INTTypeSyntax () {
		super("int");
		this.ty = TY.INT;
	}
}

class TRUESyntax extends TermSyntax {
	public TRUESyntax () {
		super("true");
		this.flag = SYNFLAG.ExprTerm;
	}
}

class FALSESyntax extends TermSyntax {
	public FALSESyntax () {
		super("false");
		this.flag = SYNFLAG.ExprTerm;
	}
}

class IFSyntax extends Syntax {
	public IFSyntax() {
		super("if");
		this.rule = "\"if\" \"(\" $expr \")\" $block [\"else\" else: $block]";
	}
}

class ELSESyntax extends Syntax {
	public ELSESyntax () {
		super("else");
		this.rule = "\"else\" $block";
	}
}
class RETURNSyntax extends Syntax {
	public RETURNSyntax() {
		super("return");
		this.rule = "\"return\" [$expr]";
		this.flag = SYNFLAG.StmtBreakExec;
	}
}
