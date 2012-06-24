package jkonoha;

import java.util.List;

import jkonoha.compiler.JavaClass;
import jkonoha.compiler.PrimitiveClass;

import org.objectweb.asm.Type;

public abstract class KClass extends KObject {
	
	// constant
	public static final KClass voidClass = JavaClass.create(void.class);
	public static final KClass varClass = JavaClass.create(Object.class); //FIXME
	public static final KClass objectClass = JavaClass.create(KObject.class);
	public static final KClass intClass = new PrimitiveClass(int.class, JavaClass.create(KInt.class));
	public static final KClass floatClass = new PrimitiveClass(float.class, JavaClass.create(KFloat.class));
	public static final KClass booleanClass = new PrimitiveClass(boolean.class, JavaClass.create(KBoolean.class));
	public static final KClass stringClass = JavaClass.create(KString.class);
	public static final KClass methodClass = JavaClass.create(KMethod.class);
	public static final KClass classClass = JavaClass.create(KClass.class);
	public static final KClass systemClass = JavaClass.create(KSystem.class);
	public static final KClass konohaSpaceClass = JavaClass.create(KonohaSpace.class);
	
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
