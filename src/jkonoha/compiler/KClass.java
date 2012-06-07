package jkonoha.compiler;

import java.util.List;

import org.objectweb.asm.Type;

public abstract class KClass extends jkonoha.KClass {
	
	public abstract String getName();
	public abstract Type getAsmType();
	public abstract KClass getSuperClass();
	public abstract KClass[] getInterfaces();
	public int getID() {
		return 0;
	}
	@Override public List<jkonoha.KMethod> getMethods() {
		return null;//TODO
	}
	
	@Override public KClass getBaseClass() {
		return null;
	}
	public KClass getP0() {
		return null;
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
	
	public abstract KMethod getOneMethod(String name);
	
}
