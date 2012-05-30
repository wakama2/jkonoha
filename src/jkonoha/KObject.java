package jkonoha;

import java.util.*;

public class KObject {
	
	private Map<String, Object> kvproto = null;
	
	public void setObject(int key, Object value) {
		setObject(Integer.toString(key), value);
	}
	
	public Object getObject(int key) {
		return getObject(Integer.toString(key));
	}
	
	public void setObject(String key, Object value) {
		if(kvproto == null) {
			kvproto = new HashMap<String, Object>();
		}
		kvproto.put(key, value);
	}
	
	public Object getObject(String key) {
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
