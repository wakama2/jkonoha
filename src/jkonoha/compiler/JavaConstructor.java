package jkonoha.compiler;

import java.lang.reflect.Constructor;

import org.objectweb.asm.Opcodes;

import jkonoha.*;

public class JavaConstructor extends KMethod {
	
	private final Constructor<?> method;
	private KClass[] argTypes = null;
	
	public JavaConstructor(Constructor<?> method) {
		this.method = method;
	}

	@Override
	public KClass getParent() {
		return JavaClass.create(method.getDeclaringClass());
	}

	@Override
	public String getName() {
		return "<init>";
	}

	@Override
	public KClass[] getArgClasses() {
		if(argTypes == null) {
			Class<?>[] args = method.getParameterTypes();
			argTypes = new KClass[args.length];
			for(int i=0; i < args.length; i++) {
				argTypes[i] = JavaClass.create(args[i]);
			}
		}	
		return argTypes;
	}

	@Override
	public KClass getReturnClass() {
		return KClass.voidClass;
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public int getCallIns() {
		return Opcodes.INVOKESPECIAL;
	}

}
