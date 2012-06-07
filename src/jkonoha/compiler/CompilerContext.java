package jkonoha.compiler;

import java.io.*;
import java.util.*;

import jkonoha.CTX;
import jkonoha.Block;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class CompilerContext {
	
	private final CTX ctx;
	private final Map<String, KonohaClass> classMap = new HashMap<String, KonohaClass>();
	
	private class KClassLoader extends ClassLoader {
		@Override public Class<?> findClass(String name) {
			KonohaClass klass = classMap.get(name);
			byte[] b = genBytecode(klass);
			return defineClass(name, b, 0, b.length);
		}
	}
	
	public CompilerContext(CTX ctx) {
		this.ctx = ctx;
	}
	
	private byte[] genBytecode(KonohaClass klass) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		klass.accept(cw);
		return cw.toByteArray();
	}
	
	// tycheck.h 1335
	public void evalBlock(Block b) {
		// new_kMethod && kMethod_setParam
		KonohaMethod mtd = new KonohaMethod(new JavaClass(Object.class), 
				Opcodes.ACC_STATIC, "", Type.VOID_TYPE, null, null);
		try {
			evalBlock(mtd, b);
		} catch(Exception e) {
			System.err.println("Catch eval exception");
			e.printStackTrace();
		}
	}
	
	public void evalBlock(KonohaMethod mtd, Block b) {
		Compiler cg = new Compiler(ctx, this, mtd);
		cg.asmBlock(b, 0);
		cg.close();	
	}
	
	public void addClass(KonohaClass k) {
		classMap.put(k.getName(), k);
	}
	
	public KonohaClass getClass(String name) {
		if(classMap.containsKey(name)) {
			return classMap.get(name);
		} else {
			throw new CodeGenException("class not found: " + name);
		}
	}
	
	public ClassLoader createClassLoader() {
		return new KClassLoader();
	}
	
	public void writeClassFile(String dir) throws IOException {
		for(KonohaClass klass : classMap.values()) {
			byte[] bc = genBytecode(klass);
			File file = new File(dir, klass.getName() + ".class");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				fos.write(bc);
			} finally {
				if(fos != null) fos.close();
			}
		}
	}
	
}
