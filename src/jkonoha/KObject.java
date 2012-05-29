package jkonoha;

import java.util.*;

public class KObject {
	
	private Map<Integer, Object> kvproto = null;
	
	public void setObject(int key, Object value) {
		if(kvproto == null) {
			kvproto = new HashMap<Integer, Object>();
		}
		kvproto.put(key, value);
	}
	
	public Object getObject(int key) {
		if(kvproto != null) {
			return kvproto.get(key);
		} else {
			return null;
		}
	}
	
	public static boolean isNull(KObject o) {
		return o == null;
	}
	
	public static boolean isNotNull(KObject o) {
		return o != null;
	}
	
}
