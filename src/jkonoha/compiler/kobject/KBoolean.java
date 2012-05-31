package jkonoha.compiler.kobject;

import jkonoha.KObject;

public class KBoolean extends KObject {
	
	private final boolean value;
	
	public KBoolean(boolean value) {
		this.value = value;
	}
	
	public static KBoolean box(boolean b) {
		return new KBoolean(b);
	}
	
	public boolean unbox() {
		return value;
	}
	
	public static boolean opNOT(boolean self) {
		return !self;
	}
	
}
