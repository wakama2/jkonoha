package jkonoha.compiler;

import org.objectweb.asm.Type;

public abstract class KClass {
	
	public abstract String getName();
	public abstract Type getAsmType();
	public abstract KClass getSuperClass();
	public abstract KClass[] getInterfaces();
	
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
