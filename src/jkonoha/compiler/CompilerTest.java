package jkonoha.compiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import jkonoha.*;
import jkonoha.compiler.kobject.*;

//public class CompilerTest {
//	
//	private static void createFibCode(CompilerContext com, KonohaClass klass) {
//		// generate method : int fib(int x)
//		KonohaMethod fib = new KonohaMethod(klass, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "fib", Type.INT_TYPE, 
//				new String[]{ "x" }, new Type[]{ Type.INT_TYPE });
//		klass.addMethod(fib);
//		int arg0index = 0;
//		
//		// if (arg0 < 3)
//		Stmt ifStmt = new Stmt(TSTMT.IF);
//		{
//			Expr e1 = new Expr(TY.INT, TEXPR.LOCAL);
//			e1.data = arg0index;
//			Expr e2 = new Expr(TY.INT, TEXPR.CONST);
//			e2.data = 3;
//			Expr cond = new Expr(TY.BOOLEAN, TEXPR.CALL);
//			ArrayList<Object> l = new ArrayList<Object>();
//			l.add(KMethod.getMethod(KInt.class, "opLT"));
//			l.add(e1);
//			l.add(e2);
//			cond.data = l;
//			ifStmt.setObject(1, cond);
//		}
//		
//		// then block:
//		{
//			// return 1;
//			Expr e = new Expr(TY.INT, TEXPR.CONST);
//			e.data = 1;
//			Stmt stmt = new Stmt(TSTMT.RETURN);
//			stmt.setObject(1, e);
//			Block bk = new Block();
//			bk.blocks.add(stmt);
//			ifStmt.setObject(KW.Block, bk);
//		}
//		// else block:
//		{
//			// fib(arg0-1)
//			Expr fibm1;
//			{
//				Expr e1 = new Expr(TY.INT, TEXPR.LOCAL);
//				e1.data = arg0index;
//				Expr e2 = new Expr(TY.INT, TEXPR.CONST);
//				e2.data = 1;
//				Expr em = new Expr(TY.INT, TEXPR.CALL);
//				ArrayList<Object> l = new ArrayList<Object>();
//				l.add(KMethod.getMethod(KInt.class, "opSUB"));
//				l.add(e1);
//				l.add(e2);
//				em.data = l;
//				fibm1 = new Expr(TY.INT, TEXPR.CALL);
//				ArrayList<Object> l2 = new ArrayList<Object>();
//				l2.add(fib);
//				l2.add(em);
//				fibm1.data = l2;
//			}
//			// fib(arg0-2)
//			Expr fibm2;
//			{
//				Expr e1 = new Expr(TY.INT, TEXPR.LOCAL);
//				e1.data = arg0index;
//				Expr e2 = new Expr(TY.INT, TEXPR.CONST);
//				e2.data = 2;
//				Expr em = new Expr(TY.INT, TEXPR.CALL);
//				ArrayList<Object> l = new ArrayList<Object>();
//				l.add(KMethod.getMethod(KInt.class, "opSUB"));
//				l.add(e1);
//				l.add(e2);
//				em.data = l;
//				fibm2 = new Expr(TY.INT, TEXPR.CALL);
//				ArrayList<Object> l2 = new ArrayList<Object>();
//				l2.add(fib);
//				l2.add(em);
//				fibm2.data = l2;
//			}
//			Expr fibp = new Expr(TY.INT, TEXPR.CALL);
//			ArrayList<Object> l = new ArrayList<Object>();
//			l.add(KMethod.getMethod(KInt.class, "opADD"));
//			l.add(fibm1);
//			l.add(fibm2);
//			fibp.data = l;
//			
//			Stmt stmt = new Stmt(TSTMT.RETURN);
//			stmt.setObject(1, fibp);
//			
//			Block bk = new Block();
//			bk.blocks.add(stmt);
//			
//			ifStmt.setObject(KW._else, bk);
//		}
//		
//		Block bk = new Block();
//		bk.blocks.add(ifStmt);
//		
//		com.evalBlock(fib, bk);
//	}
//	
//	public static void main(String[] args) throws Exception {
//		CTX ctx = new CTX();
//		CompilerContext com = new CompilerContext(ctx);
//		
//		// class Script
//		KonohaClass k = new KonohaClass("Script", new JavaClass(Object.class), null);
//		com.addClass(k);
//		createFibCode(com, k);
//
//		// write class file
//		com.writeClassFile(".");
//		
//		// exec
//		ClassLoader c = com.createClassLoader();
//		try {
//			Class<?> scriptClass = c.loadClass("Script");
//			Method m = scriptClass.getMethod("fib", new Class<?>[]{ int.class });
//			
//			int r = (Integer)m.invoke(null, 40);
//			System.out.println("result = " + r);
//		} catch(InvocationTargetException e) {
//			System.out.println("Error in Script:");
//			e.getTargetException().printStackTrace();
//		}
//	}
//}
