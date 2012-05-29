package jkonoha.compiler;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.MethodNode;

public class KonohaMethod extends KMethod {
	
	private final KClass parent;
	private final int access;
	private final String name;
	private final Type retType;
	private final String[] argNames;
	private final Type[] argTypes;
	private final MethodNode node;
	
	public KonohaMethod(KClass parent, int access, String name, Type retType, String[] argNames, Type[] argTypes) {
		this.parent = parent;
		this.access = access;
		this.name = name;
		this.retType = retType;
		this.argNames = argNames;
		this.argTypes = argTypes;
		this.node = new MethodNode(Opcodes.ASM4, access, name, retType.getDescriptor(), null/*generics*/, null/*throws*/);
	}
	
	@Override public KClass getParent() {
		return parent;
	}
	
	@Override public String getName() {
		return name;
	}
	
	public MethodNode getNode() {
		return node;
	}
	
	public String[] getArgNames() {
		return argNames;
	}
	
	@Override public Type[] getArgTypes() {
		return argTypes;
	}
	
	@Override public Type getReturnType() {
		return retType;
	}
	
	public void accept(ClassVisitor cv) {
		String desc = Type.getMethodDescriptor(retType, argTypes);
		MethodVisitor mv = cv.visitMethod(access, name, desc, null/*generics*/, null/*throws*/);
		node.accept(mv);
	}
	
	@Override public boolean isStatic() {
		return (access & Opcodes.ACC_STATIC) != 0;
	}
	
}