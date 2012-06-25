package jkonoha.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.Opcodes;

import jkonoha.KClass;
import jkonoha.KMethod;

public class JavaMethod extends KMethod {
	
	private final Method method;
	private KClass parent = null;
	private KClass[] argTypes = null;
	private KClass retType = null;
	
	public JavaMethod(Method m) {
		this.method = m;
	}
	
	@Override public KClass getParent() {
		if(parent == null) {
			parent = JavaClass.create(method.getDeclaringClass());
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
				argTypes[i] = JavaClass.create(args[i]);
			}
		}
		return argTypes;
	}
	
	@Override public KClass getReturnClass() {
		if(retType == null) {
			retType = JavaClass.create(method.getReturnType());
		}
		return retType;
	}
	
	@Override public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}
	
	@Override public int getCallIns() {
		if(isStatic()) return Opcodes.INVOKESTATIC;
		if(Modifier.isInterface(method.getDeclaringClass().getModifiers())) return Opcodes.INVOKEINTERFACE;
		return Opcodes.INVOKEVIRTUAL;
	}

}