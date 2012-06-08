package jkonoha;

import java.util.List;

import jkonoha.compiler.JavaClass;
import jkonoha.compiler.kobject.KSystem;

import org.objectweb.asm.Type;

public abstract class KClass {
	
	// constant
	public static KClass voidClass = new JavaClass(void.class);
	public static KClass varClass = new JavaClass(Object.class); //FIXME
	public static KClass objectClass = new JavaClass(Object.class);
	public static KClass intClass = new JavaClass(int.class);
	public static KClass booleanClass = new JavaClass(boolean.class);
	public static KClass stringClass = new JavaClass(String.class);
	public static KClass systemClass = new JavaClass(KSystem.class);
	
	public abstract String getName();
	public abstract Type getAsmType();
	public abstract KClass getSuperClass();
	public abstract KClass[] getInterfaces();
	public int getID() {
		return 0;
	}
	public List<KMethod> getMethods() {
		return null;//TODO
	}
	
	public abstract KMethod getMethod(String name, KClass reqty);
	
	public KClass getBaseClass() {
		return null;
	}
	public KClass getP0() {
		return null;
	}
	
	public boolean isa(KClass k) {
		return !isAssignableFrom(k);
	}
	
	// A >: B ->
	//   A.isAssignableFrom(B) == true
	//   B.isAssignableFrom(A) == false
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
