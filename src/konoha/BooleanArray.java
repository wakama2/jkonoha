package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class BooleanArray extends KObject {
	
	boolean[] barray;
	
	BooleanArray(int size) {
		this.barray = new boolean[size];
	}
	
	boolean get(int n) {
		return barray[n];
	}
	
	void set(int n, boolean b) {
		barray[n] = b;
	}
	
	int getSize(int n) {
		return barray.length;
	}
	
	void add(boolean b) {
		barray = Arrays.copyOf(barray, barray.length + 1);
		barray[barray.length - 1] = b;
	}
}
