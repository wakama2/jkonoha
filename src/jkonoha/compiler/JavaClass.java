package jkonoha.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import jkonoha.KClass;
import jkonoha.KMethod;
import jkonoha.KObject;

import org.objectweb.asm.Type;

public class JavaClass extends KClass {
	
	private final Class<?> klass;
	private KClass superClass = null;
	private KClass[] interfaces = null;
	
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
		} else if(c == String.class) {
			return KClass.stringClass;
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
			superClass = JavaClass.create(klass.getSuperclass());
		}
		return superClass;
	}
	
	@Override public KClass[] getInterfaces() {
		if(interfaces == null) {
			Class<?>[] is = klass.getInterfaces();
			interfaces = new JavaClass[is.length];
			for(int i=0; i<is.length; i++) {
				interfaces[i] = JavaClass.create(is[i]);
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
	
	@Override public KMethod getMethod(String name, List<KClass> args) {
		for(Method m : klass.getMethods()) {
			Class<?>[] a = m.getParameterTypes();
			if(m.getName().equals(name) && a.length == args.size()) {
				int i;
				for(i=0; i<a.length; i++) {
					if(a[i] == KObject.class) continue;
					KClass p = JavaClass.create(a[i]);
					KClass p1 = args.get(i);
					if(!p1.isa(p)) {
						break;
					}
				}
				if(i == a.length) return new JavaMethod(m);
			}
		}
		if(superClass != null) {
			return superClass.getMethod(name, args);
		}
		return null;
	}
	
	@Override public KMethod getGetter(String fn) {
		throw new RuntimeException("not impl");
	}
	
	@Override public KMethod getSetter(String fn) {
		throw new RuntimeException("not impl");
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