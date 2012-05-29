package jkonoha.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.Type;

public class JavaMethod extends KMethod {
	
	private final Method method;
	private JavaClass parent = null;
	private Type[] argTypes = null;
	private Type retType = null;
	
	public JavaMethod(Method m) {
		this.method = m;
	}
	
	@Override public KClass getParent() {
		if(parent == null) {
			parent = new JavaClass(method.getDeclaringClass());
		}
		return parent;
	}
	
	@Override public String getName() {
		return method.getName();
	}
	
	@Override public Type[] getArgTypes() {
		if(argTypes == null) {
			Class<?>[] args = method.getParameterTypes();
			argTypes = new Type[args.length];
			for(int i=0; i < args.length; i++) {
				argTypes[i] = Type.getType(args[i]);
			}
		}
		return argTypes;
	}
	
	@Override public Type getReturnType() {
		if(retType == null) {
			retType = Type.getType(method.getReturnType());
		}
		return retType;
	}
	
	@Override public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}
	
}