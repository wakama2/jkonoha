package konoha;

import java.util.List;
import java.util.ArrayList;
import jkonoha.KObject;

public class ObjectArray extends KObject {
	
	private List<Object> oarray;
	
	public ObjectArray(int size) {
		this.oarray = new ArrayList<Object>(size);
	}
	
	public static ObjectArray newArray(int size) {
		return new ObjectArray(size);
	}
	
	public Object get(int n) {
		return oarray.get(n);
	}
	
	public void set(int n, Object o) {
		oarray.set(n, o);
	}
	
	public int getSize() {
		return oarray.size();
	}
	
	public void add(Object o) {
		oarray.add(o);
	}
}