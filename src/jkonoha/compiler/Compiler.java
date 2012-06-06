package jkonoha.compiler;

import java.util.*;

import org.objectweb.asm.*;

import jkonoha.*;

public class Compiler implements Opcodes {
	
	private final CTX ctx;
	private final CompilerContext cctx;
	private final KonohaMethod mtd;
	private final MethodVisitor mv;
	private int localCount = 0;
	private final Map<String, Local> locals = new HashMap<String, Local>();
	private final Stack<Type> typeStack = new Stack<Type>();
	
	// ctxcode
	private long uline;
	//private final List<Object> constPools = null;
	private Label curBB = null;
	
	private static class Local {
		public final int index;
		public final Type type;
		public Local(int index, Type type) {
			this.index = index;
			this.type = type;
		}
	}
	
	public Compiler(CTX ctx, CompilerContext com, KonohaMethod mtd) {
		this.ctx = ctx;
		this.cctx = com;
		this.mtd = mtd;
		this.mv = mtd.getNode();
		
		String[] argNames = mtd.getArgNames();
		Type[] argTypes = mtd.getArgTypes();
		//addLocal("this", Type.VOID_TYPE); // TODO this
		for(int i=0; i<argNames.length; i++) {
			//addLocal(argNames[i], argTypes[i]);
			addLocal("local_" + i, argTypes[i]);
		}
	}
	
	private Local addLocal(String name, Type type) {
		if(locals.containsKey(name)) {
			throw new CodeGenException("duplicate local variable: " + name);
		}
		int size = type.getSize();
		//System.out.println(type + " size = " + size);
		int n = localCount;
		localCount += size;
		Local l = new Local(n, type);
		locals.put(name, new Local(n, type));
		return l;
	}
	
	private void loadLocal(String name) {
		if(!locals.containsKey(name)) {
			throw new CodeGenException("local variable not found: " + name);
		}
		Local l = locals.get(name);
		Type type = l.type;
		mv.visitVarInsn(type.getOpcode(ILOAD), l.index);
		typeStack.push(type);
	}
	
	private void storeLocal(String name) {
		Type type = typeStack.pop();
		Local l = locals.get(name);
		if(l == null) {
			l = addLocal(name, type);
		} else {
			if(l.type != type) {
				throw new CodeGenException("different type: " + name);
			}
		}
		mv.visitVarInsn(type.getOpcode(ISTORE), l.index);
	}
	
	private void loadConst(Object o) {
		mv.visitLdcInsn(o);
		typeStack.push(Type.getType(o.getClass()));
	}
	
	private void box() {
		Type type = typeStack.pop();
		if(type == Type.INT_TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "konoha/K_Int", "box", "(Lkonoha/K_Int;)I");
			typeStack.push(Type.getType("konoha/K_Int"));
		} else if(type == Type.DOUBLE_TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "konoha/K_Float", "box", "(Lkonoha/K_Float;)I");
			typeStack.push(Type.getType("konoha/K_Float"));
		} else if(type == Type.BOOLEAN_TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "konoha/K_Boolean", "box", "(Lkonoha/K_Boolean;)I");
			typeStack.push(Type.getType("konoha/K_Boolean"));
		} else {
			typeStack.push(type);
		}
	}
	
	private void unbox() {
		Type type = typeStack.pop();
		if(type == Type.INT_TYPE) {//TODO
			mv.visitTypeInsn(CHECKCAST, "konoha/K_Int");
			mv.visitMethodInsn(INVOKESTATIC, "konoha/K_Int", "unbox", "(Lkonoha/K_Int;)I");
		} else if(type == Type.DOUBLE_TYPE) {
			mv.visitTypeInsn(CHECKCAST, "konoha/K_Float");
			mv.visitMethodInsn(INVOKESTATIC, "konoha/K_Float", "unbox", "(Lkonoha/K_Float;)D");
		} else if(type == Type.BOOLEAN_TYPE) {
			mv.visitTypeInsn(CHECKCAST, "konoha/K_Boolean");
			mv.visitMethodInsn(INVOKESTATIC, "konoha/K_Boolean", "unbox", "(Lkonoha/K_Boolean;)Z");
		} else {
			typeStack.push(type);
		}
	}
	
	private void call(KMethod method) {
		String klassName = method.getParent().getName().replace(".", "/");
		String type = method.getMethodType().getDescriptor();
		int inst = method.isStatic() ? INVOKESTATIC : INVOKEVIRTUAL;
		mv.visitMethodInsn(inst, klassName, method.getName(), type);
	}
	
	private void asmJump(Label lb) {
		mv.visitJumpInsn(GOTO, lb);
	}
	
	private void asmLabel(Label lb) {
		curBB = lb;
		mv.visitLabel(lb);
	}

	public void asmErrStmt(Stmt stmt, int shift, int espidx) {
		String str = (String)stmt.getObject("$ERR");
		System.err.println(str);
		//asmError();
	}
	
	public void asmExprStmt(Stmt stmt, int shift, int espidx) {
		Expr expr = (Expr)stmt.getObject("$expr");
		asmExpr(espidx, expr, shift, espidx);
	}
	
	public void asmBlockStmt(Stmt stmt, int shift, int espidx) {
		asmBlock(stmt.getBlock(ctx, KW.Block, ctx.NULLBLOCK), shift);
	}
	
	public void asmReturnStmt(Stmt stmt, int shift, int espidx) {
		Object o = stmt.getObject("$expr");
		if(o != null && o instanceof Expr) {
			Expr expr = (Expr)o;
			if(expr.ty != TY.VOID) {
				asmExpr(K.RTNIDX, expr, shift, espidx);
			}
		}
		//asmJump(lbEND);
		if(mtd.getReturnType().equals(Type.VOID_TYPE)) {
			mv.visitInsn(RETURN);
		} else {
			mv.visitInsn(mtd.getReturnType().getOpcode(IRETURN));
		}
	}
	
	private void asmExprJmpIf(int a, Expr expr, boolean isTrue, Label label, int shift, int espidx) {
		asmExpr(a, expr, shift, espidx);
		if(isTrue) {
			mv.visitJumpInsn(Opcodes.IFNE, label);//POP() != 0 -> jump
		} else {
			mv.visitJumpInsn(Opcodes.IFEQ, label);//POP() == 0 -> jump
		}
	}
	
	public void asmIfStmt(Stmt stmt, int shift, int espidx) {
		Label lbELSE = new Label();
		Label lbEND  = new Label();
		/* if */
		asmExprJmpIf(espidx, (Expr)stmt.getObject("$expr"), false, lbELSE, shift, espidx);
		/* then */
		asmBlock(stmt.getBlock(ctx, KW.Block, ctx.NULLBLOCK), shift);
		asmJump(lbEND);
		/* else */
		asmLabel(lbELSE);
		asmBlock(stmt.getBlock(ctx, KW._else, ctx.NULLBLOCK), shift);
		/* endif */
		asmLabel(lbEND);
	}
	
	public void asmLoopStmt(Stmt stmt, int shift, int espidx) {
		Label lbCONTINUE = new Label();
		Label lbBREAK = new Label();
		stmt.setObject("continue", lbCONTINUE);
		stmt.setObject("break", lbBREAK);
		asmLabel(lbCONTINUE);
		asmExprJmpIf(espidx, (Expr)stmt.getObject("$expr"), false, lbBREAK, shift, espidx);
		asmBlock((Block)stmt.getObject(KW.Block), shift);
		asmJump(lbCONTINUE);
		asmLabel(lbBREAK);
	}
	
	public void asmJumpStmt(Stmt stmt, int shift, int espidx) {
		Syntax syn = stmt.syntax;
		Stmt jump = (Stmt)stmt.getObject(syn.kw);
		assert jump != null;
		Label lbJUMP = (Label)jump.getObject(syn.kw);
		assert lbJUMP != null;
		asmJump(lbJUMP);
	}
	
	public void asmUndefinedStmt(Stmt stmt, int shift, int espidx) {
		throw new CodeGenException("undefined asm syntax kw='" + stmt.syntax.kw + "'");
	}
	
	private void asmCall(int a, Expr expr, int shift, int espidx) {
		List<Object> l = expr.cons;
		KMethod mtd = (KMethod)l.get(0);
		//int s = mtd.isStatic() ? 2 : 1;//TODO
		int s = 1;
		int thisidx = espidx + K.CALLDELTA;
		for(int i=s; i<l.size(); i++) {
			Expr e = (Expr)l.get(i);
			asmExpr(thisidx + i - 1, e, shift, thisidx + i - 1);
		}
		call(mtd);
	}
	
	public void asmLetExpr(int a, Expr expr, int shift, int espidx) {
		Expr exprL = expr.at(1);
		Expr exprR = expr.at(2);
		if(exprL.build == TEXPR.LOCAL) {
			asmExpr(exprL.index, exprR, shift, espidx);
			if(a != espidx) {
				Type type = typeStack.peek();
				String name = "local_" + exprL.index;
				addLocal(name, type);
				storeLocal(name);
			}
		} else if(exprL.build == TEXPR.STACKTOP) {
			//TODO
		} else {
			//TODO
		}
	}
	
	public void asmOr(int a, Expr expr, int shift, int espidx) {
		List<Object> l = expr.cons;
		Label lbTRUE = new Label();
		Label lbFALSE = new Label();
		for(int i=1; i<l.size(); i++) {
			this.asmExprJmpIf(a, (Expr)l.get(i), true, lbTRUE, shift, espidx);
		}
		mv.visitInsn(ICONST_0);//false
		asmJump(lbFALSE);
		asmLabel(lbTRUE);
		mv.visitInsn(ICONST_1);//true
		asmLabel(lbFALSE);
	}
	
	public void asmAnd(int a, Expr expr, int shift, int espidx) {
		List<Object> l = expr.cons;
		Label lbTRUE = new Label();
		Label lbFALSE = new Label();
		for(int i=1; i<l.size(); i++) {
			this.asmExprJmpIf(a, (Expr)l.get(i), false, lbTRUE, shift, espidx);
		}
		mv.visitInsn(ICONST_0);//false
		asmJump(lbFALSE);
		asmLabel(lbTRUE);
		mv.visitInsn(ICONST_1);//true
		asmLabel(lbFALSE);
	}
	
	public void asmExpr(int a, Expr expr, int shift, int espidx) {
		switch(expr.build) {
		case TEXPR.CONST:
		case TEXPR.NCONST:
			loadConst(expr.data);
			break;
		case TEXPR.NEW:
			//TODO
			break;
		case TEXPR.NULL:
			mv.visitInsn(ACONST_NULL);
			typeStack.push(Type.getType(Object.class));//TODO
			break;
		case TEXPR.LOCAL:
			loadLocal("local_" + expr.index);
			break;
		case TEXPR.BLOCK:
			asmBlock((Block)expr.data, espidx);
			//NMOV
			break;
		case TEXPR.FIELD:
			//TODO
			break;
		case TEXPR.BOX:
			//TODO
			break;
		case TEXPR.UNBOX:
			//TODO
			break;
		case TEXPR.CALL:
			asmCall(a, expr, shift, espidx);
			break;
		case TEXPR.AND:
			asmAnd(a, expr, shift, espidx);
			break;
		case TEXPR.OR:
			asmOr(a, expr, shift, espidx);
			break;
		case TEXPR.LET:
			asmLetExpr(a, expr, shift, espidx);
			break;
		case TEXPR.STACKTOP:
			//TODO
			break;
		default:
			System.out.println("ERROR");	
		}
	}
	
	public void asmBlock(Block bk, int shift) {
		int espidx = 0; //TODO
		for(Stmt stmt : bk.blocks) {
			//if(stmt.syntax == null) continue;
			this.uline = stmt.uline;
			switch(stmt.build) {
			case TSTMT.ERR: asmErrStmt(stmt, shift, espidx); return;
			case TSTMT.EXPR: asmExprStmt(stmt, shift, espidx); break;
			case TSTMT.BLOCK: asmBlockStmt(stmt, shift, espidx); break;
			case TSTMT.RETURN: asmReturnStmt(stmt, shift, espidx); break;
			case TSTMT.IF: asmIfStmt(stmt, shift, espidx); break;
			case TSTMT.LOOP: asmLoopStmt(stmt, shift, espidx); break;
			case TSTMT.JUMP: asmJumpStmt(stmt, shift, espidx); break;
			default: asmUndefinedStmt(stmt, shift, espidx); break;
			}
 		}
	}
	
	public void close() {
		//mv.visitLabel(lbEND);
		mv.visitEnd();
	}
	
}

