package jkonoha.compiler;

import java.lang.reflect.Method;

import org.objectweb.asm.Type;

public class JavaClass extends KClass {
	
	private final Class<?> klass;
	private JavaClass superClass = null;
	private JavaClass[] interfaces = null;
	
	public JavaClass(Class<?> klass) {
		this.klass = klass;
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
	
	@Override public JavaMethod getOneMethod(String name) {
		for(Method m : klass.getMethods()) {
			if(m.getName().equals(name)) {
				return new JavaMethod(m);
			}
		}
		if(superClass != null) {
			return superClass.getOneMethod(name);
		}
		return null;
	}
	
	@Override public boolean equals(Object o) {
		if(o instanceof JavaClass) {
			return klass.equals(((JavaClass)o).klass);
		} else {
			return false;
		}
	}
	
}