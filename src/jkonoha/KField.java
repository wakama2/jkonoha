package jkonoha;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public class KField extends KObject {
	
	private final int access;
	private final String name;
	private final Type type;
	
	public KField(int access, String name, Type type) {
		this.access = access;
		this.name = name;
		this.type = type;
	}
	
	public void accept(ClassVisitor cv) {
		cv.visitField(access, name, type.getDescriptor(), null/*generics*/, null/*value*/);
	}
	
}
