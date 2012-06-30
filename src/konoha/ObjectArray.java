package konoha;

import java.util.List;
import java.util.ArrayList;
import jkonoha.KObject;

public class ObjectArray extends KObject {
	
	List<Object> oarray;
	
	ObjectArray(int size) {
		this.oarray = new ArrayList<Object>(size);
	}
	
	Object get(int n) {
		return oarray.get(n);
	}
	
	void set(int n, Object o) {
		oarray.set(n, o);
	}
	
	int getSize() {
		return oarray.size();
	}
	
	void add(Object o) {
		oarray.add(o);
	}
}