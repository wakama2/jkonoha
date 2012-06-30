package jkonoha.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import jkonoha.KClass;
import jkonoha.KMethod;

import org.objectweb.asm.Type;

public class JavaClass extends KClass {
	
	private final Class<?> klass;
	private JavaClass superClass = null;
	private JavaClass[] interfaces = null;
	
	private JavaClass(Class<?> klass) {
		this.klass = klass;
	}
	
	public static KClass create(Class<?> c) {
		if(c == int.class) {
			return KClass.intClass;
		} else if(c == double.class) {
			return KClass.floatClass;
		} else if(c == boolean.class) {
			return KClass.booleanClass;
		} else {
			return new JavaClass(c);
		}
	}
	
	@Override public String getName() {
		return klass.getName();
	}

	@Override public Type getAsmType() {
		return Type.getType(klass);
	}

	@Override public KClass getSuperClass() {
		if(superClass == null && klass != Object.class) {
			superClass = new JavaClass(klass.getSuperclass());
		}
		return superClass;
	}
	
	@Override public KClass[] getInterfaces() {
		if(interfaces == null) {
			Class<?>[] is = klass.getInterfaces();
			interfaces = new JavaClass[is.length];
			for(int i=0; i<is.length; i++) {
				interfaces[i] = new JavaClass(is[i]);
			}
		}
		return interfaces;
	}
	
	@Override public KMethod getConstructor(List<KClass> args) {
		for(Constructor<?> c : klass.getConstructors()) {
			Class<?>[] a = c.getParameterTypes();
			if(a.length == args.size()) {
				//TODO arg typecheck
				return new JavaConstructor(c);
			}
		}
		return null;
	}
	
	@Override public JavaMethod getMethod(String name, List<KClass> args) {
		for(Method m : klass.getMethods()) {
			Class<?>[] a = m.getParameterTypes();
			if(m.getName().equals(name) && a.length == args.size()) {
				//TODO arg typecheck
				return new JavaMethod(m);
			}
		}
		if(superClass != null) {
			return superClass.getMethod(name, args);
		}
		return null;
	}
	
	@Override public String toString() {
		return klass.getName();
	}
	
	@Override public boolean equals(Object o) {
		if(o instanceof JavaClass) {
			return klass.equals(((JavaClass)o).klass);
		} else {
			return false;
		}
	}
	
}