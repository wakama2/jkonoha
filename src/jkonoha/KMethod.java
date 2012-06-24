package jkonoha;

import java.lang.reflect.Method;
import java.util.*;

import jkonoha.compiler.CodeGenException;
import jkonoha.compiler.JavaMethod;

import org.objectweb.asm.*;

public abstract class KMethod extends KObject {
	private Type methodType = null;
	private Type[] argTypes = null;
	private Type retType = null;
	
	public abstract KClass getParent();
	public abstract String getName();
	public abstract KClass[] getArgClasses();
	public abstract KClass getReturnClass();
	public abstract boolean isStatic();
	public abstract int getCallIns();
	
	public Type[] getArgTypes() {
		if(argTypes == null) {
			KClass[] args = getArgClasses();
			argTypes = new Type[args.length];
			for(int i=0; i < args.length; i++) {
				argTypes[i] = args[i].getAsmType();
			}
		}
		return argTypes;
	}
	
	public Type getReturnType() {
		if(retType == null) {
			retType = getReturnClass().getAsmType();
		}
		return retType;
	}
	
	public Type getMethodType() {
		if(methodType == null) {
			methodType = Type.getMethodType(getReturnType(), getArgTypes());
		}
		return methodType;
	}
	
	//private static final Map<String, JavaMethod> jmtdCache;
	
	// get a method
	public static JavaMethod getMethod(Class<?> klass, String name) {
		List<JavaMethod> m = getMethods(klass, name);
		if(m.size() == 1) {
			return m.get(0);
		} else {
			throw new CodeGenException(m.size() + " methods found: " + klass.getName() + "." + name);
		}
	}
	
	public static JavaMethod getMethod(Class<?> klass, String name, Class<?>[] params) {
		try {
			Method m = klass.getMethod(name, params);
			return new JavaMethod(m);
		} catch(NoSuchMethodException e) {
			throw new CodeGenException("method not found: " + klass.getName() + "." + name);
		}
	}
	
	// get overloaded methods
	public static List<JavaMethod> getMethods(Class<?> klass, String name) {
		List<JavaMethod> mtds = new ArrayList<JavaMethod>();
		for(Method m : klass.getMethods()) {
			if(m.getName().equals(name)) {
				mtds.add(new JavaMethod(m));
			}
		}
		return mtds;
	}
}

