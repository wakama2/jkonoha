package jkonoha;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public class KField extends KObject {
	
	private final int access;
	private final String name;
	private final KClass type;
	
	public KField(int access, String name, KClass type) {
		this.access = access;
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public KClass getReturnClass() {
		return type;
	}
	
	public void accept(ClassVisitor cv) {
		cv.visitField(access, name, type.getAsmType().getDescriptor(), null/*generics*/, null/*value*/);
	}
	
}
