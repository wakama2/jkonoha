package jkonoha.ast;

import java.util.*;

import jkonoha.KClass;
import jkonoha.KField;
import jkonoha.KMethod;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class KonohaClass extends KClass {
	
	private final String name;
	private final KClass superClass;
	private final KClass[] interfaceClass;
	private final List<KField> fields = new ArrayList<KField>();
	private final List<KonohaMethod> methods = new ArrayList<KonohaMethod>();
	
	public KonohaClass(String name) {
		this(name, KClass.objectClass, new KClass[0]);
	}
	
	public KonohaClass(String name, KClass superClass, KClass[] interfaceClass) {
		this.name = name;
		this.superClass = superClass;
		this.interfaceClass = interfaceClass;
	}
	
	public static String toGetter(String name) {
		return name + "$get";
	}
	
	public static String toSetter(String name) {
		return name + "$set";
	}
	
	public void createDefaultValue() {
		fields.add(new KField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, "defaultValue", this));
		KonohaMethod m = new KonohaMethod(this, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "<clinit>", KClass.voidClass,
				new String[0], new KClass[0]);
		methods.add(m);
		MethodVisitor mv = m.getNode();
		mv.visitTypeInsn(Opcodes.NEW, name);
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", "()V");
		mv.visitFieldInsn(Opcodes.PUTSTATIC, name, "defaultValue", "L"+name+";");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitEnd();
	}
	
	public void createDefaultConstructor() {
		KonohaMethod m = new KonohaMethod(this, Opcodes.ACC_PUBLIC, "<init>", KClass.voidClass, new String[0], new KClass[0]);
		methods.add(m);
		MethodVisitor mv = m.getNode();
		mv.visitInsn(Opcodes.RETURN);
		mv.visitEnd();
	}
	
	public KMethod getConstructor(List<KClass> args) {
		for(KonohaMethod m : methods) {
			if(m.getName().equals("<init>") && m.getArgClasses().length == args.size()) {
				return m;
			}
		}
		throw new RuntimeException("constructor not found");
	}
	
	public void addMethod(KonohaMethod m) {
		for(int i=0; i<methods.size(); i++) {
			KonohaMethod m1 = methods.get(i);
			if(m1.getName().equals(m.getName())) {
				methods.set(i, m);
				return;
			}
		}
		methods.add(m);
	}
	
	public void addField(KField f) {
		fields.add(f);
		
		// create getter/setter
		{
			String mn = toGetter(f.getName());
			KonohaMethod m = new KonohaMethod(this, Opcodes.ACC_PUBLIC, mn, f.getReturnClass(), new String[0], new KClass[0]);
			addMethod(m);
			MethodVisitor mv = m.getNode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, name, f.getName(), f.getReturnClass().getAsmType().getDescriptor());
			mv.visitInsn(f.getReturnClass().getAsmType().getOpcode(Opcodes.IRETURN));
		}
		{
			String mn = toSetter(f.getName());
			KonohaMethod m = new KonohaMethod(this, Opcodes.ACC_PUBLIC, mn, KClass.voidClass, new String[]{"v"}, new KClass[]{f.getReturnClass()});
			addMethod(m);
			MethodVisitor mv = m.getNode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(f.getReturnClass().getAsmType().getOpcode(Opcodes.ILOAD), 1);
			mv.visitFieldInsn(Opcodes.PUTFIELD, name, f.getName(), f.getReturnClass().getAsmType().getDescriptor());
			mv.visitInsn(Opcodes.RETURN);
		}
	}
	
	/* split by dot (java.lang.String) */
	@Override public String getName() {
		return name;
	}
	
	@Override public KClass getSuperClass() {
		return superClass;
	}
	
	@Override public KClass[] getInterfaces() {
		return interfaceClass;
	}
	
	@Override public Type getAsmType() {
		return Type.getType("L" + name + ";");
	}
	
	@Override public KMethod getMethod(String name, List<KClass> args) {
		for(KonohaMethod m : methods) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		return superClass.getMethod(name, args);
	}
	
	@Override public KMethod getGetter(String fn) {
		String mn = toGetter(fn);
		for(KonohaMethod m : methods) {
			if(m.getName().equals(mn)) {
				return m;
			}
		}
		throw new RuntimeException("not found");
	}
	
	@Override public KMethod getSetter(String fn) {
		String mn = toSetter(fn);
		for(KonohaMethod m : methods) {
			if(m.getName().equals(mn)) {
				return m;
			}
		}
		throw new RuntimeException("not found");
	}
	
	@Override public String toString() {
		return name;
	}
	
	public void accept(ClassVisitor cv) {
		String sn = superClass.getName().replace(".", "/");
		cv.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, name, null/*generics*/, sn/*super name*/, null/*interface name*/);
		for(KField field : fields) {
			field.accept(cv);
		}
		for(KonohaMethod method : methods) {
			method.accept(cv);
		}
	}
	
}