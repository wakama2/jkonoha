package jkonoha.ast;

import jkonoha.KClass;
import jkonoha.KMethod;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.MethodNode;

public class KonohaMethod extends KMethod {
	
	public static final int ACC_STATIC = Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC;
	
	private final KClass parent;
	private final int access;
	private final String name;
	private final KClass retType;
	private final String[] argNames;
	private final KClass[] argTypes;
	private final MethodNode node;
	
	public KonohaMethod(KClass parent, int access, String name, KClass retType, String[] argNames, KClass[] argTypes) {
		this.parent = parent;
		this.access = access;
		this.name = name;
		this.retType = retType;
		this.argNames = argNames;
		this.argTypes = argTypes;
		this.node = new MethodNode(Opcodes.ASM4, access, name, retType.getAsmType().getDescriptor(), null/*generics*/, null/*throws*/);
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
	
	@Override public KClass[] getArgClasses() {
		return argTypes;
	}
	
	@Override public KClass getReturnClass() {
		return retType;
	}
	
	public void accept(ClassVisitor cv) {
		String desc = Type.getMethodDescriptor(getReturnType(), getArgTypes());
		MethodVisitor mv = cv.visitMethod(access, name, desc, null/*generics*/, null/*throws*/);
		node.accept(mv);
	}
	
	@Override public boolean isStatic() {
		return (access & Opcodes.ACC_STATIC) != 0;
	}
	
	@Override public int getCallIns() {
		if(isStatic()) return Opcodes.INVOKESTATIC;
		else return Opcodes.INVOKEVIRTUAL;
	}

}