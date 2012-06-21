package jkonoha.compiler;

import org.objectweb.asm.Type;

import jkonoha.KClass;
import jkonoha.KMethod;

public class PrimitiveClass extends KClass {
	
	private final Class<?> klass;
	private final KClass boxedClass;
	
	public PrimitiveClass(Class<?> klass, KClass boxedClass) {
		this.klass = klass;
		this.boxedClass = boxedClass;
	}

	@Override
	public String getName() {
		return klass.getName();
	}

	@Override
	public Type getAsmType() {
		return Type.getType(klass);
	}

	@Override
	public KClass getSuperClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KClass[] getInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KMethod getMethod(String name, KClass reqty) {
		return boxedClass.getMethod(name, reqty);
	}

}
