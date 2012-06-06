package jkonoha;

import java.util.*;

public class KObject {
	
	private Map<String, Object> kvproto = null;
	
	@Deprecated public void setObject(int key, Object value) {
		setObject(Integer.toString(key), value);
	}
	
	@Deprecated public Object getObject(int key) {
		return getObject(Integer.toString(key));
	}
	
	public void setObject(String key, Object value) {
		if(kvproto == null) {
			kvproto = new HashMap<String, Object>();
		}
		System.out.println("setObject " + this + " " + key + " = " + value);
		kvproto.put(key, value);
	}
	
	public Object getObject(String key) {
		if(kvproto != null) {
			return kvproto.get(key);
		} else {
			return null;
		}
	}
	
	// debug
	void dumpObjects() {
		System.out.println("*** dump " + this);
		if(kvproto == null) {
			System.out.println("null");
		} else
		for(Map.Entry<String, Object> e : kvproto.entrySet()) {
			System.out.println("" + e.getKey() + ": " + e.getValue());
		}
		System.out.println("***");
	}
	
	public static boolean isNull(KObject o) {
		return o == null;
	}
	
	public static boolean isNotNull(KObject o) {
		return o != null;
	}
	
}
