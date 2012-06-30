package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class IntArray extends KObject {
	
	int[] iarray;
	
	IntArray(int size) {
		this.iarray = new int[size];
	}
	
	int get(int n) {
		return iarray[n];
	}
	
	void set(int n, int i) {
		iarray[n] = i;
	}
	
	int getSize(int n) {
		return iarray.length;
	}
	
	void add(int i) {
		iarray = Arrays.copyOf(iarray, iarray.length + 1);
		iarray[iarray.length - 1] = i;
	}
}