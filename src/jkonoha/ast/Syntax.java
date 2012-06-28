package jkonoha.ast;

import java.util.*;
import jkonoha.*;

public abstract class Syntax {
	public String kw;   // id
	public int flag; // flag
	public String rule;
	public List<Token> syntaxRuleNULL;

	public KClass ty = null;//TY.unknown;        // "void" ==> TY_void
	public int priority;  // op2   
	public String op2;
	public String op1;
	
	public Syntax(String kw) {
		this.kw = kw;
	}
	
	public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		//TODO undefinedParseExpr ast.h:518
		throw new RuntimeException("parseExpr");
	}
	
	public int parseStmt(CTX ctx, Stmt stmt, String name, List<Token> tls, int s, int e) {
		//TODO default parseStmt?
		throw new RuntimeException("parseStmt");
	}

	public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		//TODO tycheck.h:107
		throw new RuntimeException("exprTyCheck");
	}
	
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		//TODO undefinedStmtTyCheck tycheck.h:734
		throw new RuntimeException("stmtTyCheck");
	}
	
	public final/*FIXME*/ boolean topStmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		return stmtTyCheck(ctx, stmt, gamma);
	}
	
	public static final Syntax[] defaultSyntax = {
		new ERRSyntax(),
		new ExprSyntax(),
		new SYMBOLSyntax(),
		new USYMBOLSyntax(),
		new TextSyntax(),
		new IntSyntax(),
		new FloatSyntax(),
		new TypeSyntax(),
		new AST_ParenthesisSyntax(),
		new AST_BracketSyntax(),
		new AST_BraceSyntax(),
		new BlockSyntax(),
		new ParamsSyntax(),
		new ToksSyntax(),
		new DotSyntax(),
		new DivSyntax(),
		new ModSyntax(),
		new MulSyntax(),
		new AddSyntax(),
		new SubSyntax(),
		new LTSyntax(),
		new LTESyntax(),
		new GTSyntax(),
		new GTESyntax(),
		new EQSyntax(),
		new NEQSyntax(),
		new ANDSyntax(),
		new ORSyntax(),
		new NOTSyntax(),
		new OPLEFTSyntax(),
		new COMMASyntax(),
		new DOLLARSyntax(),
		new VOIDSyntax(),
		new BOOLEANSyntax(),
		new INTTypeSyntax(),
		new TRUESyntax(),
		new FALSESyntax(),
		new IFSyntax(),
		new ELSESyntax(),
		new RETURNSyntax()
	};
		
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
		if(ctx.debug) {
			Token.dumpTokenArray (System.out, 0, tls, s, e);
		}
		Expr expr = stmt.newExpr2(ctx, tls, s, e);
		if (expr != null) {
			if(ctx.debug) {
				expr.dump(ctx, 0, 0);
			}
			stmt.setObject(name, expr);
			r = e;
		}
		return r;
	}
	@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		boolean r = stmt.tyCheckExpr(ctx, KW.Expr, gamma, KClass.varClass, TPOL.ALLOWVOID);
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
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Token tk = expr.tk;
		String ukey = tk.text;
		ctx.DBG_P("SYMBOL %s", ukey);
		if(gamma.argNames != null) {
			for(String s : gamma.argNames) {
				if(s.equals(ukey)) {
					expr.build = TEXPR.LOCAL;
					expr.ndata = ukey;
					expr.ty = KClass.intClass;
					return expr;
				}
			}
			if(gamma.locals.keySet().contains(ukey)) {
				expr.build = TEXPR.LOCAL;
				expr.ndata = ukey;
				expr.ty = gamma.locals.get(ukey);
				return expr;
			}
		}
		return null;
	}
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
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Token tk = expr.tk;
		String ukey = tk.text;
		ctx.DBG_P("USYMBOL %s", ukey);
		if(ukey.equals("K") || ukey.equals("Konoha")) {
			expr.ty = KClass.konohaSpaceClass;
			return expr;
		}
		KClass c = gamma.ks.getClass(ctx, ukey);
		if(c != null) {
			expr.ty = c;
			return expr;
		}
		return null;
	}
}

class TextSyntax extends TermSyntax {
	public TextSyntax() {
		super("$TEXT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Token tk = expr.tk;
		String s = tk.text;
		return new ConstExpr(this, KClass.stringClass, KString.box(s));
	}
}

class IntSyntax extends TermSyntax {
	public IntSyntax() {
		super("$INT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Token tk = expr.tk;
		long l = Long.parseLong(tk.text);
		return new ConstExpr(this, KClass.intClass, KInt.box(l));
	}
}

class FloatSyntax extends TermSyntax {
	public FloatSyntax() {
		super("$FLOAT");
		this.flag = SYNFLAG.ExprTerm;
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Token tk = expr.tk;
		double d = Double.parseDouble(tk.text);
		return new ConstExpr(this, KClass.floatClass, KFloat.box(d));
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
		if(tk.kw.equals(KW.Type)) {
			stmt.setObject(name, tk);
			r = s + 1;
		}
		return r;
	}
	@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		Token tk  = (Token)stmt.getObject(KW.Type);
		Expr expr_l = (Expr)stmt.getObject(KW.Expr);
		Expr lname = (Expr)expr_l.at(1);
		Expr expr = (Expr)expr_l.at(2);
		if(tk == null || !tk.kw.equals(KW.Type) || expr == null) {
			//ERR_SyntaxError(stmt.uline);
			return false;
		}
		gamma.locals.put(lname.tk.text, stmt.parentNULL.ks.getClass(ctx, tk.text));
		expr = expr.tyCheck(ctx, gamma, KClass.varClass, 0);
		
		Expr v = new Expr(null);
		v.tk = lname.tk;
		v.build = TEXPR.LOCAL;
		
		Expr e = new Expr(null);
		e.build = TEXPR.LET;
		e.ty = KClass.voidClass;
		e.setCons(null, v, expr);
		
		stmt.syntax = stmt.parentNULL.ks.syntax(ctx, KW.Expr);
		stmt.setObject(KW.Expr, e);
		stmt.build = TSTMT.EXPR;
		return true;//expr.declType(ctx, gamma, tk.ty, stmt);
	}
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		//assert(expr.tk.kw.equals(KW.Type));
		//return expr.setVariable(null, expr.tk.ty, 0, gamma);
		return expr;
	}
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
			lexpr = stmt.addExprParams(ctx, lexpr, tk.sub, 0, tk.sub.size(), true/*allowEmpty*/);//TODO
			return lexpr;
		}
	}
	
	@Override
	public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Expr e = (Expr)expr.cons.get(0);
		if(e.isTerm()) {
			KClass k = ctx.scriptClass;
			KMethod m = k.getMethod(e.tk.text, ty);
			expr.cons.set(0, m);
			for(int i=2; i<expr.cons.size(); i++) {
				expr.tyCheckAt(ctx, i, gamma, KClass.varClass, 0);
			}
			expr.cons.remove(1);
			expr.build = TEXPR.CALL;
			expr.ty = m.getReturnClass();
			return expr;
		}
		return null;
	}
}

class AST_BracketSyntax extends Syntax {

	public AST_BracketSyntax() {
		super("[]");
	}

}

class AST_BraceSyntax extends Syntax {

	public AST_BraceSyntax() {
		super("{}");
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
	
	private Expr tyCheckCallParams(CTX ctx, Expr expr, KMethod mtd, Gamma gma, KClass reqty) {
		int size = expr.cons.size();
		for(int i=2; i<size; i++) {
			Expr e = expr.tyCheckAt(ctx, i, gma, KClass.varClass, 0);
			if(e == null) {
				return null;
			}
		}
		//TODO param check
		expr.build = TEXPR.CALL;
		expr.ty = mtd.getReturnClass();
		return expr;
	}
	
	private String checkMN(String mn) {
		if(mn.equals("import")) {
			return "_import";
		}
		if(mn.equals("assert")) {
			return "_assert";
		}
		return mn;
	}
	
	private Expr lookupMethod(CTX ctx, Expr expr, KClass this_cid, Gamma gma, KClass reqty) {
		//TODO
		Token tk = (Token)expr.cons.get(0);
		if(tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL) {
			tk.mn = tk.text;
		}
		String mn = checkMN(tk.mn);
		KClass k = this_cid;
		KMethod mtd = k.getMethod(mn, reqty);
		if(mtd != null) {
			expr.cons.set(0, mtd);
			tyCheckCallParams(ctx, expr, mtd, gma, reqty);
			return expr;
		}
		throw new RuntimeException("method not found: " + k.getName() + "." + mn);
	}
	
	@Override public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		Expr texpr = expr.tyCheckAt(ctx, 1, gamma, KClass.varClass, 0);
		if(texpr != null) {
			KClass this_cid = texpr.ty;
			return lookupMethod(ctx, expr, this_cid, gamma, ty);
		}
		return null;
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
		this.priority = 16;
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
			Expr expr2 = new ConsExpr(this);
			expr2.setCons(tls.get(c+1), expr);
			return expr2;
		}
		if(c + 1 < e) c++;
		return null;
	}
}

abstract class OpSyntax extends Syntax {
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

class COMMASyntax extends Syntax {
	public COMMASyntax () {
		super(",");
		this.op2 = "*";
		this.priority = 8192;
	}
	@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
		Expr expr = new Expr(this);
		expr.setCons(tls.get(c));
		stmt.addExprParams(ctx, expr, tls, s, e, false);
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
				expr.setTerm(true);
				expr.tk = tk;
				expr.block = Parser.newBlock(ctx, stmt.parentNULL.ks, stmt, tk.sub, 0, tk.sub.size(), ';');
				return expr;
				}
			}
			//RETURN_(kToken_p(tls.toks[c], ERR_, "unknown %s parser", kToken_s(tls.toks[c])));
			return null;
		}
	}

class VOIDSyntax extends Syntax {
	public VOIDSyntax () {
		super("void");
		this.ty = KClass.voidClass;
		this.rule = "$type [$USYMBOL \".\"] $SYMBOL $params [$block]";
	}

	@Override
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		Block param = (Block)stmt.getObject(KW.Params);
		List<String> argNames = new ArrayList<String>();
		List<KClass> argTypes = new ArrayList<KClass>();
		for(Stmt s : param.blocks) {
			Expr e = (Expr)s.getObject(KW.Expr);
			Token t = (Token)s.getObject(KW.Type);
			argNames.add(e.tk.text);
			argTypes.add(gamma.ks.getClass(ctx, t.text));
		}
		//present for joseph
		String name = ((Token)stmt.getObject(KW.Symbol)).text;
		KClass retty = gamma.ks.getClass(ctx, ((Token)stmt.getObject(KW.Type)).text);
		KonohaClass klass = ctx.scriptClass; // class "A"
		// "A" : stmt KW.USYMBOL -> Token
		// gamma.cc.getClass("A")
		KonohaMethod mtd = new KonohaMethod(klass, KonohaMethod.ACC_STATIC,
				name, retty, argNames.toArray(new String[0]), argTypes.toArray(new KClass[0]));
		klass.addMethod(mtd);
		
		gamma.argNames = argNames;
		Token bkt = (Token)stmt.getObject(KW.Block);
		List<Token> tls = new ArrayList<Token>();
		int pos = tls.size();
		gamma.ks.tokenize(ctx, bkt.text, 0, tls);
		Block bk = Parser.newBlock(ctx, gamma.ks, stmt, tls, pos, tls.size(), ';');
		bk.tyCheckAll(ctx, gamma);
		gamma.cc.evalBlock(mtd, bk);
		gamma.argNames = null;
		return false;
	}

/*	@Override
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		KonohaClass c = KClass.scriptClass;
		
		KonohaMethod m = new KonohaMethod();
		c.addMethod(m);
		
		
		boolean r = false;
		KonohaSpace ks = gamma.genv.ks;
		int flag =  stmt.flag(ctx, MethodDeclFlag, 0);
		Param pa = stmt.newMethodParamNULL(ctx, gamma);
		if(TY_isSingleton(cid)) flag |= kMethod_Static;
		if(pa != NULL) {
			INIT_GCSTACK();
			kMethod *mtd = new_kMethod(flag, cid, mn, NULL);
			PUSH_GCSTACK(mtd);
			kMethod_setParam(mtd, pa->rtype, pa->psize, (kparam_t*)pa->p);
			if(kKonohaSpace_defineMethod(ks, mtd, stmt->uline)) {
				r = 1;
				Stmt_setMethodFunc(_ctx, stmt, ks, mtd);
				kStmt_done(stmt);
			}
			RESET_GCSTACK();
		}
		RETURNb_(r);
	}*/
}

class BOOLEANSyntax extends Syntax {
	public BOOLEANSyntax () {
		super("boolean");
		this.ty = KClass.booleanClass;
	}
}

// can't name INTSyntax, why??
class INTTypeSyntax extends Syntax {
	public INTTypeSyntax () {
		super("int");
		this.ty = KClass.intClass;
	}
}

class TRUESyntax extends TermSyntax {
	public TRUESyntax () {
		super("true");
	}

	@Override
	public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		return new ConstExpr(this, KClass.booleanClass, KBoolean.box(true));
	}
}

class FALSESyntax extends TermSyntax {
	public FALSESyntax () {
		super("false");
	}
	@Override
	public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {
		return new ConstExpr(this, KClass.booleanClass, KBoolean.box(false));
	}
}

class IFSyntax extends Syntax {
	public IFSyntax() {
		super("if");
		this.rule = "\"if\" \"(\" $expr \")\" $block [\"else\" else: $block]";
	}

	@Override
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		boolean r = true;
		if((r = stmt.tyCheckExpr(ctx, KW.Expr, gamma, KClass.booleanClass, 0))) {
			Block bkThen = stmt.block(ctx, KW.Block, null);
			Block bkElse = stmt.block(ctx, KW._else, null);
			r = bkThen.tyCheckAll(ctx, gamma);
			if(bkElse != null) r = r & bkElse.tyCheckAll(ctx, gamma);
			stmt.typed(TSTMT.IF);
		}
		return r;
	}
}

class ELSESyntax extends Syntax {
	public ELSESyntax () {
		super("else");
		this.rule = "\"else\" $block";
	}

	@Override
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		boolean r = true;
		Stmt stmtIf = stmt.lookupIfStmtNULL(ctx);
		if(stmtIf != null) {
			Block bkElse = stmt.block(ctx, KW.Block, null);
			stmtIf.setObject(KW._else, bkElse);
			r = bkElse.tyCheckAll(ctx, gamma);
		}
		else {
			ctx.SUGAR_P(System.err, stmt.uline, -1, "else is not statement");
			r = false;
		}
		return r;
	}
}

class RETURNSyntax extends Syntax {
	public RETURNSyntax() {
		super("return");
		this.rule = "\"return\" [$expr]";
		this.flag = SYNFLAG.StmtBreakExec;
	}

	@Override
	public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
		stmt.typed(TSTMT.RETURN);
		Object o = stmt.getObject(KW.Expr);
		if(o != null && o instanceof Expr) {
			stmt.tyCheckExpr(ctx, KW.Expr, gamma, KClass.varClass, 0);
		}
		return true;
	}
}
