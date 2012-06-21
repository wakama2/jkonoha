package jkonoha.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import jkonoha.KClass;
import jkonoha.KMethod;

public class JavaMethod extends KMethod {
	
	private final Method method;
	private JavaClass parent = null;
	private KClass[] argTypes = null;
	private KClass retType = null;
	
	public JavaMethod(Method m) {
		this.method = m;
	}
	
	@Override public KClass getParent() {
		if(parent == null) {
			parent = new JavaClass(method.getDeclaringClass());
		}
		return parent;
	}
	
	@Override public String getName() {
		return method.getName();
	}
	
	@Override public KClass[] getArgClasses() {
		if(argTypes == null) {
			Class<?>[] args = method.getParameterTypes();
			argTypes = new KClass[args.length];
			for(int i=0; i < args.length; i++) {
				argTypes[i] = new JavaClass(args[i]);
			}
		}
		return argTypes;
	}
	
	@Override public KClass getReturnClass() {
		if(retType == null) {
			retType = new JavaClass(method.getReturnType());
		}
		return retType;
	}
	
	@Override public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}

}