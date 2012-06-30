package jkonoha.compiler;

import java.util.*;

import org.objectweb.asm.*;

import jkonoha.*;
import jkonoha.ast.*;

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
		if(!mtd.isStatic()) {
			addLocal("this", mtd.getParent().getAsmType());
		}
		for(int i=0; i<argNames.length; i++) {
			addLocal(argNames[i], argTypes[i]);
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
		//typeStack.push(Type.getType(o.getClass()));
	}
	
	private void box() {
		Type type = typeStack.pop();
		if(type == Type.INT_TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KInt", "box", "(I)Ljkonoha/KInt;");
			typeStack.push(Type.getType("jkonoha/KInt"));
		} else if(type == Type.DOUBLE_TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KFloat", "box", "(D)Ljkonoha/KFloat;");
			typeStack.push(Type.getType("jkonoha/KFloat"));
		} else if(type == Type.BOOLEAN_TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KBoolean", "box", "(Z)Ljkonoha/KBoolean;");
			typeStack.push(Type.getType("jkonoha/KBoolean"));
		} else if(type.equals(Type.getType(String.class))) {
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KString", "box", "(Ljava/lang/String;)Ljkonoha/KString;");
			typeStack.push(Type.getType("jkonoha/KString"));
		} else {
			typeStack.push(type);
		}
	}
	
	private void unbox() {
		Type type = typeStack.pop();
		if(type.equals(KClass.intClass)) {
			mv.visitTypeInsn(CHECKCAST, "jkonoha/KInt");
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KInt", "unbox", "(Ljkonoha/KInt;)I");
			typeStack.push(Type.INT_TYPE);
		} else if(type.equals(KClass.floatClass)) {
			mv.visitTypeInsn(CHECKCAST, "jkonoha/KFloat");
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KFloat", "unbox", "(Ljkonoha/KFloat;)D");
			typeStack.push(Type.DOUBLE_TYPE);
		} else if(type.equals(KClass.booleanClass)) {
			mv.visitTypeInsn(CHECKCAST, "jkonoha/KBoolean");
			mv.visitMethodInsn(INVOKESTATIC, "jkonoha/KBoolean", "unbox", "(Ljkonoha/KBoolean;)Z");
			typeStack.push(Type.BOOLEAN_TYPE);
		} else {
			typeStack.push(type);
		}
	}
	
	private void call(KMethod method) {
		String klassName = method.getParent().getName().replace(".", "/");
		String type = method.getMethodType().getDescriptor();
		int inst = method.getCallIns();
		mv.visitMethodInsn(inst, klassName, method.getName(), type);
		Type ret = method.getReturnType();
		if(ret != Type.VOID_TYPE) {
			typeStack.push(method.getReturnType());
		}
	}
	
	private void asmJump(Label lb) {
		mv.visitJumpInsn(GOTO, lb);
	}
	
	private void asmLabel(Label lb) {
		curBB = lb;
		mv.visitLabel(lb);
	}

	public void asmErrStmt(Stmt stmt, int shift, int espidx) {
		String str = (String)stmt.getObject(KW.Err);
		System.err.println(str);
		//asmError();
	}
	
	public void asmExprStmt(Stmt stmt, int shift, int espidx) {
		Expr expr = (Expr)stmt.getObject(KW.Expr);
		asmExpr(espidx, expr, shift, espidx);
	}
	
	public void asmBlockStmt(Stmt stmt, int shift, int espidx) {
		asmBlock((Block)stmt.getObject(KW.Block), shift);
	}
	
	public void asmReturnStmt(Stmt stmt, int shift, int espidx) {
		Object o = stmt.getObject(KW.Expr);
		if(o != null && o instanceof Expr) {
			Expr expr = (Expr)o;
			if(expr.ty != KClass.voidClass) {
				asmExpr(0, expr, shift, espidx);
			}
		}
		//asmJump(lbEND);
		if(mtd.getReturnType().equals(Type.VOID_TYPE)) {
			mv.visitInsn(RETURN);
		} else {
			Type ty = typeStack.pop();
			mv.visitInsn(mtd.getReturnType().getOpcode(IRETURN));
		}
	}
	
	private void asmExprJmpIf(int a, Expr expr, boolean isTrue, Label label, int shift, int espidx) {
		asmExpr(a, expr, shift, espidx);
		Type ty = typeStack.pop();
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
		asmBlock((Block)stmt.getObject(KW.Block), shift);
		asmJump(lbEND);
		/* else */
		asmLabel(lbELSE);
		asmBlock((Block)stmt.getObject(KW._else), shift);
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
		Type[] argTypes = mtd.getArgTypes();
		int thisidx = 0;
		if(!mtd.isStatic() || (l.size() >= 2 && !(((Expr)l.get(1)).build == -1))) {
			asmExpr(0, (Expr)l.get(1), shift, 0);
			typeStack.pop();
		}
		for(int i=2; i<l.size(); i++) {
			Expr e = (Expr)l.get(i);
			asmExpr(thisidx + i - 1, e, shift, thisidx + i - 1);
			if(argTypes[i-2].equals(KClass.objectClass.getAsmType())) {
				box();
			}
			typeStack.pop();
		}
		call(mtd);
	}
	
	public void asmLetExpr(int a, Expr expr, int shift, int espidx) {
		Expr exprL = expr.at(1);
		Expr exprR = expr.at(2);
		if(exprL.build == TEXPR.LOCAL) {
			asmExpr(exprL.index, exprR, shift, espidx);
			Type type = typeStack.peek();
			String name = exprL.tk.text;
			addLocal(name, type);
			storeLocal(name);
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
			if(expr.data instanceof KInt) {
				KInt i = (KInt)expr.data;
				loadConst(i.unbox());
				typeStack.push(Type.INT_TYPE);
			} else if(expr.data instanceof KBoolean) {
				KBoolean i = (KBoolean)expr.data;
				loadConst(i.unbox());
				typeStack.push(Type.BOOLEAN_TYPE);
			} else if(expr.data instanceof KString) {
				KString s = (KString)expr.data;
				loadConst(s.unbox());
				typeStack.push(Type.getType(String.class));
			} else {
				throw new CodeGenException("err const");
			}
			break;
		case TEXPR.NEW: {
			KClass k = expr.at(1).ty;
			mv.visitTypeInsn(NEW, k.getName().replace(".", "/"));
			mv.visitInsn(DUP);
			typeStack.push(k.getAsmType());
			int size = expr.cons.size();
			for(int i=2; i<size; i++) {
				asmExpr(0, expr.at(i), 0, 0);
				typeStack.pop();
			}
			call((KMethod)expr.cons.get(0));
			break;
		}
		case TEXPR.NULL:
			mv.visitInsn(ACONST_NULL);
			typeStack.push(Type.getType(Object.class));//TODO
			break;
		case TEXPR.LOCAL:
			loadLocal((String)expr.ndata);
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
			throw new CodeGenException("error");
		}
	}
	
	public void asmBlock(Block bk, int shift) {
		if(bk == null) return;
		int espidx = 0; //TODO
		for(Stmt stmt : bk.blocks) {
			//if(stmt.syntax.kw.equals(KW._void)) continue;
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
		if(!typeStack.isEmpty()) {
			box();
		} else {
			if(mtd.getReturnType() == Type.INT_TYPE) {
				mv.visitInsn(Opcodes.ICONST_0);
			} else {
				mv.visitInsn(Opcodes.ACONST_NULL);
			}
		}
		mv.visitInsn(mtd.getReturnType().getOpcode(IRETURN));
		mv.visitEnd();
	}
	
}

