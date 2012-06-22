package jkonoha;

import java.util.List;

import jkonoha.compiler.JavaClass;
import jkonoha.compiler.PrimitiveClass;

import org.objectweb.asm.Type;

public abstract class KClass extends KObject {
	
	// constant
	public static final KClass voidClass = new JavaClass(void.class);
	public static final KClass varClass = new JavaClass(Object.class); //FIXME
	public static final KClass objectClass = new JavaClass(KObject.class);
	public static final KClass intClass = new PrimitiveClass(int.class, new JavaClass(KInt.class));
	public static final KClass floatClass = new PrimitiveClass(float.class, new JavaClass(KFloat.class));
	public static final KClass booleanClass = new PrimitiveClass(boolean.class, new JavaClass(KBoolean.class));
	public static final KClass stringClass = new JavaClass(KString.class);
	public static final KClass methodClass = new JavaClass(KMethod.class);
	public static final KClass classClass = new JavaClass(KClass.class);
	public static final KClass systemClass = new JavaClass(KSystem.class);
	public static final KClass konohaSpaceClass = new JavaClass(KonohaSpace.class);
	
	public abstract String getName();
	public abstract Type getAsmType();
	public abstract KClass getSuperClass();
	public abstract KClass[] getInterfaces();
	
	public List<KMethod> getMethods() {
		throw new RuntimeException("not impl");
	}
	
	public abstract KMethod getMethod(String name, KClass reqty);
	
	public KClass getBaseClass() {
		throw new RuntimeException("not impl");
	}
	
	public KClass getP0() {
		throw new RuntimeException("not impl");
	}
	
	public boolean isa(KClass k) {
		return !isAssignableFrom(k);
	}
	
	public boolean isAssignableFrom(KClass k) {
		if(this.equals(k)) {
			return true;
		}
		KClass ksup = k.getSuperClass();
		if(ksup != null && ksup.isAssignableFrom(this)) {
			return true;
		}
		for(KClass kif : k.getInterfaces()) {
			if(kif.isAssignableFrom(this)) {
				return true;
			}
		}
		return false;
	}
	
	
}
